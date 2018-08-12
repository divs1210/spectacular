(ns spectacular.core-test
  (:require [clojure.test :refer :all]
            [spectacular.core :refer :all]
            [clojure.string :as str]))

(deftest with-check-asserts-test
  (let [orig (check-asserts?)]
    (try
      (testing "with initial value: false"
        (check-asserts false)
        (is (thrown? Exception
                     (with-check-asserts true
                       (spec-assert integer? "1"))))
        (is (false? (check-asserts?))))

      (testing "with initial value: true"
        (check-asserts true)
        (is (= "1"
               (with-check-asserts false
                 (spec-assert integer? "1"))))
        (is (true? (check-asserts?))))
      (finally
        (check-asserts orig)))))


(deftest with-spec-in-test
  (with-check-asserts true
    (let [a "hi"
          b 101]
      (is (= "hi101"
             (with-spec-in {a string?
                            b integer?}
               (str a b))))  
      (is (thrown? Exception
                   (with-spec-in {b string?}
                     (str/reverse b)))))))


(deftest with-spec-out-test
  (with-check-asserts true
    (is (= "12"
           (with-spec-out string?
             (str 12))))
    (is (thrown? Exception
                 (with-spec-out string?
                   12)))))


(deftest with-spec-test
  (with-check-asserts true
    (let [a 100]
      (is (= "100"
             (with-spec {a integer?}
               string?
               (str a))))
      (is (thrown? Exception
                   (with-spec {a integer?}
                     string?
                     (inc a))))
      (is (thrown? Exception
                   (with-spec {a string?}
                     string?
                     (str/reverse a)))))))
