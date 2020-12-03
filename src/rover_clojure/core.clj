(ns rover-clojure.core
  (:require [clojure.string :as str]))

(defn split-input-at [input i]
  (str/split
    (get (str/split input #"\n") i)
    #" "))

(defn initial-coordinates [input]
  (split-input-at input 1))

(defn instructions [input]
  (let [move-list (str/split input #"\n")]
    (if (and (= (count move-list) 3)
             (not (empty? (get move-list 2))))
      (apply list (str/split (get move-list 2) #""))
      '())))

(defn plateau
  [input]
  (let [plat (map #(Integer/parseInt %) (split-input-at input 0))]
    (fn [x y]
      (not (and (and (<= x (first plat)) (>= x 0))
                (and (<= y (second plat)) (>= y 0)))))))

(defn get-x [position]
  (Integer/parseInt (get position 0)))

(defn get-y [position]
  (Integer/parseInt (get position 1)))

(defn turn [direction instruction]
  (let [compass ["N" "E" "S" "W"]
        dir-index (str/index-of (apply str compass) direction)]
    (cond
      (= instruction "R") (get compass (mod (+ dir-index 1) 4))
      (= instruction "L") (get compass (mod (- dir-index 1) 4))
      :else direction)
    ))

(defn difference
  [direction instruction]
  (cond
    (or (= instruction "L")
        (= instruction "R")) [0 0 (turn direction instruction)]
    (= direction "N") [0 1 direction]
    (= direction "S") [0 -1 direction]
    (= direction "E") [1 0 direction]
    (= direction "W") [-1 0 direction]))

(defn sum-positions [pos-1 pos-2]
  [(str (+ (get-x pos-1) (first pos-2)))
   (str (+ (get-y pos-1) (second pos-2)))
   (get pos-2 2)])

(defn step [position instruction]
  (sum-positions position (difference (get position 2) instruction)))

(defn move [instructions position out-of-bounds?]
  (cond
    (> (count instructions) 0) (recur (pop instructions) (step position (first instructions)) out-of-bounds?)
    (out-of-bounds? (get-x position) (get-y position)) "out of bounds"
    :else (str/join " " position)))

(defn rover [input]
  (move (instructions input)
        (initial-coordinates input)
        (plateau input)))
