
(ns reel.comp.todolist
  (:require-macros [respo.macros :refer [defcomp <> div span button input]])
  (:require [respo.core :refer [create-comp]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [reel.comp.task :refer [comp-task]]))

(def style-container {:padding 8})

(defn on-click [state cursor]
  (fn [e dispatch!] (dispatch! :task/add state) (dispatch! :states [cursor ""])))

(defn on-input [cursor] (fn [e dispatch!] (dispatch! :states [cursor (:value e)])))

(defcomp
 comp-todolist
 (states tasks)
 (let [state (or (:data states) "")]
   (div
    {:style style-container}
    (div
     {}
     (input
      {:placeholder "Task to add...",
       :value state,
       :style ui/input,
       :on {:input (on-input *cursor*)}})
     (=< 8 nil)
     (button {:style ui/button, :on {:click (on-click state *cursor*)}} (<> "Add")))
    (div {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))
