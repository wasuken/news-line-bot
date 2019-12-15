(ns news-line-bot.core
  (:gen-class)
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [bidi.ring :refer [make-handler]]
            [camel-snake-kebab.core :refer [->kebab-case ->snake_case]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [ring.util.response :as response]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]
            [environ.core :refer [env]]
            [news-line-bot.line.logic :refer [line-callback]]))

(defn wrap-kebab-case-keys [handler]
  (fn [request]
    (let [response (-> request
                       (update :params (partial transform-keys #(->kebab-case % :separator \_)))
                       handler)]
      (transform-keys #(->snake_case % :separator \-) response))))

(defmethod ig/init-key ::routes [_ _]
  ["/" {"callback" {:post line-callback}
        }
   ])

(defmethod ig/init-key ::app [_ {:keys [routes]}]
  (-> (make-handler routes)
      wrap-kebab-case-keys
      wrap-keyword-params
      wrap-json-params
      wrap-json-response
      wrap-params))

;;; API server

(defmethod ig/halt-key! ::server [_ server]
  (.stop server))

(defmethod ig/init-key ::server [_ {:keys [app options]}]
  (jetty/run-jetty app options))

;;; system configuration

(def config
  {::routes {}
   ::app {:routes (ig/ref ::routes)}
   ::server {:app (ig/ref ::app)
             :options {:port (or (env :port) 3000)
                       :join? false}}})

;;; main entry point

(defn -main [& args]
  (ig/init config))
