(ns demo.routes.services
  (:require
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [reitit.ring.coercion :as coercion]
   [reitit.coercion.spec :as spec-coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.parameters :as parameters]
   [demo.middleware.formats :as formats]
   [demo.middleware.exception :as exception]
   [ring.util.http-response :refer :all]
   [clojure.java.io :as io]
   [demo.db.core :as db]))


(defn service-routes []
  ["/api"
   {:coercion   spec-coercion/coercion
    :muuntaja   formats/instance
    :swagger    {:id ::api}
    :middleware [;; query-params & form-params
                 parameters/parameters-middleware
                 ;; content-negotiation
                 muuntaja/format-negotiate-middleware
                 ;; encoding response body
                 muuntaja/format-response-middleware
                 ;; exception handling
                 exception/exception-middleware
                 ;; decoding request body
                 muuntaja/format-request-middleware
                 ;; coercing response bodys
                 coercion/coerce-response-middleware
                 ;; coercing request parameters
                 coercion/coerce-request-middleware
                 ;; multipart
                 multipart/multipart-middleware]}

   ;; swagger documentation
   ["" {:no-doc  true
        :swagger {:info {:title       "my-api"
                         :description "https://cljdoc.org/d/metosin/reitit"}}}

    ["/swagger.json"
     {:get (swagger/create-swagger-handler)}]

    ["/api-docs/*"
     {:get (swagger-ui/create-swagger-ui-handler
             {:url    "/api/swagger.json"
              :config {:validator-url nil}})}]]

   ["/user"
    {:get {:summary    "Get a user by id"
           :parameters {:query {:user-id int?}}
           :responses  {200 {:body {:id         int?
                                    :first_name string?
                                    :last_name  string?
                                    :email      string?}}}
           :handler    (fn [{{{:keys [user-id]} :query} :parameters}]
                         (if-let [result (demo.db.core/get-user {:id user-id})]
                           {:status 200
                            :body   (-> result
                                        (dissoc :pass)
                                        (update :id #(Integer/parseInt %)))}
                           {:status 404}))}

     :post {:summary    "Create a new user"
            :parameters {:body {:id         string?
                                :first_name string?
                                :last_name  string?
                                :email      string?
                                :pass       string?}}
            :responses  {200 {:body {:id int?}}}
            :handler    (fn [{{:keys [body]} :parameters}]
                          {:status 200
                           :body {:id (demo.db.core/create-user! body)}})}}]])
