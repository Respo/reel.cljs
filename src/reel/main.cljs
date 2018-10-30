
(ns reel.main
  (:require [respo.core :refer [render! clear-cache! realize-ssr!]]
            [reel.comp.container :refer [comp-container]]
            [cljs.reader :refer [read-string]]
            [reel.core :refer [reel-updater refresh-reel]]
            [reel.util :refer [listen-devtools!]]
            [reel.schema :as schema]
            [reel.updater :refer [updater]]))

(defonce *reel
  (atom
   (-> schema/reel
       (assoc :base schema/store)
       (assoc :store schema/store)
       (assoc :display? false))))

(defn dispatch! [op op-data]
  (println "Dispatch!" op op-data)
  (let [new-reel (reel-updater updater @*reel op op-data)]
    (comment println "Reel:" new-reel)
    (reset! *reel new-reel)))

(def mount-target (.querySelector js/document ".app"))

(defn render-app! [renderer] (renderer mount-target (comp-container @*reel) dispatch!))

(def ssr? (some? (.querySelector js/document "meta.respo-ssr")))

(defn main! []
  (if ssr? (render-app! realize-ssr!))
  (render-app! render!)
  (add-watch *reel :changes (fn [] (render-app! render!)))
  (listen-devtools! "a" dispatch!)
  (println "App started!"))

(defn reload! []
  (clear-cache!)
  (reset! *reel (refresh-reel @*reel schema/store updater))
  (println "code update."))
