
(ns reel.updater )

(defn updater [store op op-data op-id]
  (case op
    :task/add (conj store {:done? false, :id op-id, :text op-data})
    :task/remove (filterv (fn [task] (not= (:id task) op-data)) store)
    :task/toggle
      (mapv (fn [task] (if (= (:id task) op-data) (update task :done? not) task)) store)
    :task/edit
      (mapv
       (fn [task]
         (let [[task-id text] op-data]
           (if (= (:id task) task-id) (assoc task :text text) task)))
       store)
    store))
