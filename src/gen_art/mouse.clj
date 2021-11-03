(ns gen-art.mouse
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  {:mouse (v/vector (gu/w 0.5) (gu/h 0.5))})

(defn update-state [{:keys [_]}]
  (let [new-mouse (v/vector (q/mouse-x) (q/mouse-y))]
    {:mouse new-mouse}))

(defn draw [{:keys [mouse]}]
  (q/background 255)
  (q/line 0 0 (mouse 0) (mouse 1)))

(def app (core/create {:title "Move your mouse"
                       :size [640 360]
                       :setup setup
                       :draw draw
                       :update update-state
                       :features [:keep-on-top]
                       :middleware [m/fun-mode]}))

(comment
  (core/close app))
