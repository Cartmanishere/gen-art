(ns gen-art.utils)

(defn random-dist
  "Uniform distribution of width `n` centerd at 0."
  [n]
  (- (rand n) (/ n 2)))

(defn check-edges
  "Check whether the location exceeds width or height of the canvas.
  How to interpret return values:
  - When x < 0: :lzero
  - When x > width: :gwidth
  - when y < 0: :lzero
  - when y > height: :gheight
  - Otherwise: nil
  Return a two values for x and y."
  [[x y] w h]
  (cond-> [nil nil]
    (> x w) (update 0 (constantly :gwidth))
    (< x 0) (update 0 (constantly :lzero))
    (> y h) (update 1 (constantly :gheight))
    (< y 0) (update 1 (constantly :lzero))))

(defn v-dist
  "Give euclidean distance between two vectors"
  [[x1 y1] [x2 y2]]
  (Math/sqrt (+ (Math/pow (- x1 x2) 2)
                (Math/pow (- y1 y2) 2))))
