
(ns reel.comp.task
  (:require [respo.core :refer [defcomp <> div button input]]
            [hsl.core :refer [hsl]]
            [respo.comp.space :refer [=<]]
            [respo-ui.core :as ui]))

(defn on-input [task-id] (fn [e dispatch!] (dispatch! :task/edit [task-id (:value e)])))

(defn on-remove [task-id] (fn [e dispatch!] (dispatch! :task/remove task-id)))

(defn on-toggle [task-id] (fn [e dispatch!] (dispatch! :task/toggle task-id)))

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
    :on-click (on-toggle (:id task))})
  (=< 8 nil)
  (input
   {:value (:text task),
    :placeholder "Content of task",
    :on-input (on-input (:id task)),
    :style ui/input})
  (=< 8 nil)
  (button
   {:style (merge
            ui/button
            {:background-color (hsl 6 100 60), :color :white, :border :none}),
    :on-click (on-remove (:id task))}
   (<> "Remove"))))
