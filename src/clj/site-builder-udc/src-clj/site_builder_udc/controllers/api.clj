(ns site-builder-udc.controllers.api
  "API controller for site-builder client")

(defn save [{:keys [xpath layout dom]}] {:xpath xpath
                                         :layout layout
                                         :dom dom})

(defn ping-the-api [pingback]
  (str "You have hit the API with: " pingback))

