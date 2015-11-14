(ns sudoku-solver.rules-test
  (:require-macros [cljs.test :refer (deftest is testing run-tests)])
  (:require [sudoku-solver.rules :as r]))

(def board [[1 0 0 0 0 9 2 0 4]
            [0 0 0 0 1 7 6 0 3]
            [7 6 0 0 2 0 0 8 5]
            [0 0 0 0 0 0 0 1 8]
            [0 0 7 0 4 0 3 0 0]
            [6 2 0 0 0 0 0 0 0]
            [3 7 0 0 9 0 0 6 1]
            [9 0 2 4 5 0 0 0 0]
            [5 0 6 1 0 0 0 0 0]])

(deftest test-is-allowed
  (is (r/is-allowed? board 3 1 0 9)))

(deftest test-is-allowed-not-allowed
  (is (not (r/is-allowed? board 2 1 0 9))))

(deftest test-row-horizontal
  (is (r/check-row-horizontal board 3 1 0 9)))

(deftest test-row-horizontal-not-allowed
  (is (not (r/check-row-horizontal board 2 1 0 9))))

(deftest test-row-vertical
  (is (r/check-row-vertical board 3 1 0 9)))

(deftest test-row-vertical-not-allowed
  (is (not (r/check-row-vertical board 2 1 0 9))))

(deftest test-square
  (is (r/check-square board 2 1 0 9)))

(deftest test-square-not-allowed
  (is (not (r/check-square board 7 1 0 9))))

(deftest test-square-2nd
  (is (r/check-square board 2 5 5 9)))

(deftest test-square-2nd-not-allowed
  (is (not (r/check-square board 4 5 5 9))))

(deftest test-square-3nd
  (is (r/check-square board 1 0 8 9)))

(deftest test-square-3nd-not-allowed
  (is (not (r/check-square board 4 8 0 9))))

(deftest test-get-allowed-values
  (is (= '(3 5 8) (r/get-allowed-values board 1 0 9))))

(deftest test-is-solved-not
  (is (not (r/is-solved? board))))

(deftest test-solve
  (is (= (r/solve board)
        board)))
