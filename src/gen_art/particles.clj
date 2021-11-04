(ns gen-art.particles
  (:require [euclidean.math.vector :as v]
            [genartlib.util :as gu]
            [gen-art.core :as core]
            [gen-art.utils :as utils]
            [quil.core :as q]
            [quil.middleware :as m]))

(def v-factor (atom 1))
(def edge-factor (atom 100))
(def color (atom [236 240 241]))

(defn create-particle
  ([]
   (create-particle (rand-int (gu/w))
                    (rand-int (gu/h))
                    (+ 3 (rand-int 5))))
  ([x y r]
   {:loc (v/vector x y)
    :velocity (v/vector (- (* 2 (rand)) 1)
                        (- (* 2 (rand)) 1))
    :alpha (* 256 (rand))
    :radius r}))

(defn update-particle
  [{:keys [loc velocity] :as particle}]
  (let [v-fac (v/vector @v-factor @v-factor)
        new-loc (v/add loc (v/mult velocity
                                   v-fac))
        [x-check y-check] (utils/check-edges new-loc (gu/w) (gu/h))]
    (assoc particle
           :loc new-loc
           :velocity (apply v/vector
                            (cond-> velocity
                              (contains? #{:lzero :gwidth} x-check)
                              (v/mult (v/vector -1 1))

                              (contains? #{:lzero :gheight} y-check)
                              (v/mult (v/vector 1 -1)))))))

(defn draw-edge?
  "Draw an edge between two particles."
  [{loc1 :loc alpha1 :alpha} {loc2 :loc alpha2 :alpha}]
  (when (< (utils/v-dist loc1 loc2) @edge-factor)
    [true (/ (+ alpha1 alpha2) 2)]))

(defn setup
  []
  (q/frame-rate 60)
  {:particles (doall (repeatedly 300 create-particle))})

(defn draw
  [{:keys [particles]}]
  (q/background 0 0 0)
  (doseq [{:keys [loc radius alpha] :as p} particles]
    (q/stroke 0)
    (apply q/fill (conj @color alpha))
    (q/ellipse (loc 0) (loc 1) radius radius)
    (doseq [par particles]
      (when-let [[_ a](draw-edge? p par)]
        (apply q/stroke (conj @color a))
        (q/line ((:loc p) 0)
                ((:loc p) 1)
                ((:loc par) 0)
                ((:loc par) 1))))))

(defn update-state
  [{:keys [particles] :as state}]
  (assoc state
         :particles (mapv update-particle particles)))

(def app (core/create {:title "Particles"
                       :size [1366 768]
                       :setup setup
                       :draw draw
                       :update update-state
                       :features [:keep-on-top]
                       :middleware [m/fun-mode]}))
