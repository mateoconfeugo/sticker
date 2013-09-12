(ns repl-client.core
  (:require [clojure.tools.nrepl :as repl :refer [connect client message response-values]]
            [clojure.pprint :refer [pprint]]))

(defn read-all
  [f]
  "Reads all top-level forms from f, which will be coerced by
  clojure.java.io/reader into a suitable input source. Not lazy."
  (with-open [pbr (java.io.PushbackReader. (clojure.java.io/reader f))]
    (doall
     (take-while
      #(not= ::eof %)
            (repeatedly #(read pbr false ::eof))))))

(def src-code (read-all (java.io.FileReader. "user_1_landing_site_1_data.clj")))

;;  

(defn run-loaded-code-on-server [file server port]  
  (with-open [conn (repl/connect :port 7888)]
    (-> (repl/client conn 1000)
        (repl/message {:op :eval :code src-code})        
        doall  
        pprint)))

(defn load-code-on-server [filename server port]  
  (with-open [conn (repl/connect :host server :port (or port 7888))]
    (-> (repl/client conn 1000)
        (repl/message {:op :eval :code "(load-file  filename)"})
        doall  
        pprint)))

(defn get-landing-site-src-code [landing-site]
  "Gets the data model needed to build the landing site from
   the document store"
  {})

(defn get-src-code-filename [user landing-site base-dir]
  "Creates the file name based from user and landing site data"
  (str base-dir "/user-" (:id user) "-landing-site-" (:id landing-site) "-data.clj"))

(defn copy-to-delivery [filename server]
  "Moves the customized landing site clojure source code file to the delivery server")

(defn publish-landing-site [user landing-site destinations]
  "Publish user landing site to the destinations (production).
   A customized piece of clojure code that contains the function that
   the server uses to build the dom that houses all the elments and
   resources the landing site single page web application needs to run.
   The clojure is constructed from various resources in the authoring document
   store.  A single clojure file is created and then copied to the destinations.
   Once the file is in place the destination is contacted via its repl server
   and the file that was just copied gets loaded into the program"
  (let [ls-src-code (get-landing-site-src-code user landing-site)
        filename (get-src-code-filename user landing-site)]
    (doseq [d destinations]
      (publish-code code d)
      (load-code-on-server filename (:host d) (:port d))
      (with-open [conn (repl/connect (:host d) (:port d))]
        (-> (repl/client conn 1000)
            (repl/message {:op :eval :code "(report-publish-successful)"})        
            doall)))))

(defn publish-code [code server]
  "Moves the clojure source code from the authoring area into
   the production publishing servers"
  (->> code spit (copy-to-delivery server))


(load-code-on-server src-code)
(run-loaded-code-on-server )

