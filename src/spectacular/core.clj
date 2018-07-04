(ns spectacular.core
  "Spec arbitrary Clojure expressions!"
  (:require [clojure.spec.alpha :as s]))

(defmacro with-check-assert
  "Puts body in a block where
  `s/check-asserts` is `true`."
  [& body]
  `(with-bindings {s/check-asserts true}
     ~@body))

(defmacro with-spec-in*
  "Takes a vector of bindings from
  symbols to specs,
  inserting respective spec-asserts
  before the body.

  NOTE: Asserts work only when
  `s/check-asserts` is `true`."
  [args-n-specs & body]
  `(do
     ~@(for [[arg spec] (partition 2 args-n-specs)]
         `(s/assert ~spec ~arg))
     ~@body))


(defmacro with-spec-out*
  "Inserts a spec-assert on
  the value resulting from the body.

  NOTE: Asserts work only when
  `s/check-asserts` is `true`."
  [spec & body]
  `(s/assert ~spec ~@body))


(defmacro with-spec*
  "Composite of `with-spec-in*`
  and `with-spec-out*`.

  NOTE: Asserts work only when
  `s/check-asserts` is `true`."
  [args-n-specs ret-spec & body]
  `(with-spec-in ~args-n-specs
           (with-spec-out ~ret-spec
                      ~@body)))
