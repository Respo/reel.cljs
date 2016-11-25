
(ns reel.main
  (:require [respo.core
             :refer
             [render! clear-cache! falsify-stage! render-element gc-states!]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.util :refer [id!]]
            [reel.reel :refer [reel-schema reel-updater]]
            [reel.updater :refer [updater]]))

(defonce reel-ref (atom (-> reel-schema (assoc :initial-store []) (assoc :store []))))

(defn dispatch! [op op-data]
  (let [op-id (id!), new-reel (reel-updater updater @reel-ref op op-data op-id)]
    (reset! reel-ref new-reel)))

(defonce states-ref (atom {}))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @reel-ref) target dispatch! states-ref)))

(def ssr-stages
  (let [ssr-element (.querySelector js/document "#ssr-stages")
        ssr-markup (.getAttribute ssr-element "content")]
    (read-string ssr-markup)))

(defn -main! []
  (enable-console-print!)
  (if (not (empty? ssr-stages))
    (let [target (.querySelector js/document "#app")]
      (falsify-stage!
       target
       (render-element (comp-container @reel-ref ssr-stages) states-ref)
       dispatch!)))
  (render-app!)
  (add-watch reel-ref :gc (fn [] (gc-states! states-ref)))
  (add-watch reel-ref :changes render-app!)
  (add-watch states-ref :changes render-app!)
  (println "app started!"))

(defn on-jsload! [] (clear-cache!) (render-app!) (println "code update."))

(set! (.-onload js/window) -main!)
