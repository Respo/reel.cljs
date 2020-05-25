
(ns reel.comp.todolist
  (:require [respo.core :refer [defcomp <> div span button input list->]]
            [respo.comp.space :refer [=<]]
            [respo-ui.core :as ui]
            [reel.comp.task :refer [comp-task]]))

(def style-container {:padding 8, :overflow :auto})

(defcomp
 comp-todolist
 (states tasks)
 (let [cursor (:cursor states), state (or (:data states) "")]
   (div
    {:style (merge ui/fullscreen style-container)}
    (div
     {}
     (input
      {:placeholder "Task to add...",
       :value state,
       :style ui/input,
       :on-input (fn [e d!] (d! cursor (:value e))),
       :on-keydown (fn [e d!] (if (= (:keycode e) 13) (do (d! :task/add state) (d! [] ""))))})
     (=< 8 nil)
     (button
      {:style ui/button, :on-click (fn [e d!] (d! :task/add state) (d! cursor ""))}
      (<> "Add")))
    (list-> {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))
