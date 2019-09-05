(ns demo.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]
    ["semantic-ui-react" :refer [Image]]))

(defn page []
  [:div
   [:p "Hola mundo"]
   [:> Image {:src "/img/warning_clojure.png"}]])

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  ;; (rf/dispatch-sync ,,,)
  (mount-components))
