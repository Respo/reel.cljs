
(ns reel.comp.records
  (:require-macros [respo.macros :refer [defcomp <> div span style]])
  (:require [respo.core :refer [create-comp]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.space :refer [=<]]
            [reel.style :as style]))

(def style-record {:cursor :pointer, :padding "0 8px", :white-space :nowrap})

(def style-container
  {:overflow :auto,
   :flex-shrink 0,
   :padding-bottom 200,
   :padding-top 32,
   :width 120,
   :font-size 12})

(def style-data
  {:max-width 100,
   :overflow :hidden,
   :text-overflow :ellipsis,
   :white-space :nowrap,
   :display :inline-block,
   :vertical-align :middle})

(defn on-recall [idx] (fn [e dispatch!] (dispatch! :reel/recall idx)))

(defcomp
 comp-records
 (records pointer)
 (div
  {:style (merge style/code style-container)}
  (style {:innerHTML ".record-item:hover{\n  background-color: #eee;\n}"})
  (div
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
              :on {:click (on-recall idx)}}
             (<> (pr-str (first record))))]))))))
