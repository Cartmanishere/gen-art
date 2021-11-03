(ns gen-art.rotation
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 60)
  {:angle 0.0
   :angularVelocity 0.0
   :angularAcc 0.001})

(defn update-state [{:keys [angle angularVelocity] :as state}]
  (let [mouse-move (v/magnitude (v/sub (v/vector (q/mouse-x) (q/mouse-y))
                                       (v/vector (gu/w 0.5) (gu/h 0.5))))
        acc (q/map-range mouse-move 0.0 (gu/w) -0.01 0.01)
        angularVelocity (+ angularVelocity acc)]
    (assoc state
           :angle (+ angle angularVelocity)
           :angularVelocity angularVelocity)))

(defn draw [{:keys [angle]}]
  (q/background 240)
  (q/with-translation [(gu/w 0.5) (gu/h 0.5)]
    (q/rotate angle)
    (q/rect-mode :center)
    (q/rect 0 0 30 90)))

(def app
  (core/create {:title "Nature of code - Bouncing ball in vector"
                :size [640 360]
                :setup setup
                :draw draw
                :update update-state
                :features [:keep-on-top]
                :middleware [m/fun-mode]}))
