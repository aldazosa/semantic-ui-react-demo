(defproject demo "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[ch.qos.logback/logback-classic "1.2.3"]
                 [cheshire "5.8.1"]
                 [cljs-ajax "0.8.0"]
                 [clojure.java-time "0.3.2"]
                 [com.cognitect/transit-clj "0.8.313"]
                 [com.google.javascript/closure-compiler-unshaded "v20190618" :scope "provided"]
                 [cprop "0.1.14"]
                 [day8.re-frame/http-fx "0.1.6"]
                 [expound "0.7.2"]
                 [funcool/struct "1.4.0"]
                 [luminus-jetty "0.1.7"]
                 [luminus-transit "0.1.1"]
                 [luminus/ring-ttl-session "0.3.3"]
                 [markdown-clj "1.10.0"]
                 [metosin/muuntaja "0.6.4"]
                 [metosin/reitit "0.3.9"]
                 [metosin/ring-http-response "0.9.1"]
                 [mount "0.1.16"]
                 [nrepl "0.6.0"]
                 [org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojure/core.async "0.4.500"]
                 [org.clojure/google-closure-library "0.0-20190213-2033d5d9" :scope "provided"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.webjars.npm/bulma "0.7.5"]
                 [org.webjars.npm/material-icons "0.3.0"]
                 [org.webjars/webjars-locator "0.36"]
                 [re-frame "0.10.8"]
                 [reagent "0.8.1"]
                 [ring-webjars "0.2.0"]
                 [ring/ring-core "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [selmer "1.12.14"]
                 [thheller/shadow-cljs "2.8.52" :scope "provided"]]

  :min-lein-version "2.0.0"

  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "target/cljsbuild"]
  :target-path "target/%s/"
  :main ^:skip-aot demo.core

  :plugins [[lein-shadow "0.1.5"]]
  :clean-targets ^{:protect false}
  [:target-path "target/cljsbuild"]
  :shadow-cljs
  {:nrepl {:port 7002}
   :builds
   {:app
    {:target :browser
     :output-dir "target/cljsbuild/public/js"
     :asset-path "/js"
     :modules {:app {:entries [demo.app]}}
     :devtools
     {:watch-dir "resources/public"
      :preloads  [day8.re-frame-10x.preload]}
     :dev
     {:closure-defines {"re_frame.trace.trace_enabled_QMARK_"        true
                        "day8.re_frame.tracing.trace_enabled_QMARK_" true}}
     :middleware
     [cider.nrepl/wrap-apropos
      cider.nrepl/wrap-classpath
      cider.nrepl/wrap-clojuredocs
      cider.nrepl/wrap-complete
      cider.nrepl/wrap-debug
      cider.nrepl/wrap-format
      cider.nrepl/wrap-info
      cider.nrepl/wrap-inspect
      cider.nrepl/wrap-macroexpand
      cider.nrepl/wrap-ns
      cider.nrepl/wrap-spec
      cider.nrepl/wrap-profile
      cider.nrepl/wrap-refresh
      cider.nrepl/wrap-resource
      cider.nrepl/wrap-stacktrace
      cider.nrepl/wrap-test
      cider.nrepl/wrap-trace
      cider.nrepl/wrap-out
      cider.nrepl/wrap-undef
      cider.nrepl/wrap-version
      cider.nrepl/wrap-xref]}
    :test
    {:target :node-test
     :output-to "target/test/test.js"
     :autorun true}}}

  :npm-deps [[semantic-ui-react "0.88.0"]
             [shadow-cljs "2.8.52"]
             [create-react-class "15.6.3"]
             [react "16.8.6"]
             [react-dom "16.8.6"]]

  :profiles
  {:uberjar {:omit-source true
             :prep-tasks ["compile" ["shadow" "release" "app"]]

             :aot :all
             :uberjar-name "demo.jar"
             :source-paths ["env/prod/clj" "env/prod/cljs"]
             :resource-paths ["env/prod/resources"]}

   :dev           [:project/dev :profiles/dev]
   :test          [:project/dev :project/test :profiles/test]

   :project/dev  {:jvm-opts     ["-Dconf=dev-config.edn"]
                  :npm-deps     [[react-flip-move "^3.0.2"]
                                 [react-highlight.js "^1.0.7"]]
                  :dependencies [[binaryage/devtools "0.9.10"]
                                 [cider/piggieback "0.4.1"]
                                 [pjstadig/humane-test-output "0.9.0"]
                                 [prone "2019-07-08"]
                                 [day8.re-frame/re-frame-10x "0.3.6-react16"]
                                 [day8.re-frame/tracing "0.5.1"]
                                 [ring/ring-devel "1.7.1"]
                                 [ring/ring-mock "0.4.0"]]
                  :plugins      [[com.jakemccrary/lein-test-refresh "0.24.1"]]
                  :source-paths   ["env/dev/clj" "env/dev/cljs" "test/cljs"]
                  :resource-paths ["env/dev/resources"]
                  :repl-options   {:init-ns user
                                   :nrepl-middleware
                                   [shadow.cljs.devtools.server.nrepl04/middleware]}
                  :injections     [(require 'pjstadig.humane-test-output)
                                   (pjstadig.humane-test-output/activate!)]}
   :project/test {:jvm-opts ["-Dconf=test-config.edn"]
                  :resource-paths ["env/test/resources"]}
   :profiles/dev {}
   :profiles/test {}})
