
(ns reel.comp.records
  (:require [respo.alias :refer [create-comp div]]
            [respo-ui.style :as ui]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]))

(def style-data
  {:text-overflow :ellipsis,
   :vertical-align :middle,
   :white-space :nowrap,
   :overflow :hidden,
   :max-width 100,
   :display :inline-block})

(def style-container {:overflow :auto})

(defn render [records]
  (fn [state mutate!]
    (div
     {:style style-container}
     (->> records
          (map
           (fn [record]
             [(last record)
              (div
               {}
               (comp-text (first record) nil)
               (comp-space 8 nil)
               (comp-text (get record 1) style-data))]))))))

(def comp-records (create-comp :records render))
