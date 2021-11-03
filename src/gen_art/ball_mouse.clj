(ns gen-art.ball-mouse
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.applet :as applet]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 120)
  (q/color-mode :hsb)
  {:location (v/vector 100 100)
   :velocity (v/vector 0 0)})

(defn limit
  "If any vector's value is greater than limit, then reduce to the limit."
  [v limit]
  (apply v/vector (map (fn [x]
                  (if (> x limit)
                    limit
                    x)) v)))

(defn update-state [{:keys [location velocity]}]
  (let [dir (v/normalize (v/sub (v/vector (q/mouse-x) (q/mouse-y))
                                location))

        acc (v/mult dir (v/vector 0.5 0.5))
        new-velocity (limit (v/add velocity acc) 10)
        new-location (v/add location new-velocity)]
    {:location new-location
     :velocity new-velocity}))

(defn draw [{:keys [location]}]
  (q/stroke 0)
  (q/fill 175)
  (q/background 240)
  (q/ellipse (location 0) (location 1) 32 32))

(def app
  (core/create {:title "Nature of code - Bouncing ball in vector"
                :size [640 360]
                :setup setup
                :draw draw
                :update update-state
                :features [:keep-on-top]
                :middleware [m/fun-mode]}))
