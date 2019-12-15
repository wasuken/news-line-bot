(ns news-line-bot.core
  (:gen-class)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [news-line-bot.line.logic :refer [line-callback]]))

(defroutes app-routes
  (POST "/callback" req (let [req-str (str req)]
                          (println req-str)
                          req-str))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
