
(ns reel.comp.reel
  (:require-macros [respo.macros :refer [defcomp <> div button span]])
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

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(defn on-recall [reel updater]
  (fn [idx] (fn [e dispatch!] (dispatch! :reel/recall [idx (replay-store reel updater idx)]))))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defcomp
 comp-reel
 (reel updater style-reel)
 (if (:display? reel)
   (div
    {:style (merge
             ui/flex
             ui/row
             ui/fullscreen
             {:width "80%",
              :left :auto,
              :right 0,
              :background-color (hsl 0 0 100 0.9),
              :border (str "1px solid " (hsl 0 0 90))}
             style-reel)}
    (comp-records (:records reel) (:pointer reel) (on-recall reel updater))
    (=< 8 nil)
    (div
     {}
     (div
      {}
      (div
       {:style ui/clickable-text,
        :on {:click (let [new-store (play-records (:base reel) (:records reel) updater)]
               (on-run new-store))}}
       (<> "Run"))
      (div
       {:style ui/clickable-text,
        :on {:click (let [new-store (play-records (:base reel) (:records reel) updater)]
               (on-merge new-store))}}
       (<> "Merge"))
      (div {:style ui/clickable-text, :event {:click on-reset}} (<> "Reset"))
      (if (not (:stopped? reel))
        (div {:style ui/clickable-text, :event {:click on-toggle}} (<> "Close"))))
     (div {:style ui/row} (<> (:store reel)))))
   (span {})))
