# demo

generated using Luminus version "3.48"

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

You will need `npm` and `shadow-cljs` installed

`npm install -g shadow-cljs`

## Running

To start a web server for the application, run:

    lein run

### To compile cljs for development ###

    lein shadow compile app

### To launch shadow-cljs for development ###

    lein shadow watch app

### To launch everything from a repl ###

    (start)                                             ;; start web server
    (require '[shadow.cljs.devtools.server :as server])
    (server/start!)                                     ;; start shadow-cljs server
    (require '[shadow.cljs.devtools.api :as shadow])
    (shadow/watch :app)                                 ;; start watching the app. Requires package.json & shadow-cljs.edn to exist (e.g. run "lein shadow compile app" first)

### To connect to the cljs repl ###

    (shadow/repl :app)
    (js/alert "Hi")

### To quit the cljs repl ###

    :cljs/quit

### So, to test everything from the cli ###

Run `lein run` in one terminal, and after the http server is up run `lein shadow watch app` in another.


### Testing the database ###

After starting the backend (e.g. by runnning `(go)`), in the `user` namespace run:

```clojure
(reset-db) ; recreate the whole db

(demo.db.core/create-user! {:id         1
                            :first_name "Bruce"
                            :last_name  "Wayne"
                            :email      "bruce@waynecorp.com"
                            :pass       "iamthenight"})
;; => 1

(demo.db.core/get-user {:id 1})
;; => {:id "1",
;;     :first_name "Bruce"
;;     :last_name "Wayne"
;;     :email "bruce@waynecorp.com"
;;     :admin nil,
;;     :last_login nil,
;;     :is_active nil,
;;     :pass "iamthenight"}

```
