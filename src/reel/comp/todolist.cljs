
(ns reel.comp.todolist
  (:require [respo.alias :refer [create-comp div button input]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo-ui.style :as ui]
            [reel.comp.task :refer [comp-task]]))

(def style-container {:padding 8})

(defn on-click [cursor]
  (fn [e dispatch!] (dispatch! :task/add nil) (dispatch! :states [cursor ""])))

(defn on-input [cursor] (fn [e dispatch!] (dispatch! :states [cursor (:value e)])))

(def comp-todolist
  (create-comp
   :todolist
   (fn [states tasks]
     (fn [cursor]
       (let [state (:data states)]
         (div
          {:style style-container}
          (div
           {}
           (input
            {:style ui/input,
             :event {:input (on-input cursor)},
             :attrs {:placeholder "Task to add...", :value state}})
           (comp-space 8 nil)
           (button
            {:style ui/button, :event {:click (on-click cursor)}}
            (comp-text "Add" nil)))
          (div {} (->> tasks (map (fn [task] [(:id task) (comp-task task)]))))))))))
