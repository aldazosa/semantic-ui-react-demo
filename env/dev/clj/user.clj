(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
   [demo.config :refer [env]]
   [clojure.spec.alpha :as s]
   [expound.alpha :as expound]
   [mount.core :as mount]
   [demo.core :refer [start-app]]
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


(defn go []
  (restart)
  (server/stop!)
  (server/start!)
  (shadow/watch :app))
