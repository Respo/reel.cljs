
(ns reel.comp.task
  (:require [respo.macros :refer [defcomp <> div button input]]
            [hsl.core :refer [hsl]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]))

(defn on-input [task-id] (fn [e dispatch!] (dispatch! :task/edit [task-id (:value e)])))

(defn on-remove [task-id] (fn [e dispatch!] (dispatch! :task/remove task-id)))

(defn on-toggle [task-id] (fn [e dispatch!] (dispatch! :task/toggle task-id)))

(def style-container {:margin "8px 0", :height 32})

(def style-done
  {:width 32,
   :height 32,
   :display :inline-block,
   :background-color colors/attractive,
   :cursor :pointer})

(defcomp
 comp-task
 (task)
 (div
  {:style style-container}
  (div
   {:style (merge style-done (if (:done? task) {:background-color colors/warm})),
    :on-click (on-toggle (:id task))})
  (=< 8 nil)
  (input
   {:value (:text task),
    :placeholder "Content of task",
    :on-input (on-input (:id task)),
    :style ui/input})
  (=< 8 nil)
  (button
   {:style (merge ui/button {:background-color colors/irreversible}),
    :on-click (on-remove (:id task))}
   (<> "Remove"))))
