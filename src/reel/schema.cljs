
(ns reel.schema )

(def reel
  {:records [],
   :base nil,
   :store nil,
   :pointer nil,
   :stopped? false,
   :display? false,
   :merged? false})

(def store {:states {}, :tasks (list)})
