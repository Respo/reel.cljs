
(ns reel.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :states (update store :states (mutate op-data))
    :task/add
      (update store :tasks (fn [tasks] (cons {:id op-id, :done? false, :text op-data} tasks)))
    :task/remove
      (update store :tasks (fn [tasks] (filter (fn [task] (not= (:id task) op-data)) tasks)))
    :task/toggle
      (update
       store
       :tasks
       (fn [tasks]
         (map (fn [task] (if (= (:id task) op-data) (update task :done? not) task)) tasks)))
    :task/edit
      (update
       store
       :tasks
       (fn [tasks]
         (map
          (fn [task]
            (let [[task-id text] op-data]
              (if (= (:id task) task-id) (assoc task :text text) task)))
          tasks)))
    store))
