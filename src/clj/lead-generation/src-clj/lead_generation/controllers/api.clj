(ns lead-generation.controllers.api
  "API controller for lead generation client")

(defn save [{:keys [xpath layout dom]}] {:xpath xpath
                                         :layout layout
                                         :dom dom})


