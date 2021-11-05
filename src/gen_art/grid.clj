(ns gen-art.grid
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]))

(def x-res 40)
(def y-res 40)
(def bg [0 0 0])
(def lin-height 14)
(def color [236 240 241])

(defn create-flow
  []
  (map (fn [i]
         (map (constantly (* (/ i x-res) Math/PI))
              (range y-res)))
       (range x-res)))

(defn map-grid
  "Take a grid point and map it to the width and height"
  [x y width height]
  [(q/map-range x 0 x-res 0 width)
   (q/map-range y 0 y-res 0 height)])

(defn setup
  []
  (q/frame-rate 120)
  {:flow (create-flow)})

(defn polar->cart
  "Convert from polar coordinates to cartesian coordinates."
  [r theta]
  [(* r (q/cos theta))
   (* r (q/sin theta))])

(defn get-theta
  "Given two points, calculate the angle between them"
  [x1 y1 x2 y2]
  (Math/atan2 (- y2 y1)
              (- x2 x1)))

(defn draw
  [{:keys [flow]}]
  (apply q/background bg)
  (apply q/stroke color)
  (reduce (fn [_ row]
            (reduce (fn [_ col]
                      (let [[gx gy] (map-grid row col (gu/w) (gu/h))
                            theta (nth (nth flow row) col)
                            [x y] (polar->cart lin-height theta)]
                        (q/with-translation [gx gy]
                          (q/line 0 0 x y))))
                    (range y-res)))
          (range x-res)))

(defn update-state
  [state]
  (let [mx (q/mouse-x)
        my (q/mouse-y)]
    (assoc state
           :flow
           (mapv (fn [r]
                   (mapv (fn [c]
                           (let [[x y] (map-grid r c (gu/w) (gu/h))]
                             (- (get-theta mx my x y) Math/PI)))
                         (range y-res)))
                 (range x-res)))))

(def app (core/create {:title "Grid of points"
                       :size [640 480]
                       :setup setup
                       :draw draw
                       :update update-state
                       :features [:keep-on-top]
                       :middleware [m/fun-mode]}))
