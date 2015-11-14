(ns ^:figwheel-always sudoku-solver.rules)

(defn check-row [board v x y dx dy]
  (not-any? true?
          (for [i (range (count board))]
            (= (get-in board [(+ (* dy i) y)
                              (+ (* dx i) x)])
               v))))

(defn check-row-horizontal [board v x y]
  (check-row board v x y 1 0))

(defn check-row-vertical [board v x y]
  (check-row board v x y 0 1))

(defn check-square [board v x y]
  (let [r (.sqrt js/Math (count board))
        qx (quot x r)
        qy (quot y r)]
    (not-any? true?
            (for [i (range (* qx r) (* (+ qx 1) r))
                  j (range (* qy r) (* (+ qy 1) r))]
              (= (get-in board [j i])
                 v)))))

(defn is-allowed? [board v x y]
  (and (check-square board v x y)
       (check-row-horizontal board v x y)
       (check-row-vertical board v x y)))

(defn get-allowed-values [board x y]
  (filter #(is-allowed? board % x y) (range 1 (+ (count board) 1))))

(defn is-solved? [board]
  (every? true?
          (for [y (range (count board))
                x (range (count board))]
            (> (get-in board [y x]) 0))))

(defn reduce-row [board y row]
  (reduce-kv
    (fn [r x v]
      (if (= 0 v)
        (let [vs (get-allowed-values board x y)]
          (if (= (count vs) 1)
            (conj r (first vs))
            (conj r 0)))
        (conj r v)))
   []
   row))

(defn reduce-board [board]
  (reduce-kv
    (fn [c i v]
      (conj c (reduce-row board i v)))
    []
    board))

(defn solve [board]
  (if (is-solved? board)
    (do (println "Solved" board) board)
    (let [nb (reduce-board board)]
      (println "Solve" board)
      (if (= nb board)
        (do (println "NOT  " board) board)
        (solve nb)))))
