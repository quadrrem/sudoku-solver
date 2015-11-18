(ns ^:figwheel-always sudoku-solver.sync
    (:require-macros [cljs.core.async.macros :refer [go go-loop]])
    (:require [cljs.core.async :refer [chan put! <!]]
              [sudoku-solver.rules :as rules]))

(def sudoku-channel (chan 10))

(defn solve-sudoku [board]
  (go
    (put!
      sudoku-channel
      (rules/solve board))))

(defn receive-sudoku [state]
  (go-loop []
    (let [board (<! sudoku-channel)]
      (state board
             (if (rules/is-solved? board)
                :solved
                :failed))
      (recur))))
