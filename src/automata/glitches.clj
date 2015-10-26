(ns automata.glitches
  (:require [quil.core :as q]
            [quil.middleware :as m]))


(def aberration-amount 0.21)
(def shutter-angle 0.075)
(def num-frames 48)
(def samples-per-frame 32)
(def tau (* 2 Math/PI))

(defn h
  ([] (h 1.0))
  ([value] (* (q/height) value)))

(defn w
  ([] (w 1.0))
  ([value] (* (q/width) value)))


(defn setup []
  (q/frame-rate 1)
  {:time 0})

(defn ctime []
  (mod (q/map-range (- (q/frame-count) 1) 0 num-frames 0 1) 1))

(defn update-state [state]
  {
    :time (ctime)
   })


(defn paint [state]

  '(q/background 255)
  '(q/fill 255)
  (let [time (:time state)
        x (+ (/ (q/width ) 2) (* 100 (q/sin (* tau time))))
        y (+ (/ (q/height) 2) (* 100 (q/sin (* 2 tau time))))
        width 50
        height 50]
    (q/ellipse x y width height)))


(defn draw-glitch [state]
  (let [hw (/ (q/width) 2)
        hh (/ (q/height) 2)
        result (aclone (q/pixels))]
    '(println (bit-and (q/color 2 55 0) 0xffffffff) )
    (doseq [a (range 30)]
      ;'(println a x y time (+ 1 (* 0.008 a aberration-amount)))

      (q/push-matrix)
      (q/translate hw hh)
      (q/scale (+ 1 (* 0.008 a aberration-amount)))
      (q/translate (- hw) (- hh))
      (q/fill 255 0 0)
      (paint state)
      (q/pop-matrix)
      '(aclone (q/pixels)))))

(defn draw-blur [])

(defn draw-state [state]
  (q/background 120)
  (q/frame-rate 50)
  '(paint state)
  (draw-glitch state)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]


                      )
  '(q/save "output.png"))


(q/defsketch automata
             :title "arcs"
             :size [500 500]
             :setup setup
             :renderer :p2d
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
