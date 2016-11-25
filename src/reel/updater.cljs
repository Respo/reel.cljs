
(ns reel.updater )

(defn updater [store op op-data op-id]
  (case op
    :task/add (cons {:done? false, :id op-id, :text op-data} store)
    :task/remove (filter (fn [task] (not= (:id task) op-data)) store)
    :task/toggle
      (map (fn [task] (if (= (:id task) op-data) (update task :done? not) task)) store)
    :task/edit
      (map
       (fn [task]
         (let [[task-id text] op-data]
           (if (= (:id task) task-id) (assoc task :text text) task)))
       store)
    store))
