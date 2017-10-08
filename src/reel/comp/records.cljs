
(ns reel.comp.records
  (:require-macros [respo.macros :refer [defcomp <> div span]])
  (:require [respo.core :refer [create-comp]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.space :refer [=<]]))

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

(defn on-recall [idx] (fn [e dispatch!] (dispatch! :reel/recall idx)))

(defcomp
 comp-records
 (records pointer)
 (div
  {:style style-container}
  (->> records
       (cons [:base nil :base])
       (map-indexed
        (fn [idx record]
          [(last record)
           (div
            {:style (merge
                     style-record
                     (if (= pointer idx)
                       {:background-color colors/attractive, :color :white})),
             :on {:click (on-recall idx)}}
            (<> (pr-str (first record)))
            (=< 8 nil)
            (<> span (get record 1) style-data))])))))
