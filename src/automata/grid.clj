(ns automata.grid
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

(defn pix [x y]
  (q/no-stroke)
  (q/fill 0)
  (q/rect x y 1 1))

(defn draw-state [state]
  (q/background 255)
  (let [s (thing)]
    (doseq [x (range 5 (q/width) 10)
            y (range 5 (q/height) 10)]
      (pix x y))))

(q/defsketch automata
             :title "grid automatics"
             :size [500 300]
             :setup setup
             :update update-state
             :draw draw-state
             :features [:keep-on-top]
             :middleware [m/fun-mode])
