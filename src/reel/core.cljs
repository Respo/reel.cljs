
(ns reel.core (:require [clojure.string :as string] ["shortid" :as shortid]))

(defn play-records [store records updater]
  (if (empty? records)
    store
    (let [[op op-data op-id] (first records), next-store (updater store op op-data op-id)]
      (recur next-store (rest records) updater))))

(defn reel-updater [updater reel op op-data]
  (comment println "Name:" (name op))
  (let [op-id (.generate shortid), op-time (.valueOf (js/Date.))]
    (if (string/starts-with? (str op) ":reel/")
      (merge
       reel
       (let [pointer (:pointer reel)
             records (:records reel)
             base (:base reel)
             store (:store reel)
             stopped? (:stopped? reel)]
         (case op
           :reel/toggle {:display? (not (:display? reel))}
           :reel/recall
             (let [idx op-data, new-store (play-records base (subvec records 0 idx) updater)]
               {:pointer idx, :stopped? true, :store new-store})
           :reel/run
             (let [new-store (play-records base records updater)]
               {:store new-store, :stopped? false, :pointer nil})
           :reel/step
             (if stopped?
               (if (< (count records) 2)
                 nil
                 (if (< pointer (count records))
                   (let [next-pointer (inc pointer)
                         next-record (nth records pointer)
                         [old-op old-data old-id old-time] next-record]
                     {:pointer next-pointer,
                      :store (updater (:store reel) old-op old-data old-id old-time)})
                   {:store base, :pointer 0}))
               nil)
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
           :reel/remove
             (let [idx op-data]
               (if (zero? idx)
                 reel
                 (-> reel
                     (update :pointer dec)
                     (update
                      :records
                      (fn [records]
                        (vec (concat (subvec records 0 (dec idx)) (subvec records idx)))))
                     (assoc :store (play-records base (subvec records 0 (dec idx)) updater)))))
           (do (.warn js/console "Unknown reel/ op:" op) nil))))
      (let [data-pack [op op-data op-id op-time]]
        (if (:stopped? reel)
          (-> reel (update :records (fn [records] (conj records data-pack))))
          (-> reel
              (assoc :store (updater (:store reel) op op-data op-id op-time))
              (update :records (fn [records] (conj records data-pack)))))))))

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
