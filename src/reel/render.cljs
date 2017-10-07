
(ns reel.render
  (:require [respo.render.html :refer [make-string]]
            [shell-page.core :refer [make-page spit slurp]]
            [reel.comp.container :refer [comp-container]]
            [reel.reel :refer [reel-schema]]))

(def base-info
  {:title "Reel", :icon "http://repo-cdn.b0.upaiyun.com/logo/respo.png", :ssr nil})

(defn dev-page []
  (make-page
   ""
   (merge
    base-info
    {:styles ["http://192.168.99.228:8100/main.css"],
     :scripts ["/main.js" "/browser/lib.js" "/browser/main.js"]})))

(def preview? (= "preview" js/process.env.prod))

(defn prod-page []
  (let [html-content (make-string (comp-container (:initial-store reel-schema)))
        manifest (.parse js/JSON (slurp "dist/assets-manifest.json"))
        cljs-manifest (.parse js/JSON (slurp "dist/manifest.json"))
        cdn (if preview? "" "http://repo-cdn.b0.upaiyun.com/smallist/")
        prefix-cdn (fn [x] (str cdn x))]
    (make-page
     html-content
     (merge
      base-info
      {:styles [(prefix-cdn (aget manifest "main.css"))],
       :scripts (map
                 prefix-cdn
                 [(aget manifest "main.js")
                  (-> cljs-manifest (aget 0) (aget "js-name"))
                  (-> cljs-manifest (aget 1) (aget "js-name"))]),
       :ssr "respo-ssr"}))))

(defn main! []
  (if (= js/process.env.env "dev")
    (spit "target/index.html" (dev-page))
    (spit "dist/index.html" (prod-page))))
