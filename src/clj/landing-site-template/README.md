# landing-site

A leiningen project template that creates the a customized clojure clojurescript ring compojure landing site application web server hosting the landing site delivery engine.
This application is created and deployed programmatically.  This allows our application to scale horizontal. Each application gets its own
heroku application to run in the cloud and corresponding github repository.  The github repo allows for rolling back and all the other features git offers.

## Usage

lein new landing-site foo.com
** lein publish destination - coming soon


## License

Copyright © 2013 Matt Burns

Distributed under the Eclipse Public License, the same as Clojure.
