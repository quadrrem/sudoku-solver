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

(def solved-board [[1 3 5 6 8 9 2 7 4]
                   [2 4 8 5 1 7 6 9 3]
                   [7 6 9 3 2 4 1 8 5]
                   [4 9 3 2 6 5 7 1 8]
                   [8 5 7 9 4 1 3 2 6]
                   [6 2 1 7 3 8 4 5 9]
                   [3 7 4 8 9 2 5 6 1]
                   [9 1 2 4 5 6 8 3 7]
                   [5 8 6 1 7 3 9 4 2]])

(deftest test-is-allowed
  (is (r/is-allowed? board 3 1 0)))

(deftest test-is-allowed-not-allowed
  (is (not (r/is-allowed? board 2 1 0))))

(deftest test-row-horizontal
  (is (r/check-row-horizontal board 1 6 2)))

(deftest test-row-horizontal-not-allowed
  (is (not (r/check-row-horizontal board 2 1 0))))

(deftest test-row-vertical
  (is (r/check-row-vertical board 3 1 0)))

(deftest test-row-vertical-not-allowed
  (is (not (r/check-row-vertical board 2 1 0))))

(deftest test-square
  (is (r/check-square board 1 7 0)))

(deftest test-square-not-allowed
  (is (not (r/check-square board 7 1 0))))

(deftest test-square-2nd
  (is (r/check-square board 2 5 5)))

(deftest test-square-2nd-not-allowed
  (is (not (r/check-square board 4 5 5))))

(deftest test-square-3nd
  (is (r/check-square board 1 0 8)))

(deftest test-square-3nd-not-allowed
  (is (not (r/check-square board 4 8 0))))

(deftest test-get-allowed-values
  (is (= '(3 5 8) (r/get-allowed-values board 1 0))))

(deftest test-get-allowed-positions-horizontal
  (is (= '([0 1] [0 2] [0 3]) (r/get-allowed-positions-horizontal board 5 1 0))))

(deftest test-get-allowed-positions-vertical
  (is (= '([0 1] [1 1] [3 1] [4 1]) (r/get-allowed-positions-vertical board 5 1 0))))

(deftest test-get-allowed-positions-square
  (is (= '([0 1] [1 1] [0 2] [1 2]) (r/get-allowed-positions-square board 5 1 0))))

(deftest test-get-allowed-positions-horizontal-2nd
  (is (= '([2 6]) (r/get-allowed-positions-horizontal board 1 6 2))))

(deftest test-get-allowed-positions-vertical-2nd
  (is (= '([2 6] (r/get-allowed-positions-vertical board 1 6 2)))))

(deftest test-get-allowed-positions-square-2nd
  (is (= '([2 6]) (r/get-allowed-positions-square board 1 6 2))))

(deftest test-get-allowed-positions-horizontal-3nd
  (is (= '([2 2] [2 6]) (r/get-allowed-positions-horizontal board 9 6 2))))

(deftest test-get-allowed-positions-vertical-3nd
  (is (= '([2 6] [3 6] [5 6] [8 6]) (r/get-allowed-positions-vertical board 9 6 2))))

(deftest test-get-allowed-positions-square-3nd
  (is (= '([2 6] [1 7]) (r/get-allowed-positions-square board 9 6 2))))

(deftest test-only-possible-position
  (is (r/only-possible-position? board 1 6 2)))

(deftest test-only-possible-position-not
  (is (not (r/only-possible-position? board 5 0 1))))

(deftest test-is-solved?
  (is (r/is-solved? solved-board)))

(deftest test-is-solved-not
  (is (not (r/is-solved? board))))

(deftest test-solve
  (is (= (r/solve board)
        solved-board)))
