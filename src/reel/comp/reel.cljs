
(ns reel.comp.reel
  (:require [respo.alias :refer [create-comp div button]]
            [hsl.core :refer [hsl]]
            [respo.comp.debug :refer [comp-debug]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.text :refer [comp-text]]
            [reel.comp.records :refer [comp-records]]))

(def style-panel
  {:bottom 0,
   :background-color colors/paper,
   :width (/ (.-innerWidth js/window) 2),
   :opacity 0.6,
   :right 0,
   :position :fixed,
   :transition-duration "400ms",
   :height (.-innerHeight js/window)})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(def style-link
  (merge style-panel {:transform "scale(0.1)", :transform-origin "100% 100%"}))

(defn render [reel]
  (fn [state mutate!]
    (if (:display? reel)
      (div
       {:style (merge ui/column style-panel)}
       (div
        {}
        (div {:style ui/clickable-text, :event {:click on-toggle}} (comp-text "Close" nil)))
       (div {:style ui/row} (comp-records (:records reel))))
      (div {:style style-link, :event {:click on-toggle}}))))

(def comp-reel (create-comp :reel render))
