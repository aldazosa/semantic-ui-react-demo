(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [demo.config :refer [env]]
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [mount.core :as mount]
   [demo.core :refer [start-app]]
   [demo.db.core]
   [conman.core :as conman]
   [luminus-migrations.core :as migrations]
   ;; shadow-cljs
   [shadow.cljs.devtools.server :as server]
   [shadow.cljs.devtools.api :as cljs]
   [shadow.cljs.devtools.cli]
   [shadow.cljs.devtools.api :as shadow]))

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(defn start
  "Starts application.
  You'll usually want to run this on startup."
  []
  (mount/start-without #'demo.core/repl-server))

(defn stop
  "Stops application."
  []
  (mount/stop-except #'demo.core/repl-server))

(defn restart
  "Restarts application."
  []
  (stop)
  (start))

(defn restart-db
  "Restarts database."
  []
  (mount/stop #'demo.db.core/*db*)
  (mount/start #'demo.db.core/*db*)
  (binding [*ns* 'demo.db.core]
    (conman/bind-connection demo.db.core/*db* "sql/queries.sql")))

(defn reset-db
  "Resets database."
  []
  (migrations/migrate ["reset"]
                      (select-keys env [:database-url])))

(defn migrate
  "Migrates database up for all outstanding migrations."
  []
  (migrations/migrate ["migrate"] (select-keys env [:database-url])))

(defn rollback
  "Rollback latest database migration."
  []
  (migrations/migrate ["rollback"] (select-keys env [:database-url])))

(defn create-migration
  "Create a new up and down migration file with a generated timestamp and `name`."
  [name]
  (migrations/create name (select-keys env [:database-url])))

(defn go []
  (restart)
  (server/stop!)
  (server/start!)
  (shadow/watch :app))
