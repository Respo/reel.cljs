
(ns reel.comp.todolist
  (:require-macros [respo.macros :refer [defcomp <> div span button input]])
  (:require [respo.core :refer [create-comp]]
            [respo.comp.space :refer [=<]]
            [respo-ui.style :as ui]
            [reel.comp.task :refer [comp-task]]))

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
      {:placeholder "Task to add...", :value state, :style ui/input, :on {:input on-input}})
     (=< 8 nil)
     (button {:style ui/button, :on {:click (on-click state)}} (<> "Add")))
    (div {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))
