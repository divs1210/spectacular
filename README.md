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

`[spectacular "0.1.0"]`

```clojure
(require '[clojure.spec.alpha :as s])
```

Specs will be checked **iff** `(s/check-asserts?)` evaluates to `true`.

This can be achieved in two ways:
* `(s/check-asserts true)` enables all spec asserts globally
* `with-check-asserts` sets spec-checking on/off for its body:

```clojure
(require '[spectacular.core :refer [with-check-asserts]])

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

or even entire functions:

```clojure
(let [palindrome? (fn [s]
                    (with-spec {s (s/and string? not-empty)}
                      boolean?
                      (= (seq s) (reverse s))))]
  (with-check-asserts true
    (palindrome? "pop")))
```

## Note

Get better error messages with [expound](https://github.com/bhb/expound).

Add this to `src/user.clj`:

```clojure
(ns user
  (:require [clojure.spec.alpha :as s]
            [expound.alpha :as e]))

(set! s/*explain-out* e/printer)
```

## License

Copyright © 2018 Divyansh Prakash

Distributed under the Eclipse Public License either version 1.0.
