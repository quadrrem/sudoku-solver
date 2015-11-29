(ns ^:figwheel-always sudoku-solver.rules)

(defn check-row [board v y x dy dx]
  (not-any? true?
          (for [i (range (count board))]
            (= (get-in board [(+ (* dy i) (* dx y))
                              (+ (* dx i) (* dy x))])
               v))))

(defn check-row-horizontal [board v y x]
  (check-row board v y x 0 1))

(defn check-row-vertical [board v y x]
  (check-row board v y x 1 0))

(defn check-square [board v y x]
  (let [r (.sqrt js/Math (count board))
        qy (quot y r)
        qx (quot x r)]
    (not-any? true?
            (for [ny (range (* qy r) (* (+ qy 1) r))
                  nx (range (* qx r) (* (+ qx 1) r))]
              (= (get-in board [ny nx])
                 v)))))

(defn is-allowed? [board v y x]
  (and (check-square board v y x)
       (check-row-horizontal board v y x)
       (check-row-vertical board v y x)))

(defn get-allowed-positions [board v y x dy dx]
  (filter
    #(not (nil? %))
    (for [i (range (count board))]
      (let [ny (+ (* dy i) (* dx y))
            nx (+ (* dx i) (* dy x))]
          (when (and (= 0 (get-in board [ny nx]))
                     (is-allowed? board v ny nx))
            [ny nx])))))

(defn get-allowed-positions-horizontal [board v y x]
  (get-allowed-positions board v y x 0 1))

(defn get-allowed-positions-vertical [board v y x]
  (get-allowed-positions board v y x 1 0))

(defn get-allowed-positions-square [board v y x]
  (filter
    #(not (nil? %))
    (let [r (.sqrt js/Math (count board))
          qy (quot y r)
          qx (quot x r)]
        (for [ny (range (* qy r) (* (+ qy 1) r))
              nx (range (* qx r) (* (+ qx 1) r))]
            (when (and (= 0 (get-in board [ny nx]))
                       (is-allowed? board v ny nx))
                 [ny nx])))))

(defn get-allowed-values [board y x]
  (filter #(is-allowed? board % y x) (range 1 (+ (count board) 1))))

(defn only-possible-position? [board v y x]
  (some #(and (= (count %) 1)
              (= (first %) [y x]))
    (vector (get-allowed-positions-square board v y x)
            (get-allowed-positions-vertical board v y x)
            (get-allowed-positions-horizontal board v y x))))

(defn get-only-possible-value [board vs y x]
  (let [v (filter
            #(only-possible-position? board % y x)
            vs)]
    (if (seq v)
        (first v)
        0)))

(defn reduce-row [board y row]
  (reduce-kv
    (fn [r x v]
      (if (= 0 v)
        (let [vs (get-allowed-values board y x)]
          (if (= (count vs) 1)
            (conj r (first vs))
            (conj r (get-only-possible-value board vs y x))))
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
