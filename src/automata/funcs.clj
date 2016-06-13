(ns automata.funcs
  (:require [quil.core :as q]))

(defn height []
  512)
(defn width []
  512)

(defn h
  ([] (h 1.0))
  ([percentage] (* (q/height) percentage)))

(defn w
  ([] (w 1.0))
  ([percentage] (* (q/width) percentage)))

(defn make-randomizer [min max]
  (let [nums (range min max)]
    #(rand-nth nums)))

(defn distance [p1 p2]
  ; return pythagorean distance between two points
  (q/sqrt
      (+
        (q/sq (- (:x p2) (:x p1)))
        (q/sq (- (:y p2) (:y p1))))))

(defn make-osc-cheating [start min max step]
  ; return a function that oscillates inputs
  (def cur-store (atom start))
  (def dir-store (atom 1))
  (fn []
    (let [cur @cur-store
          dir @dir-store
          next (+ cur (* dir step))]
      (if (> next max)
        [(reset! cur-store (- max step)) (reset! dir-store (* -1 dir))]
        (if (< next min)
          [(reset! cur-store (+ min step)) (reset! dir-store (* -1 dir))]
          [(reset! cur-store next) dir])))))

(defn make-osc [start min max step]
  ; return a function that oscillates inputs
  (fn [cur dir]
    (let [next (+ cur (* dir step))]
      (if (> next max)
        [(- max step) (* -1 dir)]
        (if (< next min)
          [(+ min step) (* -1 dir)]
          [next dir])))))
