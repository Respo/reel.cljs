
(set-env!
  :resource-paths #{"src"}
  :dependencies '[])

(def +version+ "0.2.0-alpha4")

(deftask build []
  (comp
    (pom :project     'respo/reel
         :version     +version+
         :description "Time travel demo for Respo"
         :url         "https://github.com/Respo/reel"
         :scm         {:url "https://github.com/Respo/reel"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (install)
    (target)))

(deftask deploy []
  (set-env!
    :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"}]))
  (comp
    (build)
    (push :repo "clojars" :gpg-sign (not (.endsWith +version+ "-SNAPSHOT")))))
