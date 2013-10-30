(ns site-builder-udc.site-builder
  "The business logic streams manipulate a composite data structure
   made up of the associated documents, the landing site dom, the
   mapping between the two. "
  (:require [com.ashafa.clutch :refer [update-document get-document put-document document-exists? create! couch all-documents with-db]]
            [flourish-common.utils :refer [str->int]]
            [clojure.walk :refer [keywordize-keys]]
            [site-builder-udc.db :refer [get-landing-sites]]))

(defn get-new-landing-site-id
  "Return the increment of the highest landing site id"
  [db]
  (let [docs  (all-documents db)]
    (if (> (count docs) 0)
      (inc (first (sort > (remove  #(= % nil) (map #(-> % :id str->int) docs)))))
      1)))

(defn update-tmp-xpath-uuid-index
  "Maintains a xpath to unique id map so that it is easy for both client and
   server to perform dom edits"
  [{:keys [db landing-site-id xpath uuid snippet-html] :as args}]
  (let [token (keyword (str xpath "-" uuid))
        landing-site (get-document db landing-site-id)
        updates (update-in landing-site [:tmp-xpath-uuid] merge {token snippet-html})]
    (with-db db
      (update-document landing-site updates))))

(defn update-landing-site
  "These are the edits that are being made to the document before it is saved so that a user
   doesn't lose any work if something happens to their browser. "
  [{:keys [db landing-site-id xpath page-html snippet-html uuid] :as args}]
  (let [opts {:db db :landing-site-id landing-site-id :xpath xpath :uuid uuid :snippet-html snippet-html}
        _ (update-tmp-xpath-uuid-index opts)]
    (with-db db
      (update-document (get-document db landing-site-id) {:tmp-page-html page-html}))))

(defn save-landing-site
  "Saves the edits in the persistence store"
  [{:keys [db landing-site-id page-html] :as args}]
  (let [ls (get-document db landing-site-id)
        updates (dissoc
                 (assoc ls :page-html page-html :xpath-uuid (merge (get ls :tmp-xpath-uuid) (get ls :xpath-uuid)))
                 :tmp-page-html :tmp-xpath-uuid)]
    (with-db db
      (update-document ls updates))))
