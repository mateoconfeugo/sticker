(ns cms.site-builder
  "The business logic streams manipulate a composite data structure
   made up of the associated documents, the landing site dom, the
   mapping between the two. "
  (:use [flourish-common.pipeline :only[defpipe defpipeline return]]))

(defpipe validate  [model]
  "Make sure all the relevant data is present and makes sense")

(defpipe write-snippet-template []
  "Create the html file that the snippet function will populate
   integrating it into the rest of the page")

(defpipe build-snippet-function []
  "Take the associated cms document modal data and insert it into snippet html function")

(defpipe update-base-template []
  "Add, remove, change a selector snippet rule pair")

(defpipe update-base-html []
  "Add, remove, change the html in the base html template file")

(defpipe update-component-list []
  "Add, remove, change the html in the base html template file")  

(defpipe push-to-editor []
  "Provide the updated html bound to the updated content")

(defpipeline create-component 
  validate
  write-snippet-template
  build-snippet-function
  update-base-template
  update-base-html
  push-to-editor)

(defpipeline retrieve-component 
  push-to-editor)

(defpipeline update-component 
  validate
  write-snippet-template
  build-snippet-function
  update-base-template
  update-base-html
  push-to-editor)

(defpipeline delete-component 
  update-base-template
  update-base-html
  push-to-editor)

(defpipeline list-components 
  update-component-list
  push-to-editor)

(defpipeline preview-landing-site
  update-component-list
  push-to-editor)


(defpipeline delete-landing-site
  update-component-list
  push-to-editor)

(defpipeline save-landing-site
  update-component-list
  push-to-editor)

(defpipeline edit-landing-site
  update-component-list
  push-to-editor)





