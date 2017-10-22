
(defn read-password [guide]
  (String/valueOf (.readPassword (System/console) guide nil)))

(set-env!
  :resource-paths #{"src"}
  :dependencies '[]
  :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"
                                     :username "jiyinyiyong"
                                     :password (read-password "Clojars password: ")}]))

(def +version+ "0.2.0-alpha6")

(deftask deploy []
  (comp
    (pom :project     'respo/reel
         :version     +version+
         :description "Time travel demo for Respo"
         :url         "https://github.com/Respo/reel"
         :scm         {:url "https://github.com/Respo/reel"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (push :repo "clojars" :gpg-sign false)))
