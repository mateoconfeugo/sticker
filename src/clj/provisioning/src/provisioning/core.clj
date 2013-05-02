(ns provisioning.core)


(defn add-operating-agent []
  "adds a unique billable end user entity to the system")

(defn add-user []
  "adds a login account")

(defn setup-new-cms-account []
  "creates a new user account in the content management system")

(defn create-initial-landing-site []
  "creates the intitial default if need be landing site for the new user")

(defn create-initial-vertical-site []
  "Creates the intitial default if need be landing site for the new user")

(defn deploy-servers []
  "deploy the necessary servers needed to accomplish the desired footprint")
  

(defn publish-intial-content []
  "push content from cms to servers")

(defn provision-infrastructure
  ""
  (let [cms (setup-new-cms-account )
        landing-site (create-initial-landing-site  )
        vertical-site (create-intial-vertical-site )
        cms-client (watch-cms cms) ]
    (deploy-servers {:cms cms-client :vertical vertical-site :landing landing-site})))


(defn signup-adnetworks []
  "")

(defn audit-new-user-account []
  "make sure everything that needs to be install is up and operating")

(defn signup-new-user
  "top level object that ads a user to the system"
  [options]
  (let [operator (add-operating-agent options)
        user (add-user operator)
        footprint (provision-infrastructure operator)
        schemas (publish-intial-content footprint)
        ad-networks (signup-adnetworks)]
    (audit operator user footprint ad-networks)))


