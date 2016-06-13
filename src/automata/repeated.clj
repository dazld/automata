(ns automata.repeated
  (:require [quil.core :as q]
            [tsl.funcs :as f]
            [quil.middleware :as m]))

(def WIDTH 512)
(def HEIGHT 512)

(def num-points 6)

(def o (f/make-osc 0 0 1 0.01))

(def rc
  (f/make-randomizer 0 255))

(defn make-circle [idx]
  (let [grid 5
        col (mod idx grid)
        row (int (Math/floor (/ idx grid)))
        x (* (+ 0 col) (identity (/ 1 grid)))
        y (* (+ 0 row) (identity (/ 1 grid)))]
    ; (println idx col row x y)
    {:x x :y y :size 0.05 :color [(rc) 0 (rc)]}))

(make-circle 23)

(defn setup []
  (q/frame-rate 1)
  {:circles (map make-circle (range ( * 5 5)))})

(defn update-state [{:keys [circles]}]
  (let [next-circs (map make-circle (range ( * 5 5)))]
    {:circles next-circs}))

(defn draw-circle [{:keys [x y size color]}]
  (apply q/fill color)

  (q/ellipse (f/w x) (f/h y) (f/h size) (f/h size)))

(defn draw-state [{:keys [circles]}]
  (q/background 255)
  (q/stroke 0)
  (q/fill 0 0 0 0)
  (q/bezier
    (f/w 0.1) (f/h 0)
    (f/w 0.013) (f/h 0.01)
    (f/w 0.012) (f/h 0.02)
    (f/w 0.1) (f/h 0.1))

  (doseq [circle circles]
    (draw-circle circle))
  (if (q/key-pressed?)
      (let []
        (q/fill 0 255 0)
        (q/text (str (f/h 0.1)) 10 10)
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
