
(ns reel.comp.task
  (:require [respo.alias :refer [create-comp div input button]]
            [hsl.core :refer [hsl]]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]))

(defn on-input [task-id] (fn [e dispatch!] (dispatch! :task/edit [task-id (:value e)])))

(def style-done
  {:background-color colors/attractive, :width 32, :display :inline-block, :height 32})

(defn on-toggle [task-id] (fn [e dispatch!] (dispatch! :task/toggle task-id)))

(defn on-remove [task-id] (fn [e dispatch!] (dispatch! :task/remove task-id)))

(defn render [task]
  (fn [state mutate!]
    (div
     {}
     (div
      {:style (merge style-done (if (:done? task) {:background-color colors/warm})),
       :event {:click (on-toggle (:id task))}})
     (comp-space 8 nil)
     (input
      {:style ui/input,
       :event {:input (on-input (:id task))},
       :attrs {:placeholder "Content of task", :value (:text task)}})
     (comp-space 8 nil)
     (button
      {:style (merge ui/button {:background-color colors/irreversible}),
       :event {:click (on-remove (:id task))}}
      (comp-text "Remove" nil)))))

(def comp-task (create-comp :task render))
