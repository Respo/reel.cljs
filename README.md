
Reel: state management library for Respo
----

> as a time traveling debugger. This is exprimental technology.

Built as [actions-in-recorder](https://github.com/mvc-works/actions-in-recorder).

Demo http://repo.respo.site/reel/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reel.svg)](https://clojars.org/respo/reel)

```edn
[respo/reel "0.2.0-alpha"]
```

Browse [src/reel/main.cljs](https://github.com/Respo/reel/blob/master/src/reel/main.cljs) to see how to use it.

Functions you need from namespaces:

```clojure
[reel.util :refer [id!]]
[reel.core :refer [reel-updater *code handle-reload!]]
[reel.schema :as reel-schema]
```

Notice that `store` now lives as part of `reel` map.

Instead of `*store`, you need `*reel` for global states. For example:

```clojure
(def store {:states {} :tasks (list)})

(defonce *reel
  (atom (-> reel-schema/reel
            (assoc :base store)
            (assoc :store store))))
```

And we need a `reel-updater` besides the familiar `updater` we used in Respo:

```clojure
(defn dispatch! [op op-data]
  (let [op-id (id!),
        new-reel (reel-updater updater @*reel op op-data op-id)]
    (reset! *reel new-reel)))
```

Make sure you watch `*reel` and initialize `reel.core/*code` inside `main!` function:

```clojure
(add-watch *reel :changes (fn [] (render-app! render! false)))
(reset! *code {:updater updater, :view comp-container, :base schema/store})
```

Call `handle-reload!` with so many arguments to reload store and element caches:

```clojure
(defn reload! []
  (handle-reload! (:inital schema/store) updater comp-container *reel clear-cache!)
  (render-app! render! false)
  (println "code update."))
```

To use records panel, please refer to `comp-reel`. Not indent to document that yet.

### Develop

Workflow https://github.com/mvc-works/coworkflow

### License

MIT
