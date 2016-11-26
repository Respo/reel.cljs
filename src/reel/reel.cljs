
(ns reel.reel (:require [clojure.string :as string]))

(def reel-schema
  {:initial-store nil,
   :store nil,
   :records [],
   :pointer 0,
   :tab :records,
   :stopped? false,
   :display? false})

(defn reel-updater [updater reel op op-data op-id]
  (comment println "Name:" (name op))
  (if (string/starts-with? (str op) ":reel/")
    (case op
      :reel/toggle (update reel :display? not)
      :reel/recall
        (let [[idx a-store] op-data]
          (-> reel (assoc :pointer idx) (assoc :stopped? true) (assoc :store a-store)))
      :reel/run
        (-> reel (assoc :store op-data) (assoc :stopped? false) (assoc :pointer nil))
      :reel/merge
        (-> reel
            (assoc :store op-data)
            (assoc :initial-store op-data)
            (assoc :stopped? false)
            (assoc :pointer nil)
            (assoc :records []))
      :reel/reset
        (-> reel
            (assoc :store (:initial-store reel))
            (assoc :pointer nil)
            (assoc :records [])
            (assoc :stopped? false))
      (do (println "Unknown reel/ op:" op) reel))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))
