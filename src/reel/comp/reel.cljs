
(ns reel.comp.reel
  (:require [respo.macros :refer [defcomp cursor-> <> div button span]]
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
  {:width "60%",
   :height "60%",
   :right 0,
   :bottom 0,
   :position :fixed,
   :background-color (hsl 0 0 100 0.7),
   :border (str "1px solid " (hsl 0 0 90)),
   :font-size 14,
   :backdrop-filter "blur(2px)"})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defn render-button [guide on-click enabled?]
  (div
   {:style (merge
            ui/clickable-text
            {:user-select :none}
            (if (not enabled?) {:color (hsl 0 0 90)})),
    :on {:click (if enabled? on-click identity)}}
   (<> guide)))

(defn on-step [e d! m!] (d! :reel/step nil))

(defcomp
 comp-reel
 (states reel user-styles)
 (if (:display? reel)
   (div
    {:style (merge ui/flex ui/column style-reel user-styles)}
    (div
     {}
     (render-button "Merge" on-merge true)
     (render-button "Reset" on-reset true)
     (render-button "Step" on-step (:stopped? reel))
     (render-button "Run" on-run (:stopped? reel))
     (render-button "Close" on-toggle (not (:stopped? reel))))
    (div
     {:style ui/row}
     (comp-records (:records reel) (:pointer reel))
     (=< 8 nil)
     (div
      {:style (merge ui/column ui/flex {:overflow :auto})}
      (let [records (:records reel), pointer (:pointer reel)]
        (div
         {:style (merge style/code {:font-size 12})}
         (<>
          (with-out-str
           (pprint (if (:stopped? reel) (get records (dec pointer)) (last records)))))))
      (div
       {:style (merge
                style/code
                {:font-size 12,
                 :white-space :pre,
                 :padding "8px 0 32px 0",
                 :line-height "20px"})}
       (<> (with-out-str (pprint (:store reel))))))))
   (span {})))
