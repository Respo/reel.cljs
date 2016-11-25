
(ns reel.reel (:require [clojure.string :as string]))

(def reel-schema
  {:viewer {:pointer 0, :tab :records},
   :initial-store nil,
   :store nil,
   :records [],
   :stopped? false})

(defn reel-updater [updater reel op op-data op-id]
  (if (string/starts-with? (name op) "reel/")
    (case op :reel/view reel :reel/run reel (do (println "Unknown reel/ op:" op) reel))
    (-> reel
        (assoc :store (updater (:store reel) op op-data op-id))
        (update :records (fn [records] (conj records [op op-data op-id]))))))
