
(ns reel.comp.reel
  (:require-macros [respo.macros :refer [defcomp <> div button span]])
  (:require [respo.core :refer [create-comp]]
            [hsl.core :refer [hsl]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.space :refer [=<]]
            [reel.comp.records :refer [comp-records]]))

(defn on-run [new-store] (fn [e dispatch!] (dispatch! :reel/run new-store)))

(defn on-merge [e dispatch! m!] (dispatch! :reel/merge nil))

(def style-reel
  {:width "50%",
   :height "50%",
   :right 0,
   :bottom 0,
   :position :fixed,
   :background-color (hsl 0 0 100 0.9),
   :border (str "1px solid " (hsl 0 0 90))})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defcomp
 comp-reel
 (reel updater user-styles)
 (if (:display? reel)
   (div
    {:style (merge ui/flex ui/row style-reel user-styles)}
    (comp-records (:records reel) (:pointer reel))
    (=< 8 nil)
    (div
     {}
     (div
      {}
      (div {:style ui/clickable-text, :on {:click on-run}} (<> "Run"))
      (div {:style ui/clickable-text, :on {:click on-merge}} (<> "Merge"))
      (div {:style ui/clickable-text, :on {:click on-reset}} (<> "Reset"))
      (if (not (:stopped? reel))
        (div {:style ui/clickable-text, :on {:click on-toggle}} (<> "Close"))))
     (div {:style ui/row} (<> (:store reel)))))
   (span {})))
