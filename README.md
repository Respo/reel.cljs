
Reel: state management library for Respo(WIP...)
----

> as a time traveling debugger.

Built as [actions-in-recorder](https://github.com/mvc-works/actions-in-recorder).

Demo http://repo.respo.site/reel/

### Usage

> WIP... latest code not released yet, docs not ready.

[![Clojars Project](https://img.shields.io/clojars/v/respo/reel.svg)](https://clojars.org/respo/reel)

```edn
[respo/reel "0.1.0"]
```

Browse [src/reel/main.cljs](https://github.com/Respo/reel/blob/master/src/reel/main.cljs) to see how to use it.

> WIP... guide is outdated...

Functions you need from namespaces:

```clojure
[reel.core :refer [reel-schema reel-updater]]
[reel.updater :refer [updater]
[reel.comp.reel :refer [comp-reel]]
```

Notice that `store` now lives as part of `reel` HashMap:

```clojure
(def reel-schema
  {:initial nil,
   :store nil, ; <---- store here, you have to add initial-store
   :records [],
   :pointer nil,
   :stopped? false,
   :display? false})
```

Instead of `*store`, you need `*reel` now.
In a todolist, the initial store is `(list)`:

```clojure
(def store {:states {} :tasks (list)})

(defonce *reel
  (atom (-> reel-schema
            (assoc :initial store)
            (assoc :store store))))
```

And we need a `reel-updater` besides the familiar `updater` we used in Respo:

```clojure
(defn dispatch! [op op-data]
  (let [op-id (id!),
        new-reel (reel-updater updater @*reel op op-data op-id)]
    (reset! *reel new-reel)))
```

If you are going to use the debugger, add `updater` as a parameter:

```clojure
(comp-container @*reel updater)
```

To use the debugger as a component:

```clojure
(comp-reel reel updater false) ; false for server?
```

### Develop

Workflow https://github.com/mvc-works/coworkflow

### License

MIT
