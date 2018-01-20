
(ns reel.comp.records
  (:require [respo.macros :refer [defcomp <> div span style list->]]
            [respo-ui.core :as ui]
            [respo-ui.colors :as colors]
            [respo.comp.space :refer [=<]]
            [reel.style :as style]))

(defn on-recall [idx] (fn [e dispatch!] (dispatch! :reel/recall idx)))

(def style-container
  {:overflow :auto,
   :flex-shrink 0,
   :padding-bottom 120,
   :padding-top 16,
   :width 160,
   :font-size 12})

(def style-record
  {:cursor :pointer,
   :padding "0 8px",
   :white-space :nowrap,
   :overflow :hidden,
   :text-overflow :ellipsis})

(defcomp
 comp-records
 (records pointer)
 (div
  {:style (merge style/code style-container)}
  (style {:innerHTML ".record-item:hover{\n  background-color: #eee;\n}"})
  (list->
   :div
   {}
   (->> records
        (cons [:base nil :base])
        (map-indexed
         (fn [idx record]
           [(last record)
            (div
             {:class-name "record-item",
              :style (merge
                      style-record
                      (if (= pointer idx)
                        {:background-color colors/attractive, :color :white})),
              :on-click (on-recall idx)}
             (<> (pr-str (first record))))]))))))

(def style-data
  {:max-width 100,
   :overflow :hidden,
   :text-overflow :ellipsis,
   :white-space :nowrap,
   :display :inline-block,
   :vertical-align :middle})
