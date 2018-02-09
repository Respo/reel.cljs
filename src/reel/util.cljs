
(ns reel.util (:require [clojure.string :as string]))

(defn listen-devtools! [keyboard dispatch!]
  (.addEventListener
   js/window
   "keydown"
   (fn [event]
     (if (and (.-shiftKey event)
              (.-metaKey event)
              (= (.charCodeAt (string/upper-case keyboard)) (.-keyCode event)))
       (dispatch! :reel/toggle nil)))))
