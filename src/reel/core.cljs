
(ns reel.core (:require [clojure.string :as string]))

(defn reel-updater [updater reel op op-data op-id]
  (comment println "Name:" (name op))
  (if (string/starts-with? (str op) ":reel/")
    (merge
     reel
     (case op
       :reel/toggle {:display? (not (:display? reel))}
       :reel/recall
         (let [[idx a-store] op-data] {:pointer idx, :stopped? true, :store a-store})
       :reel/run {:store op-data, :stopped? false, :pointer nil}
       :reel/merge
         {:store op-data, :initial-store op-data, :stopped? false, :pointer nil, :records []}
       :reel/reset
         {:store (:initial-store reel), :pointer nil, :records [], :stopped? false}
       (do (println "Unknown reel/ op:" op) nil)))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))

(defn play-records [store records updater]
  (if (empty? records)
    store
    (let [[op op-data op-id] (first records), next-store (updater store op op-data op-id)]
      (recur next-store (rest records) updater))))

(defn replay-store [reel updater idx]
  (let [records-slice (if (some? idx) (subvec (:records reel) 0 idx) (:records reel))]
    (play-records (:initial-store reel) records-slice updater)))
