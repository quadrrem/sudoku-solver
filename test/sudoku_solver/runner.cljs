(ns sudoku-solver.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [sudoku-solver.rules-test]))

(doo-tests 'sudoku-solver.rules-test)
