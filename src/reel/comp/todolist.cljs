
(ns reel.comp.todolist
  (:require [respo.alias :refer [create-comp div button input]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo-ui.common :refer [init-input update-input on-input]]
            [respo-ui.style :as ui]
            [reel.comp.task :refer [comp-task]]))

(defn on-click [mutate! text]
  (fn [state dispatch!] (dispatch! :task/add text) (mutate! "")))

(def style-container {:padding 8})

(defn render [tasks]
  (fn [state mutate!]
    (div
     {:style style-container}
     (div
      {}
      (input
       {:style ui/input,
        :event {:input (on-input mutate!)},
        :attrs {:placeholder "Task to add...", :value state}})
      (comp-space 8 nil)
      (button
       {:style ui/button, :event {:click (on-click mutate! state)}}
       (comp-text "Add" nil)))
     (div {} (->> tasks (map (fn [task] [(:id task) (comp-task task)])))))))

(def comp-todolist (create-comp :todolist init-input update-input render))
