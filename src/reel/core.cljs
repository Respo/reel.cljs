
(ns reel.core (:require [clojure.string :as string]))

(defn play-records [store records updater]
  (if (empty? records)
    store
    (let [[op op-data op-id] (first records), next-store (updater store op op-data op-id)]
      (recur next-store (rest records) updater))))

(defn reel-updater [updater reel op op-data op-id]
  (comment println "Name:" (name op))
  (if (string/starts-with? (str op) ":reel/")
    (merge
     reel
     (let [pointer (:pointer reel)
           records (:records reel)
           base (:base reel)
           stopped? (:stopped? reel)]
       (case op
         :reel/toggle {:display? (not (:display? reel))}
         :reel/recall
           (let [idx op-data
                 new-store (play-records
                            (:base reel)
                            (subvec (:records reel) 0 idx)
                            updater)]
             {:pointer idx, :stopped? true, :store new-store})
         :reel/run
           (let [new-store (play-records (:base reel) (:records reel) updater)]
             {:store new-store, :stopped? false, :pointer nil})
         :reel/merge
           (if stopped?
             (if (zero? pointer)
               {}
               (let [new-store (play-records base (subvec records 0 pointer) updater)]
                 {:store new-store,
                  :base new-store,
                  :pointer 0,
                  :records (subvec records pointer),
                  :merged? true}))
             {:base (:store reel), :pointer nil, :records [], :merged? true})
         :reel/reset
           (if stopped?
             {:records (subvec records 0 pointer)}
             {:store (:base reel), :pointer nil, :records [], :stopped? false})
         (do (.warn js/console "Unknown reel/ op:" op) nil))))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))

(defn listen-devtools! [keyboard dispatch!]
  (.addEventListener
   js/window
   "keydown"
   (fn [event]
     (if (and (.-shiftKey event)
              (.-metaKey event)
              (= (.charCodeAt (string/upper-case keyboard)) (.-keyCode event)))
       (dispatch! :reel/toggle nil)))))

(defn refresh-reel [reel base updater]
  (let [next-base (if (:merged? reel) (:base reel) base)]
    (-> reel
        (assoc :base next-base)
        (assoc
         :store
         (play-records
          next-base
          (if (:stopped? reel) (subvec (:records reel) 0 (:pointer reel)) (:records reel))
          updater)))))
