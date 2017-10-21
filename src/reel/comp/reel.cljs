
(ns reel.comp.reel
  (:require-macros [respo.macros :refer [defcomp cursor-> <> div button span]])
  (:require [respo.core :refer [create-comp]]
            [hsl.core :refer [hsl]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.space :refer [=<]]
            [reel.comp.records :refer [comp-records]]
            [respo-value.comp.value :refer [comp-value]]
            [reel.style :as style]
            [fipp.edn :refer [pprint]]))

(defn on-run [e dispatch!] (dispatch! :reel/run nil))

(defn on-merge [e dispatch! m!] (dispatch! :reel/merge nil))

(def style-reel
  {:width "50%",
   :height "50%",
   :right 0,
   :bottom 0,
   :position :fixed,
   :background-color (hsl 0 0 100 0.9),
   :border (str "1px solid " (hsl 0 0 90)),
   :font-size 14})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defcomp
 comp-reel
 (states reel user-styles)
 (if (:display? reel)
   (div
    {:style (merge ui/flex ui/row style-reel user-styles)}
    (comp-records (:records reel) (:pointer reel))
    (=< 8 nil)
    (div
     {:style (merge ui/flex ui/column)}
     (div
      {}
      (div {:style ui/clickable-text, :on {:click on-merge}} (<> "Merge"))
      (div {:style ui/clickable-text, :on {:click on-reset}} (<> "Reset"))
      (div {:style ui/clickable-text, :on {:click on-run}} (<> "Run"))
      (if (not (:stopped? reel))
        (div {:style ui/clickable-text, :on {:click on-toggle}} (<> "Close"))))
     (div
      {:style (merge ui/column ui/flex {:overflow :auto})}
      (let [records (:records reel), pointer (:pointer reel)]
        (div
         {:style (merge style/code {:font-size 12})}
         (<> (pr-str (get (get records (dec pointer)) 1)))))
      (div
       {:style (merge style/code {:font-size 12, :white-space :pre})}
       (<> (with-out-str (pprint (:store reel))))))))
   (span {})))
