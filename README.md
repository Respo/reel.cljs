
Reel: state management library for Respo
----

> as a time traveling debugger. This is exprimental technology.

Built as [actions-in-recorder](https://github.com/mvc-works/actions-in-recorder).

Demo http://repo.respo.site/reel/

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reel.svg)](https://clojars.org/respo/reel)

```edn
[respo/reel "0.2.2"]
```

Browse [src/reel/main.cljs](https://github.com/Respo/reel/blob/master/src/reel/main.cljs) to see how to use it.

Functions you need from namespaces:

```clojure
[reel.util :refer [id!]]
[reel.core :refer [reel-updater listen-devtools! refresh-reel]]
[reel.schema :as reel-schema]
```

Notice that `store` now lives inside `reel` map.

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
(add-watch *reel :changes (fn [] (render-app! render!)))
```

Call `handle-reload!` with so many arguments to reload store and element caches:

```clojure
(defn reload! []
  (clear-cache!)
  (reset! *reel (refresh-reel @*reel schema/store updater)))
```

To use records panel, please refer to `comp-reel`:

```clojure
(comp-reel reel styles)
```

Listening to `Command Shift a` to launch DevTools:

```clojure
(listen-devtools! "a" dispatch!)
```

### Develop

Workflow https://github.com/mvc-works/coworkflow

### License

MIT
