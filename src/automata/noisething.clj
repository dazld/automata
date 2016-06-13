(ns automata.noisething
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/smooth)
  (q/frame-rate 30)
  (q/background 255)
  (q/color-mode :hsb)
  {:color 0
   :angle 0})

(defn update-state [state]

  {:color 0
   :angle 0})

(defn cycler []
  (let [pos (atom 0)
        next (atom 1)]
    (fn []
      (if (> @pos 255)
        (reset! pos 128))
      (swap! pos + 1))))

(def thing (cycler))

(defn pix [x y scaler]
  (q/no-stroke)
  (q/no-smooth)
  (q/fill (* scaler (q/noise (/ x 50) (/ y 50))))
  (q/rect x y 1 1))

(defn draw-state [state]
  (q/background 255)
  (let [s (thing)]
    (doseq [x (range 0 50)
            y (range 0 50)]
      (pix x y s))))

(q/defsketch automata
             :title "grid automatics"
             :size [500 300]
             :setup setup
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
