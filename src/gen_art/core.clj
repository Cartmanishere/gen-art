(ns gen-art.core
  (:require [quil.core :as q]
            [quil.applet :as qa]))

(defn flat-1
  "Flatten elements of a vec for depth level 1"
  [v]
  (mapcat identity v))

(defn create
  "Creates an applet app and returns it."
  [app]
  (apply qa/applet (flat-1 app)))

(defn pause
  [app]
  (qa/with-applet app
    (q/no-loop)))

(defn resume
  [app]
  (qa/with-applet app
    (q/start-loop)))

(defn close
  [app]
  (.exit app))

(defn refresh
  [app app-config]
  (close app)
  (create app-config))
