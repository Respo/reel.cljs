
(ns reel.util )

(def id-ref (atom 0))

(defn id! [] (swap! id-ref inc) @id-ref)
