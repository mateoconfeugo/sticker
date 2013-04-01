(ns landing-site.cms
  "Configuration component that reads a json file when created"
  (:use [cheshire.core]))

;; INTERFACE SPECIFICATION
(defprotocol CMS-Filesystem
  "Site content file lookup interface"
  (assemble-site-files [this landing-site]
    "Determines the files that make up a landing site")
  (populate-contents [this files]
    "gets all the content for the single page web app view of the site")
  (get-page [this resource  market-vector]
    "returns the desired html for the requested page"))

;; BACK-END IMPLEMENTATION 
(defn read-custom-config
  "Read and transform  json data into a nested map"
  [file]
  (parse-string (slurp file) true))

(defn determine-path [request]
  "from a http request figure out the path to the resource")

(defn file-exists? [path]
  "Does the file exist")

(defn get-file-contents [this path]
  "Retreive the file contents at a give path")

;; IMPLEMENTATION CONSTRUCTOR
(defn new-cms-filesystem
  [{:keys[root-dir cfg site-schema] :as settings}]
  (let [base-path (if root-dir  root-dir (:cms-root-dir cfg))
        market_vectors (:market-vector site-schema)]

    (reify CMS-Filesystem
      ;; PROTOCOL METHOD IMPLEMENTATIONS
      (get-page [this resource  market-vector]
        (let [landing-site (:landing-site market-vector)
              full-path (base-path resource)]))
              ;; return contents of the page
      
      (assemble-site-files [this landing-site])
      (populate-contents [this files]))))
