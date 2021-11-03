(ns gen-art.walker
  (:require [gen-art.core :as core]
            [genartlib.util :as gu]
            [quil.core :as q]
            [quil.applet :as applet]
            [quil.middleware :as m]))

;; A random walker from nature of code

(defn random
  "Random distribution of width `n` centerd at 0."
  [n]
  (- (rand n) (/ n 2)))

(defn setup []
  (q/frame-rate 500)
  (q/color-mode :hsb)
  (q/background 240)
  {:x (gu/w 0.5)
   :y (gu/h 0.5)
   :tx 0
   :ty 10000})

(defn update-state-1 [{:keys [x y]}]
  {:x (mod (+ x (random 2)) (gu/w))
   :y (mod (+ y (random 2)) (gu/h))})

(defn update-state-2 [{:keys [x y]}]
  (let [r (rand)
        mod-fn (fn [f m x] (mod (f x) m))
        mod-inc (partial mod-fn inc)
        mod-dec (partial mod-fn dec)]
    (condp > r
      0.4 {:x (mod-inc (gu/w) x) :y y}
      0.6 {:x (mod-dec (gu/w) x) :y y}
      0.8 {:x x :y (mod-inc (gu/h) y)}
      1.0 {:x x :y (mod-dec (gu/h) y)})))

(defn perlin-walker
  [{:keys [x y tx ty]}]
  {:x (mod (+ x (q/noise tx)) (gu/w))
   :y (mod (+ y (q/noise ty)) (gu/h))
   :tx (inc tx)
   :ty (inc ty)})

(defn draw [{:keys [x y]}]
  (q/stroke 0)
  (q/point x y))

(def app (core/create {:title "Nature of code - Walker"
                       :size [250 250]
                       :setup setup
                       :draw draw
                       :update update-state-1
                       :features [:keep-on-top]
                       :middleware [m/fun-mode]}))
