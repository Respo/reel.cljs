
(ns reel.comp.task
  (:require [respo.core :refer [defcomp <> div button input]]
            [hsl.core :refer [hsl]]
            [respo.comp.space :refer [=<]]
            [respo-ui.core :as ui]))

(def style-container {:margin "8px 0", :height 32})

(def style-done
  {:width 32,
   :height 32,
   :display :inline-block,
   :background-color (hsl 220 100 76),
   :cursor :pointer})

(defcomp
 comp-task
 (task)
 (div
  {:style style-container}
  (div
   {:style (merge style-done (if (:done? task) {:background-color (hsl 42 100 60)})),
    :on-click (fn [e d!] (d! :task/toggle (:id task)))})
  (=< 8 nil)
  (input
   {:value (:text task),
    :placeholder "Content of task",
    :on-input (fn [e d!] (d! :task/edit [(:id task) (:value e)])),
    :style ui/input})
  (=< 8 nil)
  (button
   {:style (merge
            ui/button
            {:background-color (hsl 6 100 60), :color :white, :border :none}),
    :on-click (fn [e d!] (d! :task/remove (:id task)))}
   (<> "Remove"))))
