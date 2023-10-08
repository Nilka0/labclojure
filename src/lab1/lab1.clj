(ns lab1.lab1)
(defn lab1 [n lst]
  (when (seq lst)
    (let [head (first lst)
          tail (rest lst)
          run-length (count (take-while #(= head %) lst))]
      (lazy-seq
        (concat (repeat (min n run-length) head)
                (lab1 n (drop run-length lst)))))))