(ns gen-art.ball
  (:require [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 120)
  (q/color-mode :hsb)
  (q/background 240)
  {:location {:x 100 :y 100}
   :velocity {:x 2.5 :y 0.5}})

(defn add-vec
  [{x1 :x y1 :y} {x2 :x y2 :y}]
  {:x (+ x1 x2)
   :y (+ y1 y2)})

(defn update-state [{:keys [location velocity]}]
  (let [new-location (add-vec location velocity)
        new-velocity {:x (if (or (> (:x new-location) (gu/w))
                                 (< (:x new-location) 0))
                           (* -1 (:x velocity))
                           (:x velocity))
                      :y (if (or (> (:y new-location) (gu/h))
                                 (< (:y new-location) 0))
                           (* -1 (:y velocity))
                           (:y velocity))}]
    {:location new-location
     :velocity new-velocity}))

(defn draw [{:keys [location]}]
  (q/stroke 0)
  (q/fill 175)
  (q/background 240)
  (q/ellipse (:x location) (:y location) 32 32))

(def app-config {:title "Nature of code - Bouncing ball in vector"
                 :size [200 360]
                 :setup setup
                 :draw draw
                 :update update-state
                 :features [:keep-on-top]
                 :middleware [m/fun-mode]})
