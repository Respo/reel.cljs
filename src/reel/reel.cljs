
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
      :reel/view reel
      :reel/run reel
      (do (println "Unknown reel/ op:" op) reel))
    (let [data-pack [op op-data op-id]]
      (if (:stopped? reel)
        (-> reel (update :records (fn [records] (conj records data-pack))))
        (-> reel
            (assoc :store (updater (:store reel) op op-data op-id))
            (update :records (fn [records] (conj records data-pack))))))))
