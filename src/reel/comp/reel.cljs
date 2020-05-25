
(ns reel.comp.reel
  (:require [respo.core :refer [defcomp <> >> div button span]]
            [hsl.core :refer [hsl]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.core :as ui]
            [respo.comp.space :refer [=<]]
            [reel.comp.records :refer [comp-records]]
            [respo-value.comp.value :refer [comp-value]]
            [reel.style :as style]
            [favored-edn.core :refer [write-edn]]))

(defn render-button [guide on-click enabled?]
  (div
   {:style (merge ui/link {:user-select :none} (if (not enabled?) {:color (hsl 0 0 90)})),
    :on-click (if enabled? on-click identity)}
   (<> guide)))

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

(defcomp
 comp-reel
 (states reel user-styles)
 (if (:display? reel)
   (div
    {:style (merge ui/flex ui/column style-reel user-styles)}
    (div
     {}
     (render-button "Merge" (fn [e d!] (d! :reel/merge nil)) true)
     (render-button "Reset" (fn [e d!] (d! :reel/reset nil)) true)
     (render-button "Step" (fn [e d!] (d! :reel/step nil)) (:stopped? reel))
     (render-button "Run" (fn [e d!] (d! :reel/run nil)) (:stopped? reel))
     (render-button "Close" (fn [e d!] (d! :reel/toggle nil)) (not (:stopped? reel))))
    (div
     {:style (merge ui/expand ui/row)}
     (comp-records (:records reel) (:pointer reel))
     (div
      {:style (merge ui/column ui/flex {:overflow :auto, :padding "0 8px"})}
      (let [records (:records reel), pointer (:pointer reel)]
        (div
         {:style (merge ui/row-parted style/code {:font-size 12})}
         (<> (pr-str (if (:stopped? reel) (get records (dec pointer)) (last records))))
         (if (and (some? pointer) (not= pointer 0))
           (span
            {:inner-text "Remove",
             :style {:cursor :pointer,
                     :font-size 12,
                     :font-family ui/font-fancy,
                     :color (hsl 200 100 84)},
             :on-click (fn [e d!] (d! :reel/remove (:pointer reel)))}))))
      (div
       {:style (merge
                style/code
                {:font-size 12,
                 :white-space :pre,
                 :padding "8px 0px 32px 0",
                 :line-height "20px",
                 :overflow :auto})}
       (<> (write-edn (:store reel)))))))
   (span {})))
