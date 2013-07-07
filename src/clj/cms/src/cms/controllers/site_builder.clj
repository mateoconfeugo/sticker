(ns cms.controllers.site-builder
  "Web accible front-end for the site builder editor"
  (:use [cms.site-builder]
        [cms.views.builder :only [render]]
        [compojure.core :only [defroutes GET DELETE POST PUT]]))

(defroutes editor-routes
  (GET "/cms/sitebuilder"  req (render req))
  (GET "/cms/sitebuilder/:ls-id" [ls-id :as req] (preview-landing-site {:landing-site-id ls-id}))    
  (DELETE "/cms/sitebuilder/:ls-id" [ls-id :as req] (delete-landing-site {:landing-site-id ls-id}))
  (POST "/cms/sitebuilder/:ls-id" [ls-id :as req](save-landing-site {:landing-site-id ls-id :model (-> req :parms :model)}))
  (PUT "/cms/sitebuilder/:ls-id" [ls-id :as req] (save-landing-site {:landing-site-id ls-id :model (-> req :parms :model)}))
  (GET "/cms/sitebuilder/edit:ls-id" [ls-id :as req] (edit-landing-site {:landing-site-id ls-id}))
  (POST "/cms/sitebuilder/publish/:ls-id" [ls-id :as req] (edit-landing-site {:landing-site-id ls-id :model (-> req :params :publish-spec)})))
