
(ns reel.schema )

(def reel
  {:records [], :initial nil, :store nil, :pointer 0, :stopped? false, :display? false})

(def store {:states {}, :tasks (list)})
