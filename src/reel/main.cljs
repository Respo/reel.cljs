
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.util :refer [id!]]
            [reel.core :refer [reel-updater *code handle-reload! listen-devtools!]]
            [reel.schema :as schema]
            [reel.updater :refer [updater]]))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defonce *reel
  (atom (-> schema/reel (assoc :base schema/store) (assoc :store schema/store))))

(defn dispatch! [op op-data]
  (println "Dispatch!" op op-data)
  (let [op-id (id!), new-reel (reel-updater updater @*reel op op-data op-id)]
    (comment println "Reel:" new-reel)
    (reset! *reel new-reel)))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer] (renderer mount-target (comp-container @*reel) dispatch!))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch *reel :changes (fn [] (render-app! render!)))
  (reset! *code {:updater updater, :view comp-container, :base schema/store})
  (listen-devtools! "a" dispatch!)
  (println "App started!"))

(defn reload! []
  (handle-reload! schema/store updater comp-container *reel clear-cache!)
  (render-app! render!)
  (println "code update."))

(set! (.-onload js/window) main!)
