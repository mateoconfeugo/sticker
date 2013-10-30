# management

Management gui for the overall system to administer users, accounts and relevant operations.
The user interface of the software business components plugin into the project - the idea here is making
it easy to add features.  The software business components themselves are jar components on a private s3 repository
allowing for a more granular development process of just updating the dependency version of the particular components
in the project file.  This ring compojure app can be hosted on heroku.

## Prerequisites

You will need [Leiningen][1] 2 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 Matt Burns
