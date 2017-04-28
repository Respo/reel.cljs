
(ns reel.comp.records
  (:require [respo.alias :refer [create-comp div]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]))

(def style-record {:cursor :pointer, :padding "0 8px"})

(def style-container
  {:overflow :auto, :flex-shrink 0, :padding-bottom 200, :padding-top 40})

(def style-data
  {:max-width 100,
   :overflow :hidden,
   :text-overflow :ellipsis,
   :white-space :nowrap,
   :display :inline-block,
   :vertical-align :middle})

(defn render [records pointer on-recall]
  (fn [state mutate!]
    (div
     {:style style-container}
     (->> records
          (cons [:initial nil :initial])
          (map-indexed
           (fn [idx record]
             [(last record)
              (div
               {:style (merge
                        style-record
                        (if (= pointer idx)
                          {:background-color colors/attractive, :color :white})),
                :event {:click (on-recall idx)}}
               (comp-text (first record) nil)
               (comp-space 8 nil)
               (comp-text (get record 1) style-data))]))))))

(def comp-records (create-comp :records render))
