
(ns reel.reel (:require [clojure.string :as string]))

(def reel-schema
  {:viewer {:pointer 0, :tab :records},
   :initial-store nil,
   :store nil,
   :records [],
   :stopped? false,
   :display? false})

(defn reel-updater [updater reel op op-data op-id]
  (println "Name:" (name op))
  (if (string/starts-with? (str op) ":reel/")
    (case op
      :reel/toggle (update reel :display? not)
      :reel/view reel
      :reel/run reel
      (do (println "Unknown reel/ op:" op) reel))
    (-> reel
        (assoc :store (updater (:store reel) op op-data op-id))
        (update :records (fn [records] (conj records [op op-data op-id]))))))
