(ns ^:figwheel-always sudoku-solver.rules)

(defn check-row [board v x y z dx dy]
  (not-any? true?
          (for [i (range z)]
            (= (get-in board [(+ (* dy i) y)
                              (+ (* dx i) x)])
               v))))

(defn check-row-horizontal [board v x y z]
  (check-row board v x y z 1 0))

(defn check-row-vertical [board v x y z]
  (check-row board v x y z 0 1))

(defn check-square [board v x y z]
  (let [r (.sqrt js/Math z)
        qx (quot x r)
        qy (quot y r)]
    (not-any? true?
            (for [i (range (* qx r) (* (+ qx 1) r))
                  j (range (* qy r) (* (+ qy 1) r))]
              (= (get-in board [i j])
                 v)))))
