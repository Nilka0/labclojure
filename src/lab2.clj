(ns lab2
  (:refer-clojure :exclude [read])
  (:require [clojure.core.async :refer [>! <!  go chan  close!]]
            [clojure.string :as str])
  (:gen-class))
(def file-as-str (slurp "C:\\Users\\verse\\IdeaProjects\\labclojure1\\src\\clojure2\\text.txt"))
(def read (str/split file-as-str #""))

(defn ch
  [c]
  (go
    (doseq [o read]
      (>! c o)
      ))
  )

(defn chars-to-words
  [words c]
  (go
    (loop [counter 1]
      (let [value (<! c)]
        (when value
          (>! words value)
          (if (> counter 1000)
            (close! words)
            (recur (inc counter))))))))
(defn -main
  [& _]
  (let [c (chan)
        _ (chan)
        words (chan 100 (comp
                          (partition-by (complement #{" "}))
                          (map #(apply str %))
                          (remove #{" "})))]

    (go
      (loop []
        (let [o (<! words)]
          (when (not= nil o)
            (println "Received from channel:" o)))
        (recur)))

    (ch c)

    (chars-to-words words c)
    (Thread/sleep 1000) ; Подождать завершения chars-to-words
    (close! words)
    ))