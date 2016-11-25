
(ns reel.comp.reel
  (:require [respo.alias :refer [create-comp div button]]
            [hsl.core :refer [hsl]]
            [respo.comp.debug :refer [comp-debug]]))

(defn render [reel] (fn [state mutate!] (div {} (comp-debug (:store reel)))))

(def comp-reel (create-comp :reel render))
