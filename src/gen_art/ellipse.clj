(ns gen-art.ellipse
  (:require [gen-art.core :as core]
            [quil.core :as q]
            [quil.applet :as applet]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 10)
  (q/color-mode :hsb)
  {:color 0
   :angle 0})

(defn update-state [state]
  ;; Update sketch state by changing circle color and position.
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

(defn draw-state [state]
  ;; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ;; Set circle color
  (q/fill (:color state) 255 255)
  ;; Calculate x and y coordinates of the circle.
  (let [angle (:angle state)
        x (* 150 (q/cos angle))
        y (* 150 (q/sin angle))]
    ;; Move origin point to the center of the sketch.
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ;; Draw the circle.
      (q/ellipse x y 100 100))))

(def app (core/create {:title "You spin my circle right round"
                       :size [500 500]
                       :setup setup
                       :update update-state
                       :draw draw-state
                       :features [:keep-on-top]
                       :middleware [m/fun-mode]}))
