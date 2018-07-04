(ns spectacular.core
  "Spec arbitrary Clojure expressions!"
  (:require [clojure.spec.alpha :as s]))

;; Internals
;; =========
(defmacro with-spec-in*
  "Use `with-spec-in` instead."
  [sym-spec-map & body]
  `(do
     ~@(for [[arg spec] sym-spec-map]
         `(s/assert ~spec ~arg))
     ~@body))

(s/def ::sym-spec-map
  (s/map-of symbol? any?))


;; API
;; ===
(defmacro with-check-asserts
  "Puts body in a block in which
  (s/check-asserts?) is `true`."
  [& body]
  `(with-bindings {s/check-asserts true}
     ~@body))


(defmacro with-spec-in
  "Takes a map of bindings from
  symbols to specs,
  inserting respective spec-asserts
  before the body.

  NOTE: Asserts work only when
  `(s/check-asserts?)` is `true`.

  Check out `with-check-asserts`."
  [sym-spec-map & body]
  (with-check-asserts
    (with-spec-in* {sym-spec-map ::sym-spec-map}
      `(with-spec-in* sym-spec-map
         ~@body))))


(defmacro with-spec-out
  "Inserts a spec-assert on the value
  resulting from the evaluation of body.

  NOTE: Asserts work only when
  `(s/check-asserts?)` is `true`.

  Check out `with-check-asserts`."
  [spec & body]
  `(s/assert ~spec ~@body))


(defmacro with-spec
  "Composite of `with-spec-in`
  and `with-spec-out`.

  NOTE: Asserts work only when
  `(s/check-asserts?)` is `true`.

  Check out `with-check-asserts`."
  [sym-spec-map ret-spec & body]
  `(with-spec-in ~sym-spec-map
     (with-spec-out ~ret-spec
       ~@body)))
