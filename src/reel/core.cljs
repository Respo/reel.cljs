
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
     (case op
       :reel/toggle {:display? (not (:display? reel))}
       :reel/recall
         (let [idx op-data
               new-store (play-records (:base reel) (subvec (:records reel) 0 idx) updater)]
           {:pointer idx, :stopped? true, :store new-store})
       :reel/run
         (let [new-store (play-records (:base reel) (:records reel) updater)]
           {:store new-store, :stopped? false, :pointer nil})
       :reel/merge
         (let [new-store (play-records (:base reel) (:records reel) updater)]
           {:store new-store, :base new-store, :stopped? false, :pointer nil, :records []})
       :reel/reset {:store (:base reel), :pointer nil, :records [], :stopped? false}
       (do (println "Unknown reel/ op:" op) nil)))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))

(defonce *code (atom nil))

(defn handle-reload! [store0 updater comp-container *reel clear-cache!]
  (if (or (not (identical? updater (:updater @*code)))
          (not (identical? store0 (:base @*code))))
    (do
     (swap! *code merge {:updater updater, :base store0})
     (swap!
      *reel
      assoc
      :store
      (let [reel @*reel]
        (play-records
         (:base reel)
         (if (:stopped? reel) (subvec (:records reel) 0 (:pointer reel)) (:records reel))
         updater)))))
  (if (not (identical? comp-container (:view @*code)))
    (do (swap! *code assoc :view comp-container) (clear-cache!))))

(defn listen-devtools! [keyboard dispatch!]
  (.addEventListener
   js/window
   "keydown"
   (fn [event]
     (if (and (.-shiftKey event)
              (.-metaKey event)
              (= (.charCodeAt (string/upper-case keyboard)) (.-keyCode event)))
       (dispatch! :reel/toggle nil)))))
