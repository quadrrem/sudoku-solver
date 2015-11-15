(ns ^:figwheel-always sudoku-solver.rules)

(defn check-row [board v x y dx dy]
  (not-any? true?
          (for [i (range (count board))]
            (= (get-in board [(+ (* dy i) (* dx y))
                              (+ (* dx i) (* dy x))])
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

(defn get-allowed-positions [board v x y dx dy]
  (filter
    #(not (nil? %))
    (for [i (range (count board))]
      (let [ny (+ (* dy i) (* dx y))
            nx (+ (* dx i) (* dy x))]
          (when (and (= 0 (get-in board [ny nx]))
                     (is-allowed? board v nx ny))
            [ny nx])))))

(defn get-allowed-positions-horizontal [board v x y]
  (get-allowed-positions board v x y 1 0))

(defn get-allowed-positions-vertical [board v x y]
  (get-allowed-positions board v x y 0 1))

(defn get-allowed-positions-square [board v x y]
  (filter
    #(not (nil? %))
    (let [r (.sqrt js/Math (count board))
          qx (quot x r)
          qy (quot y r)]
        (for [nx (range (* qx r) (* (+ qx 1) r))
              ny (range (* qy r) (* (+ qy 1) r))]
            (when (and (= 0 (get-in board [ny nx]))
                       (is-allowed? board v nx ny))
                 [ny nx])))))

(defn get-allowed-values [board x y]
  (filter #(is-allowed? board % x y) (range 1 (+ (count board) 1))))

(defn only-possible-position? [board v x y]
  (some #(and (= (count %) 1)
              (= (first %) [y x]))
    (vector (get-allowed-positions-square board v x y)
            (get-allowed-positions-vertical board v x y)
            (get-allowed-positions-horizontal board v x y))))

(defn get-only-possible-value [board vs x y]
  (let [v (filter
            #(only-possible-position? board % x y)
            vs)]
    ;(println y " " x " " v)
    (if (seq v)
        (first v)
        0)))

(defn reduce-row [board y row]
  (reduce-kv
    (fn [r x v]
      (if (= 0 v)
        (let [vs (get-allowed-values board x y)]
          (if (= (count vs) 1)
            (conj r (first vs))
            (conj r (get-only-possible-value board vs x y))))
        (conj r v)))
   []
   row))

(defn reduce-board [board]
  (reduce-kv
    (fn [c i v]
      (conj c (reduce-row board i v)))
    []
    board))

(defn is-solved? [board]
  (every? true?
          (for [y (range (count board))
                x (range (count board))]
            (> (get-in board [y x]) 0))))

(defn solve [board]
  (if (is-solved? board)
    board
    (let [nb (reduce-board board)]
      (if (= nb board)
        board
        (solve nb)))))
