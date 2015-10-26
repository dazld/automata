(ns automata.walker
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn rand-int [min max]
  (int (q/random min max)))

(defn rand-color []
  [(rand-int 0 255) 255 255])

(defn get-nextposition [p]
  (+ p (q/random -2 2)))

(defn make-walker []
  {:x 0
   :y 0
   ; hsb color
   :color (rand-color)})

(defn update-walker [walker]
  '(println walker)
  {:x (get-nextposition (:x walker))
   :y (get-nextposition (:y walker))
   :color (:color walker)})

(defn update-angle [angle]
  (+ angle 0.02))


(defn setup []
  (q/no-smooth)
  (q/frame-rate 30)
  (q/background 255)
  (q/color-mode :hsb)
  {:color 0
   :angle 0
   :walkers (repeatedly 100 make-walker)})



(defn update-state [state]
  '(println (:walkers state))
  {:color (mod (+ (:color state) 0.1) 255)
   :angle (update-angle (:angle state))
   :walkers (map update-walker (:walkers state))})

(defn draw-line [x1 y1 x2 y2]
  (q/stroke 132 128)
  (q/line x1 y1 x2 y2))

(defn pix [walker]
  (q/no-smooth)
  (q/no-stroke)
  (apply q/fill (:color walker))
  (q/rect (:x walker) (:y walker) 3 3))

(defn draw-state [state]
  '(q/background 255 )

  (let [walkers (:walkers state)]
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]

                        (doseq [walker walkers]

                          (pix walker)
                          (draw-line (:x walker) (:y walker) 0 0 ))


                        )))

(q/defsketch automata
             :title "You spin my circle right round"
             :size [500 500]
             :setup setup
             :update update-state
             :draw draw-state

             :features [:keep-on-top]
             :middleware [m/fun-mode])
