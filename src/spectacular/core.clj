(ns spectacular.core
  "Spec arbitrary Clojure expressions!"
  (:require [clojure.spec.alpha :as s]))

(s/def ::sym-spec-map
  (s/map-of symbol? any?))

(defmacro with-check-asserts
  "Puts body in a block inside which
  (`s/check-asserts?`) returns flag."
  [flag & body]
  `(let [flag# ~flag
         orig-val# (s/check-asserts?)]
     (try
       (assert (boolean? flag#)
               "flag must be boolean!")
       (s/check-asserts flag#)
       ~@body
       (finally
         (s/check-asserts orig-val#)))))


(defmacro with-spec-in
  "Takes a map of bindings from
  symbols to specs,
  inserting respective spec-asserts
  before the body.

  NOTE: Asserts work only when
  (`s/check-asserts?`) is true.

  Check out `with-check-asserts`."
  [sym-spec-map & body]
  (with-check-asserts true
    (s/assert ::sym-spec-map sym-spec-map)
    `(do
       ~@(for [[sym spec] sym-spec-map]
           `(s/assert ~spec ~sym))
       ~@body)))


(defmacro with-spec-out
  "Inserts a spec-assert on the value
  resulting from the evaluation of body.

  NOTE: Asserts work only when
  (`s/check-asserts?`) is true.

  Check out `with-check-asserts`."
  [spec & body]
  `(let [res# ~(cons `do body)]
     (s/assert ~spec res#)))


(defmacro with-spec
  "Composite of `with-spec-in`
  and `with-spec-out`.

  NOTE: Asserts work only when
  (`s/check-asserts?`) is true.

  Check out `with-check-asserts`."
  [sym-spec-map ret-spec & body]
  `(with-spec-in ~sym-spec-map
     (with-spec-out ~ret-spec
       ~@body)))
