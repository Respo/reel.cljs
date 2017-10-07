
(ns reel.util )

(def *id (atom 0))

(defn id! [] (swap! *id inc) @*id)
