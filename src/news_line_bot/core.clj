(ns news-line-bot.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.util.request :refer [body-string]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [news-line-bot.line.logic :refer [line-callback]]
            [clojure.tools.logging :as log]))

(defroutes app-routes
  (POST "/callback" req (line-callback (:body-text req) (:headers req)))
  (route/not-found "<h1>Page not found</h1>"))

(defn assoc-in-all-value [m value & paths]
  (reduce (fn [m path] (assoc-in m path true))
          m
          paths))

(defn wrap-body-string [handler]
  (fn [request]
    (let [body-str (ring.util.request/body-string request)]
      (handler (assoc request :body-text body-str)))))

(def app
  (wrap-body-string (wrap-defaults app-routes (assoc-in-all-value
                             (assoc-in site-defaults
                                       [:security :anti-forgery] false)
                             true
                             [:params :multipart]
                             [:params :nested]))))
