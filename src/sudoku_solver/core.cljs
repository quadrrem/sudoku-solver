(ns ^:figwheel-always sudoku-solver.core
  (:require [reagent.core :as reagent :refer [atom flush]]
            [sudoku-solver.sync :as sync]
            [sudoku-solver.rules :as r]))

(enable-console-print!)

(def sudoku-size 9)

(defn new-board [n]
  (vec (repeat n (vec (repeat n 0)))))

(def example-board
   [[1 0 0 0 0 9 2 0 4]
    [0 0 0 0 1 7 6 0 3]
    [7 6 0 0 2 0 0 8 5]
    [0 0 0 0 0 0 0 1 8]
    [0 0 7 0 4 0 3 0 0]
    [6 2 0 0 0 0 0 0 0]
    [3 7 0 0 9 0 0 6 1]
    [9 0 2 4 5 0 0 0 0]
    [5 0 6 1 0 0 0 0 0]])

(defonce app-state (atom {:text "Sudoku Solver!"
                          :progress :new
                          :board (new-board sudoku-size)}))

(defn change-value [y x]
  (when (= (:progress @app-state) :new)
    (let [board (:board @app-state)
          v (get-in board [y x])
          vs (filter #(> % v) (r/get-allowed-values board y x))]
      (if (seq vs)
       (swap! app-state assoc-in [:board y x] (first vs))
       (swap! app-state assoc-in [:board y x] 0)))))

(defn blank [y x]
  [:rect {:width 0.9
          :height 0.9
          :x x
          :y y
          :fill "grey"
          :on-click (fn blank-clik []
                      (change-value y x))}])

(defn number [v y x]
  [:text {:x (+ 0.3 x)
          :y (+ 0.7 y)
          :font-size 0.6
          :on-click (fn number-clik []
                      (change-value y x))}
    v])

(defn create-tiles [c]
  (reduce #(assoc %1 %2 (partial number %2)) (hash-map) (range 1 (+ c 1))))

(def tiles (create-tiles sudoku-size))

(defn swap-board
  ([board] (swap-board board :new))
  ([board progress]
   (swap! app-state assoc :progress progress)
   (swap! app-state assoc :board board)))

(defn sudoku []
  [:center
    [:h1 (str (:text @app-state) " "
              (case (:progress @app-state)
                  :solving "Solving..."
                  :solved "Solved!"
                  :failed "Failed!"
                  ""))]
    [:p
      [:button {:on-click
                    (fn new-game-click [e]
                      (swap-board (new-board sudoku-size)))}
          "New Game.."]
      [:button {:on-click
                    (fn solve-click [e]
                      (swap! app-state assoc :progress :solving)
                      (sync/solve-sudoku (:board @app-state) swap-board))}
          "Solve!"]
      [:button {:on-click
                    (fn example-click [e]
                      (swap! app-state assoc :progress :new)
                      (swap-board example-board))}
          "Example"]]
    (into
      [:svg
        {:view-box (str "0 0 " sudoku-size " " sudoku-size)
         :width 500
         :height 500}]
      (for [y (range sudoku-size)
            x (range sudoku-size)]
          (let [v (get-in @app-state [:board y x])]
            (if (= v 0)
              [blank y x]
              [(get tiles v) y x]))))])

(reagent/render-component [sudoku]
                          (. js/document (getElementById "app")))


(defn on-js-reload [])
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
