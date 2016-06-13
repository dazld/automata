(ns automata.arcs
  (:require [quil.core :as q]
            [quil.middleware :as m]))


(def aberration-amount 1.0)
(def shutter-angle 0.5)
(def num-frames 48)
(def samples-per-frame 32)
(def tau (* 2 Math/PI))

(defn h
  ([] (h 1.0))
  ([value] (* (q/height) value)))

(defn w
  ([] (w 1.0))
  ([value] (* (q/width) value)))

(defn rads [deg]
  (Math/toRadians deg))

(defn clamp [n]
  (if (< n -2)
    -2
    (if (> n 2)
      2
      n)))

(defn clamped-gaussian []
  (/
    (+ 2 (clamp (q/random-gaussian)))
    4))

(defn random-angle []
  (* (clamped-gaussian) 360))

(defn make-arc []
  (let [start (random-angle)
        end (+ start (random-angle))]
    {:x 0
     :y 0
     :direction 1
     :start-angle start
     :angle end}))

(defn check-direction [angle direction]
  (if (> angle 360)
    -1
    (if (< angle 0)
      1
      direction)))

(defn update-arc [arc]
  {:x 0
   :y 0
   :direction (check-direction (:angle arc) (:direction arc))
   :start-angle (:start-angle arc)
   :angle (+ (:angle arc) (:direction arc))})

(defn setup []
  (q/frame-rate 1)
  {:arcs (repeatedly 10 make-arc)})

(defn update-state [state]
  (let [arcs (:arcs state)]
    {
     :arcs (map update-arc arcs)
     :time (mod (+ 1 (:time state)) 48)

     (defn paint [state]
       (q/background 0)
       (q/fill 255)
       (let [time (:time state)
             x (+ (/ (q/width ) 2) (* 100 (Math/sin (* tau time))))
             y (+ (/ (q/height) 2) (* 100 (Math/sin (* 2 tau time))))
             width 50
             height 50]
         (q/ellipse x y width height)))}))

(defn draw-glitch [state]
  (doseq [a (range 3)]
    '(println a x y time (+ 1 (* 0.008 a aberration-amount)))
    (q/push-matrix)
    (q/scale (+ 1 (* 0.008 a aberration-amount)))
    (paint state)
    (q/pop-matrix)
    (aclone (q/pixels))))
(defn draw-blur [])




(defn draw-state [state]
  (q/background 0)
  (q/frame-rate 10)
  (paint state)
  (q/with-translation [(/ (q/width) 2)
                       (/ (q/height) 2)]

    (q/no-stroke)

    '(doseq [n (range 0.1 1 0.01)]
      (let [arc (make-arc)
            x (q/noise n)
            y (q/noise (+ 1000 n))
            gn (w (Math/abs (q/random-gaussian)))]

        (q/smooth)
        (q/no-fill)
        (q/stroke (* 255 n) (* 192 n) (* 32 n) 100)
        '(q/arc x y gn gn (rads (:start-angle arc)) (rads (:angle arc)))))))



;         (q/save "output.png")


(defn draw []
  (let [pix (q/pixels)]
    (doseq [x (range (q/width))
            y (range (q/height))]
      (let [bright (q/map-range (q/noise (* x 0.03) (* y 0.03)) 0 1 0 255)

            wid (q/width)]
        (aset-int pix (+ x (* y wid)) (q/color bright)))))

  (q/update-pixels))

(q/defsketch automata
             :title "arcs"
             :size [500 500]
             :setup setup 
             :renderer :p2d
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
