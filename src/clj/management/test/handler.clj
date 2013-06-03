(ns handler
  (:use [expectations]
        [management.handler :only[app]]
        [management.views.snippets]
        [ring.mock.request]))

(def test-user-request {:request-method :get
                        :uri "/mgmt/user/1"
                        :headers {}
                        :params {:id 1}})

(def test-cfg-request {:request-method :get
                      :uri "/clientconfig"
                      :headers {}
                      :params {}})


(expect true (= (:status (app (request :get (:uri test-user-request)))) 200))
(expect true (= (:status (app (request :get "/clientconfig")) 200)))
(expect true (= (:status (app (request :get "/invalid-bogus-fake"))) 404))
(expect true (= (:status (app (request :get "/login"))) 200))
