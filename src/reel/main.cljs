
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.util :refer [id!]]
            [reel.core :refer [reel-updater]]
            [reel.schema :as schema]
            [reel.updater :refer [updater]]))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defonce *reel
  (atom
   (-> schema/reel
       (assoc :initial-store {:states {}, :tasks (list)})
       (assoc :store {:states {}, :tasks (list)}))))

(defn dispatch! [op op-data]
  (println "Dispatch!" op op-data)
  (let [op-id (id!), new-reel (reel-updater updater @*reel op op-data op-id)]
    (comment println "Reel:" new-reel)
    (reset! *reel new-reel)))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer server?]
  (renderer mount-target (comp-container @*reel updater server?) dispatch!))

(defn main! []
  (if ssr? (render-app! realize-ssr! true))
  (render-app! render! false)
  (add-watch *reel :changes (fn [] (render-app! render! false)))
  (println "App started!"))

(defonce *previous-view (atom comp-container))

(defonce *previous-updater (atom updater))

(defn reload! []
  (clear-cache!)
  (render-app! render! false)
  (println "Comparing updater:" (identical? updater @*previous-updater))
  (println "Comparing view:" (identical? comp-container @*previous-view))
  (reset! *previous-updater updater)
  (reset! *previous-view comp-container)
  (println "code update."))

(set! (.-onload js/window) main!)
