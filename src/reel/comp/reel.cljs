
(ns reel.comp.reel
  (:require [respo.alias :refer [create-comp div button]]
            [hsl.core :refer [hsl]]
            [respo.comp.debug :refer [comp-debug]]
            [respo-ui.style :as ui]
            [respo-ui.style.colors :as colors]
            [respo.comp.text :refer [comp-text]]
            [respo.comp.space :refer [comp-space]]
            [reel.comp.records :refer [comp-records]]))

(defn on-run [new-store] (fn [e dispatch!] (dispatch! :reel/run new-store)))

(defn on-merge [new-store] (fn [e dispatch!] (dispatch! :reel/merge new-store)))

(def style-panel
  {:position :fixed,
   :right 0,
   :bottom 0,
   :transition-duration "400ms",
   :background-color colors/paper,
   :opacity 0.8})

(defn play-records [store records updater]
  (if (empty? records)
    store
    (let [[op op-data op-id] (first records), next-store (updater store op op-data op-id)]
      (recur next-store (rest records) updater))))

(defn style-size [server?]
  {:width (if server? 600 (.-innerWidth js/window)),
   :height (if server? 600 (.-innerHeight js/window))})

(defn on-toggle [e dispatch!] (dispatch! :reel/toggle nil))

(def style-link
  (merge style-panel {:transform "scale(0.2)", :transform-origin "100% 100%"}))

(defn replay-store [reel updater idx]
  (let [records-slice (subvec (:records reel) 0 idx)]
    (play-records (:initial-store reel) records-slice updater)))

(defn on-recall [reel updater]
  (fn [idx] (fn [e dispatch!] (dispatch! :reel/recall [idx (replay-store reel updater idx)]))))

(defn on-reset [e dispatch!] (dispatch! :reel/reset nil))

(defn render [reel updater server?]
  (fn [state mutate!]
    (if (:display? reel)
      (div
       {:style (merge ui/row style-panel (style-size server?))}
       (comp-records (:records reel) (:pointer reel) (on-recall reel updater))
       (comp-space 8 nil)
       (div
        {}
        (div
         {}
         (div
          {:style ui/clickable-text,
           :event {:click (let [new-store (play-records
                                           (:initial-store reel)
                                           (:records reel)
                                           updater)]
                     (on-run new-store))}}
          (comp-text "Run" nil))
         (div
          {:style ui/clickable-text,
           :event {:click (let [new-store (play-records
                                           (:initial-store reel)
                                           (:records reel)
                                           updater)]
                     (on-merge new-store))}}
          (comp-text "Merge" nil))
         (div {:style ui/clickable-text, :event {:click on-reset}} (comp-text "Reset" nil))
         (if (not (:stopped? reel))
           (div {:style ui/clickable-text, :event {:click on-toggle}} (comp-text "Close" nil))))
        (div {:style ui/row} (comp-text (:store reel) nil))))
      (div {:style (merge style-link (style-size server?)), :event {:click on-toggle}}))))

(def comp-reel (create-comp :reel render))
