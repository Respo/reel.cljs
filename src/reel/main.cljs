
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! falsify-stage! render-element]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.util :refer [id!]]
            [reel.reel :refer [reel-schema reel-updater]]
            [reel.updater :refer [updater]]))

(defonce reel-ref
  (atom
   (-> reel-schema
       (assoc :initial-store {:states {}, :tasks (list)})
       (assoc :store {:states {}, :tasks (list)}))))

(defn dispatch! [op op-data]
  (println "Dispatch!" op op-data)
  (let [op-id (id!), new-reel (reel-updater updater @reel-ref op op-data op-id)]
    (comment println "Reel:" new-reel)
    (reset! reel-ref new-reel)))

(defn render-app! []
  (let [target (.querySelector js/document "#app")]
    (render! (comp-container @reel-ref updater false) target dispatch!)))

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
       (render-element (comp-container @reel-ref updater true))
       dispatch!)))
  (render-app!)
  (add-watch reel-ref :changes render-app!)
  (println "app started!"))

(defn on-jsload! [] (clear-cache!) (render-app!) (println "code update."))

(set! (.-onload js/window) -main!)
