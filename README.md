# spectacular

spec / instrument arbitrary Clojure expressions!

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

`[spectacular "0.1.2"]`

```clojure
(require '[spectacular.core :as s])
```

Specs will be checked **iff** `(s/check-asserts?)` evaluates to `true`, which it doesn't by default.

This can be achieved in two ways:
* `(s/check-asserts true)` enables all spec asserts globally
* `s/with-check-asserts` sets spec-checking on/off for its body:

```clojure
(s/with-check-asserts true
  (let [a 100
        b "2"]
    (s/with-spec {a integer?
                  b string?}
      string?
      (str a b))))
```

We can just check existing bindings:

```clojure
(s/with-check-asserts true
  (let [a 100
        b "2"]
    (s/with-spec-in {a integer?
                     b string?}
      (str a b))))
```

or just the returned value:

```clojure
(s/with-check-asserts true
  (let [a 100
        b "2"]
    (s/with-spec-out string?
      (str a b))))
```

or even entire functions:

```clojure
(require '[clojure.spec.alpha :as sa])

(let [palindrome? (fn [s]
                    (s/with-spec {s (sa/and string? not-empty)}
                      boolean?
                      (= (seq s) (reverse s))))]
  (s/with-check-asserts true
    (palindrome? "pop")))
```

## Note

Get better error messages with [expound](https://github.com/bhb/expound).

```clojure
(require [clojure.spec.alpha :as s])
(require [expound.alpha :as e])

(set! s/*explain-out* e/printer)
```

## License

Copyright © 2018 Divyansh Prakash

Distributed under the Eclipse Public License either version 1.0.
