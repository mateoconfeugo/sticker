(ns flourish-common.pipeline)

(def ^:private pipe-arg (gensym "pipeline-argument"))

(defmacro branch [pred & body]
  `(fn [x#]
     (if (~pred x#) (-> x# ~@body) x#)))

(defn- req
  "Required argument"
  [pred spec message]
  (assert (pred (first spec))
          (str message " : " (pr-str (first spec))))
  [(first spec) (rest spec)])

(defn- opt
  "Optional argument"
  [pred spec]
  (if (pred (first spec))
    [(list (first spec)) (rest spec)]
    [nil spec]))

(defmacro defpipeline [name & spec]
  (let [[docstring spec] (opt string? spec)
        [attr-map spec] (opt map? spec)]
    `(defn ~name 
       ~@docstring
       ~@attr-map
       [arg#]
       (-> arg# ~@spec))))

(defmacro defpipe
  "Defines a function which takes one argument, a map. The params are
  symbols, which will be bound to values from the map as by :keys
  destructuring. In any tail position of the body, use the 'return'
  macro to update and return the input map."
  [name & spec]
  {:arglists '([name doc-string? attr-map? [params*] & body])}
  (let [[docstring spec] (opt string? spec)
        [attr-map spec] (opt map? spec)
        [argv spec] (req vector? spec "Should be a vector")]
    (assert (every? symbol? argv)
            (str "Should be a vector of symbols : "
                 (pr-str argv)))
    `(defn ~name
       ~@docstring
       ~@attr-map
       [~pipe-arg]
       (let [{:keys ~argv} ~pipe-arg]
         ~@spec))))

(defn- return-clause [spec]
  (let [[command sym & body] spec]
    (case command
      :update `(update-in [~(keyword (name sym))] ~@body)
      :set    `(assoc ~(keyword (name sym)) ~@body)
      :remove `(dissoc ~(keyword (name sym)) ~@body)
      body)))

(defmacro return
  "Within the body of the defpipe macro, returns the input argument of
  the defpipe function. Must be in tail position. The input argument,
  a map, is threaded through exprs as by the -> macro.

  Expressions within the 'return' macro may take one of the following
  forms:

      (:set key value)      ; like (assoc :key value)
      (:remove key)         ; like (dissoc :key)
      (:update key f args*) ; like (update-in [:key] f args*)

  Optionally, any other expression may be used: the input map will be
  inserted as its first argument."
  [& exprs]
  `(-> ~pipe-arg
       ~@(map return-clause exprs)))