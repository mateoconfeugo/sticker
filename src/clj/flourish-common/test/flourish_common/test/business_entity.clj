;;            [clojure.tools.reader :refer [read read-string *default-data-reader-fn* *read-eval* *data-readers*]]
(ns flourish-common.test.business-entity
  (:refer-clojure :exclude [read read-string *default-data-reader-fn* *read-eval* *data-readers*])  
  (:require [flourish-common.business-entity :refer []]
            [clojure.test :refer [is testing deftest]]
            [clojure.edn :as edn :refer [read]]
            [clojure.tools.reader :as r :refer [read-string]]            
            [expectations :refer [expect]]
            [me.raynes.fs :as fs :refer [temp-file]]))

(r/read-string "1")
(r/read-string "#=(+ 1 2)")
(binding [r/*read-eval* true]
  (r/read-string "#=(+ 1 2)"))

(defn my-reader [[a b]]
  (println (+ (* 10 a) b)))

(defn my-cms [txt] 42)

(def dispatch-table {'foo/tag #'my-reader})


                     :snippet (fn [] (+ 1 1))
                     "foo/cms-data" (fn [] (+ 1 2))
                     :template-html (fn [] (+ 1 3))})

(def edn-test-filepath (str (System/getProperty "user.dir") "/test/flourish_common/test/biz_entity_test.edn"))

(with-in-str (slurp edn-test-filepath)
  (clojure.edn/read {:readers dispatch-table
                     :default (fn [t v] {:t t :v v})} *in*))

(defn read-landing-site
  [{:keys [filepath dispatch-table] :as args}]
  "read in the data and code that will build the function and provide the data to render the site"
  (r/read-string {:readers dispatch-table} (slurp filepath)))



(binding [*data-readers*
          {'foo/tag #'my-reader}]
    (read-string "#foo/tag [4 2]"))
(r/read-string {:readers dispatch-table} (slurp  edn-test-filepath))

(read-landing-site {:filepath edn-test-filepath :dispatch-table dispatch-table})

(expect true (= 1 1))


(defn my-reader [[a b]]
  (println (+ (* 10 a) b)))

(defn my-cms [txt] 42)

(def test-str )



(binding [*data-readers*
          {'foo/tag #'my-reader
           'foo/cms-data #'my-cms
           }]
  (read-string (slurp  edn-test-filepath)))


