
Reel: time travel demo in Respo
----

Store abstraction for Respo. Built as [actions-in-recorder](https://github.com/mvc-works/actions-in-recorder).

Demo http://repo.respo.site/reel/target/dev.html

### Usage

[![Clojars Project](https://img.shields.io/clojars/v/respo/reel.svg)](https://clojars.org/respo/reel)

Functions you need from namespaces:

```clojure
[reel.reel :refer [reel-schema reel-updater]]
[reel.updater :refer [updater]
[reel.comp.reel :refer [comp-reel]]
```

Notice that `store` now lives as part of `reel` HashMap:

```clojure
(def reel-schema
  {:initial-store nil,
   :store nil, ; <---- store here, you have to add initial-store
   :records [],
   :pointer 0,
   :tab :records,
   :stopped? false,
   :display? false})
```

Instead of `store-ref`, you need `reel-ref` now.
In a todolist, the initial store is `(list)`:

```clojure
(defonce reel-ref
  (atom (-> reel-schema
            (assoc :initial-store (list))
            (assoc :store (list)))))
```

And we need a `reel-updater` besides the familiar `updater` we used in Respo:

```clojure
(defn dispatch! [op op-data]
  (let [op-id (id!),
        new-reel (reel-updater updater @reel-ref op op-data op-id)]
    (reset! reel-ref new-reel)))
```

If you are going to use the debugger, add `updater` as a parameter:

```clojure
(comp-container @reel-ref updater)

To use the debugger as a component:

```clojure
(comp-reel reel updater)
```

### Develop

Workflow https://github.com/mvc-works/stack-workflow

### License

MIT
