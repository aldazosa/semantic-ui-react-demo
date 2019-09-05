(ns demo.core
  (:require
    [reagent.core :as r]
    [re-frame.core :as rf]))

(defn page []
  [:div
   [:p "Hola mundo"]])

;; -------------------------
;; Initialize app
(defn ^:dev/after-load mount-components []
  (rf/clear-subscription-cache!)
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  ;; (rf/dispatch-sync ,,,)
  (mount-components))
