
(ns reel.comp.todolist
  (:require [respo.macros :refer [defcomp <> div span button input list->]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [reel.comp.task :refer [comp-task]]
            [keycode.core :as keycode]))

(def style-container {:padding 8, :overflow :auto})

(defn on-click [state] (fn [e dispatch! mutate!] (dispatch! :task/add state) (mutate! "")))

(defn on-input [e dispatch! mutate!] (mutate! (:value e)))

(defcomp
 comp-todolist
 (states tasks)
 (let [state (or (:data states) "")]
   (div
    {:style (merge ui/fullscreen style-container)}
    (div
     {}
     (input
      {:placeholder "Task to add...",
       :value state,
       :style ui/input,
       :on {:input on-input,
            :keydown (fn [e d! m!]
              (if (= (:keycode e) keycode/return) (do (d! :task/add state) (m! ""))))}})
     (=< 8 nil)
     (button {:style ui/button, :on {:click (on-click state)}} (<> "Add")))
    (list-> :div {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))
