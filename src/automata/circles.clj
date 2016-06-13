(ns automata.circles
  (:require [quil.core :as q]
            [tsl.funcs :as f]
            [quil.middleware :as m]))

(def WIDTH 512)
(def HEIGHT WIDTH)

(def random-pos
  (f/make-randomizer 0 WIDTH))

(def random-size
  (f/make-randomizer 1 150))

(def rc
  (f/make-randomizer 0 255))

(defn make-circle []
  (let [size (random-size)]
    {:x (random-pos) :y (random-pos) :size size :color [(rc) 0 (rc)]}))

(defn setup []
  (q/frame-rate 1)
  {:circles [(make-circle)]})

(defn is-not-overlapping [c1 c2]
  (let [sum-radii (/ (+ (:size c1) (:size c2) 2) 2)
        distance (f/distance c1 c2)]
      (< sum-radii distance)))

(defn make-if-not-overlapping [circles]
  (let [new-circle (make-circle)]
    (if (every?
          (partial is-not-overlapping new-circle) circles)
      (conj circles new-circle)
      circles)))

(defn mifn [circles _]
  (let [new-circle (make-circle)]
    (if (every?
          (partial is-not-overlapping new-circle) circles)
      (conj circles new-circle)
      circles)))

(defn update-state [{:keys [circles]}]
  `(let [next-circs circles])
  (let [next-circs (reduce mifn circles (range 10))]
    {:circles next-circs}))

(defn draw-circle [{:keys [x y size color]}]
  (apply q/fill color)
  (q/ellipse x y size size))

(defn draw-state [{:keys [circles]}]
  (q/no-stroke)
  (q/background 0)
  (q/fill 0 0 0 0)
  (doseq [circle circles]
    (draw-circle circle))
  (if (q/key-pressed?)
      (let []
        (q/fill 0 255 0)
        (q/text (str (count circles)) 10 10)
        (q/text (str (q/key-code)) 10 30))))

(q/defsketch cpack
    :title "circle packing"
    :size [WIDTH HEIGHT]
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    :features [:keep-on-top :fullscreen]
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
    :renderer :p2d
    :middleware [m/fun-mode])
