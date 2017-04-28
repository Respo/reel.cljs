
(ns reel.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo.alias :refer [create-comp div span]]
            [respo.comp.space :refer [comp-space]]
            [respo.comp.text :refer [comp-text]]
            [reel.comp.reel :refer [comp-reel]]
            [reel.comp.todolist :refer [comp-todolist]]))

(def comp-container
  (create-comp
   :container
   (fn [reel updater server?]
     (fn [state mutate!]
       (let [store (:store reel), states (:states store)]
         (div
          {:style (merge ui/global)}
          (comp-todolist states (:tasks store))
          (comp-reel reel updater server?)))))))
