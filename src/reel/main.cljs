
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.util :refer [id!]]
            [reel.core :refer [reel-updater replay-store]]
            [reel.schema :as schema]
            [reel.updater :refer [updater]]))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defonce *reel
  (atom (-> schema/reel (assoc :initial schema/store) (assoc :store schema/store))))

(defn dispatch! [op op-data]
  (println "Dispatch!" op op-data)
  (let [op-id (id!), new-reel (reel-updater updater @*reel op op-data op-id)]
    (comment println "Reel:" new-reel)
    (reset! *reel new-reel)))

(defonce *code (atom {:updater updater, :view comp-container, :initial schema/store}))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer server?]
  (renderer mount-target (comp-container @*reel updater server?) dispatch!))

(defn main! []
  (if ssr? (render-app! realize-ssr! true))
  (render-app! render! false)
  (add-watch *reel :changes (fn [] (render-app! render! false)))
  (println "App started!"))

(defn reload! []
  (if (or (not (identical? updater (:updater @*code)))
          (not (identical? (:initial schema/store) (:initial @*code))))
    (do
     (reset! *code (merge {:updater updater, :initial (:initial schema/store)}))
     (swap! *reel assoc :store (replay-store @*reel updater (:pointer @*reel)))))
  (if (not (identical? comp-container (:view @*code)))
    (do (swap! *code assoc :view comp-container) (clear-cache!)))
  (render-app! render! false)
  (println "code update."))

(set! (.-onload js/window) main!)
