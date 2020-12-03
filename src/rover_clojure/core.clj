(ns rover-clojure.core
  (:require [clojure.string :as str]))

(defn initial-coordinates
  "get initial coordinates from input"
  [input]
  (str/split
    (get (str/split input #"\n") 1)
    #" "))

(defn instructions
  "get instructions from input"
  [input]
  (let [move-list (str/split input #"\n")]
    (if (and (= (count move-list) 3)
             (not (empty? (get move-list 2))))
      (apply list (str/split (get move-list 2) #""))
      '())))

(defn plateau
  [input]
  (let [plat (map #(Integer/parseInt %) (str/split
               (get (str/split input #"\n") 0)
               #" "))]
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

(defn advance-y [position instruction]
  (if (= instruction "M")
    (cond
      (= "N" (get position 2)) (str (+ (get-y position) 1))
      (= "S" (get position 2)) (str (- (get-y position) 1))
      :else (get position 1))
    (get position 1)))

(defn advance-x [position instruction]
  (if (= instruction "M")
    (cond
      (= "E" (get position 2)) (str (+ (get-x position) 1))
      (= "W" (get position 2)) (str (- (get-x position) 1))
      :else (get position 0))
    (get position 0)))

(defn step
  [position instruction]
  [(advance-x position instruction)
   (advance-y position instruction)
   (turn (get position 2) instruction)]
  )


(defn move [instructions position out-of-bounds?]
  (cond
    (> (count instructions) 0)
      (recur (pop instructions) (step position (first instructions)) out-of-bounds?)
    (out-of-bounds? (get-x position) (get-y position)) "out of bounds"
    :else (str/join " " position)))

(defn rover [input]
  (move (instructions input)
        (initial-coordinates input)
        (plateau input)))
