(ns rover-clojure.core-test
  (:require [clojure.test :refer :all]
            [rover-clojure.core :refer :all]))

(deftest a-test
  (testing "instructions splitter"
    (is (= (instructions "5 5\n1 2 N") '()))
    (is (= (instructions "5 5\n1 2 N\nM") '("M")))
    (is (= (instructions "5 5\n1 2 N\nMM") '("M", "M")))
    )

  (testing "turning"
    (is (= (turn "N" "L") "W"))
    (is (= (turn "E" "R") "S"))
    (is (= (turn "N" "R") "E"))
    (is (= (turn "W" "L") "S"))
    )

  (testing "advance-y"
    (is (= (advance-y ["0" "0" "N"] "M") "1"))
    (is (= (advance-y ["3" "3" "S"] "M") "2"))
    (is (= (advance-y ["0" "0" "E"] "M") "0"))
    (is (= (advance-y ["0" "0" "E"] "L") "0"))
    (is (= (advance-y ["0" "0" "E"] "R") "0"))
    )

  (testing "advance-x"
    (is (= (advance-x ["2" "2" "E"] "M") "3"))
    (is (= (advance-x ["2" "2" "W"] "M") "1"))
    (is (= (advance-x ["2" "2" "S"] "M") "2"))
    (is (= (advance-x ["2" "2" "N"] "M") "2"))
    (is (= (advance-x ["2" "2" "E"] "L") "2"))
    (is (= (advance-x ["2" "2" "E"] "R") "2"))
    )

  (testing "step"
    (is (= (step ["0" "0" "N"] "M") ["0" "1" "N"]))
    (is (= (step ["0" "0" "N"] "R") ["0" "0" "E"]))
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
    (is (= (rover "5 5\n1 2 N\n") "1 2 N"))
    (is (= (rover "5 5\n0 0 N\nMMM") "0 3 N"))
    (is (= (rover "5 5\n1 2 N\nLMLMLMLMM") "1 3 N"))
    (is (= (rover "5 5\n3 3 E\nMMRMMRMRRM") "5 1 E"))
    (is (= (rover "3 3\n2 2 N\nMMMMM") "out of bounds"))
    )
  )
