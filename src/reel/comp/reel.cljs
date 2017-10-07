
(ns reel.comp.reel
  (:require-macros [respo.macros :refer [defcomp <> div button]])
  (:require [respo.core :refer [create-comp]]
            [hsl.core :refer [hsl]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.space :refer [=<]]
            [reel.comp.records :refer [comp-records]]
            [reel.core :refer [replay-store play-records]]))

(defn on-run [new-store] (fn [e dispatch!] (dispatch! :reel/run new-store)))

(defn on-merge [new-store] (fn [e dispatch!] (dispatch! :reel/merge new-store)))

(def style-panel
  {:position :fixed,
   :right 0,
   :bottom 0,
   :transition-duration "400ms",
   :background-color colors/paper,
   :opacity 0.8})

(defn style-size [server?]
  {:width (if server? 600 (/ (.-innerWidth js/window) 1.5)),
   :height (if server? 600 (.-innerHeight js/window))})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(def style-link
  (merge style-panel {:transform "scale(0.2)", :transform-origin "100% 100%"}))

(defn on-recall [reel updater]
  (fn [idx] (fn [e dispatch!] (dispatch! :reel/recall [idx (replay-store reel updater idx)]))))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defcomp
 comp-reel
 (reel updater server?)
 (if (:display? reel)
   (div
    {:style (merge ui/row style-panel (style-size server?))}
    (comp-records (:records reel) (:pointer reel) (on-recall reel updater))
    (=< 8 nil)
    (div
     {}
     (div
      {}
      (div
       {:style ui/clickable-text,
        :on {:click (let [new-store (play-records
                                     (:initial-store reel)
                                     (:records reel)
                                     updater)]
               (on-run new-store))}}
       (<> "Run"))
      (div
       {:style ui/clickable-text,
        :on {:click (let [new-store (play-records
                                     (:initial-store reel)
                                     (:records reel)
                                     updater)]
               (on-merge new-store))}}
       (<> "Merge"))
      (div {:style ui/clickable-text, :event {:click on-reset}} (<> "Reset"))
      (if (not (:stopped? reel))
        (div {:style ui/clickable-text, :event {:click on-toggle}} (<> "Close"))))
     (div {:style ui/row} (<> (:store reel)))))
   (div {:style (merge style-link (style-size server?)), :on {:click on-toggle}})))
