(ns spectacular.core
  "Spec arbitrary Clojure expressions!"
  (:require [clojure.spec.alpha :as s]))

(defonce ^:dynamic *check-spec-asserts*
  false)

(defn check-asserts? []
  *check-spec-asserts*)

(defn check-asserts [flag]
  (alter-var-root #'*check-spec-asserts*
                  (constantly flag)))

(defmacro spec-assert [spec x]
  `(let [x# ~x]
     (when *check-spec-asserts*
       (s/assert* ~spec x#))
     x#))

(defmacro with-check-asserts
  "Puts body in a block inside which
    (`s/check-asserts?`)
  returns flag."
  [flag & body]
  (assert (boolean? flag)
          "flag must be boolean!")
  (assert (not-empty body)
          "body cannot be empty!")
  `(binding [*check-spec-asserts* ~flag]
     ~@body))


(s/def ::sym-spec-map
  (s/map-of symbol? any?))

(defmacro with-spec-in
  "Takes a map of bindings from symbols to specs,
  inserting respective spec-asserts before the body.

  NOTE: Asserts work only when
    (`s/check-asserts?`)
  is true. See `with-check-asserts`."
  [sym-spec-map & body]
  (with-check-asserts true
    (spec-assert ::sym-spec-map sym-spec-map)
    `(do
       ~@(for [[sym spec] sym-spec-map]
           `(spec-assert ~spec ~sym))
       ~@body)))

(defmacro with-spec-out
  "Inserts a spec-assert on the value
  resulting from the evaluation of body.

  NOTE: Asserts work only when
    (`s/check-asserts?`)
  is true. See `with-check-asserts`."
  [spec & body]
  (with-check-asserts true
    (spec-assert not-empty body)
    `(let [res# ~(cons `do body)]
       (spec-assert ~spec res#))))

(defmacro with-spec
  "Composite of `with-spec-in` and `with-spec-out`.

  NOTE: Asserts work only when
    (`s/check-asserts?`)
  is true. See `with-check-asserts`."
  [sym-spec-map ret-spec & body]
  `(with-spec-in ~sym-spec-map
     (with-spec-out ~ret-spec
       ~@body)))
