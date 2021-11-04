(ns gen-art.ball
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [quil.core :as q]
            [quil.middleware :as m]))

(defn rand-color
  []
  (doall (repeatedly 3 #(rand-int 256))))

;;;;;;;;;;;;;;;;;;;;
;; Ball Functions ;;
;;;;;;;;;;;;;;;;;;;;

(defn create-ball
  "Create a ball object containing `location`, `veloctiy`, `acceleration` and `mass`."
  ([]
   (create-ball (rand-int (gu/w 0.5))
                (rand-int (gu/h 0.5))))
  ([x y]
   (create-ball x y (+ 20 (rand-int 30))))
  ([x y mass]
   {:location (v/vector x y)
    :mass mass
    :color (rand-color)
    :velocity (v/vector 0.0 0.0)
    :acceleration (v/vector 0.0 0.0)}))

(defn apply-force
  "Apply multiple forces to the ball and calculate the acceleration based on that."
  [{:keys [mass] :as ball} & forces]
  (reduce (fn [ball f]
            (update ball
                    :acceleration
                    #(v/add %
                            (v/div f
                                   (v/vector mass mass)))))
          ball
          forces))

(defn update-ball
  "Take a ball and update the location of the ball using the acceleration and velocity."
  [{:keys [location velocity acceleration] :as ball}]
  (let [new-velocity (v/add velocity acceleration)
        new-location (v/add location new-velocity)]
    (assoc ball
           :acceleration (v/vector 0.0 0.0)
           :velocity new-velocity
           :location new-location)))

(defn check-edge
  "Whenever the edge hits an edge, loop around the canvas"
  [{:keys [location] :as ball}]
  (let [x (location 0)
        y (location 1)]
    (cond-> ball

      ;; TODO: This is ugly. Fix it.
      (or (> x (gu/w))
          (< x 0))
      ((fn [b]
         (-> b
             (update :velocity (fn [vc]
                                 (v/mult vc (v/vector -1 1))))
             (update :location (fn [loc]
                                 (v/vector (if (> x (gu/w))
                                             (gu/w)
                                             0)
                                           (loc 1)))))))


      (or (> y (gu/h))
          (< y 0))
      ((fn [b]
         (-> b
          (update :velocity (fn [vc]
                              (v/mult vc (v/vector 1 -1))))
          (update :location (fn [loc]
                              (v/vector (loc 0)
                                        (if (> y (gu/h))
                                          (gu/h)
                                          0))))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;
;; Animation Functions ;;
;;;;;;;;;;;;;;;;;;;;;;;;;

(defn update-state
  [{:keys [balls xoff yoff] :as state}]
  (let [force (v/vector (q/noise xoff) (q/noise yoff))
        new-balls (->> balls
                       (map #(apply-force % force))
                       (map #(update-ball %))
                       (mapv #(check-edge %)))]
    (assoc state
           :xoff (inc xoff)
           :yoff (inc yoff)
           :balls new-balls)))


(defn setup []
  (q/frame-rate 120)
  {:balls (doall (repeatedly 100 create-ball))
   :xoff 1
   :yoff 1000000})

(defn draw [{:keys [balls]}]
  (q/stroke 0)
  (q/background 0 0 0)
  (doseq [{:keys [location color mass]} balls]
    (apply q/fill color)
    (q/ellipse (location 0)
               (location 1)
               mass
               mass)))


(def app-config {:title "Ball under forces"
                 :size [640 480]
                 :setup setup
                 :draw draw
                 :update update-state
                 :features [:keep-on-top]
                 :middleware [m/fun-mode]})

(def app (core/create app-config))
