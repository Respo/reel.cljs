
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
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

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer]
  (renderer mount-target (comp-container @reel-ref updater false) dispatch!))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch reel-ref :changes (fn [] (render-app! render!)))
  (println "App started!"))

(defn reload! [] (clear-cache!) (render-app! render!) (println "code update."))

(set! (.-onload js/window) main!)
