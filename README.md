# spectacular

Spec arbitrary Clojure expressions!

```clojure
(require '[spectacular.core :refer [with-spec]])

(let [a 100
      b "2"]
  (with-spec {a integer?
              b string?}
    string?
    (str a b)))
```


## Why?

Specs are great! Let's have more of them.

## Usage

```clojure
(require '[clojure.spec.alpha :as s])
(require '[spectacular.core :refer [with-check-asserts]])
```

Specs will be checked **iff** `(s/check-asserts?)` evaluates to `true`.

This can be achieved in two ways:
* `(s/check-asserts true)` enables all spec asserts globally
* `with-check-asserts` sets spec-checking on/off for its body:

```clojure
(with-check-asserts true
  (let [a 100
        b "2"]
    (with-spec {a integer?
                b string?}
      string?
      (str a b))))
```

We can just check existing bindings:

```clojure
(require '[spectacular.core :refer [with-spec-in]])

(with-check-asserts true
  (let [a 100
        b "2"]
    (with-spec-in {a integer?
                   b string?}
      (str a b))))
```

or just the returned value:

```clojure
(require '[spectacular.core :refer [with-spec-out]])

(with-check-asserts true
  (let [a 100
        b "2"]
    (with-spec-out string?
      (str a b))))
```

## License

Copyright © 2018 Divyansh Prakash

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
