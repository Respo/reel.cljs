
(ns reel.comp.container
  (:require-macros [respo.macros :refer [defcomp <> div span]])
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo.core :refer [create-comp]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]
            [reel.comp.todolist :refer [comp-todolist]]))

(defcomp
 comp-container
 (reel updater server?)
 (let [store (:store reel), states (:states store)]
   (div
    {:style (merge ui/global)}
    (comp-todolist states (:tasks store))
    (comp-reel reel updater server?)
    (<> reel))))
