(ns rover-clojure.core-test
  (:require [clojure.test :refer :all]
            [rover-clojure.core :refer :all]))

(deftest a-test
  (testing "instructions"
    (is (= (instructions "1 2 N") '()))
    (is (= (instructions "1 2 N\nM") '("M")))
    (is (= (instructions "1 2 N\nMM") '("M", "M")))
    )

  (testing "turning"
    (is (= (turn "N" "L") "W"))
    (is (= (turn "E" "R") "S"))
    (is (= (turn "N" "R") "E"))
    (is (= (turn "W" "L") "S"))
    )


  (testing "step"
    (is (= (step ["0" "0" "N"] "M") ["0" "1" "N"]))
    (is (= (step ["0" "0" "N"] "R") ["0" "0" "E"]))
    )

  (testing "difference"
    (is (= (difference "N" "L") [0 0 "W"]))
    (is (= (difference "N" "R") [0 0 "E"]))
    (is (= (difference "N" "M") [0 1 "N"]))
    (is (= (difference "S" "M") [0 -1 "S"]))
    (is (= (difference "E" "M") [1 0 "E"]))
    (is (= (difference "W" "M") [-1 0 "W"]))
    )

  (testing "sum-positions"
    (is (= (sum-positions ["0" "0" "N"] [0 1 "N"]) ["0" "1" "N"]))
    (is (= (sum-positions ["2" "4" "S"] [1 0 "E"]) ["3" "4" "E"]))
    (is (= (sum-positions ["2" "4" "S"] [1 -1 "S"]) ["3" "3" "S"]))
    )

  (testing "out-of-bounds?"
    (let [out-of-bounds-fn (plateau "5 5\n1 2 N\nM")]
      (is (false? (out-of-bounds-fn 0 0)))
      (is (false? (out-of-bounds-fn 5 5)))
      (is (true? (out-of-bounds-fn 6 5)))
      (is (true? (out-of-bounds-fn 5 6)))
      (is (true? (out-of-bounds-fn -1 0)))
      (is (true? (out-of-bounds-fn 0 -1)))
      )
    )

  (testing "Given input for one rover, return the final coordinates"
    (is (= (rover "1 2 N\n" (fn [x y] false)) "1 2 N"))
    (is (= (rover "0 0 N\nMMM" (fn [x y] false)) "0 3 N"))
    (is (= (rover "1 2 N\nLMLMLMLMM" (fn [x y] false)) "1 3 N"))
    (is (= (rover "3 3 E\nMMRMMRMRRM" (fn [x y] false)) "5 1 E"))
    (is (= (rover "2 2 N\nMMMMM" (fn [x y] true)) "out of bounds"))
    )

  (testing "multiple rovers"
    (is (= (mission "5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM")
           "1 3 N\n5 1 E"))
    )
  )
