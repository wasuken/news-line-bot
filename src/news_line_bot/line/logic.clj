(ns news-line-bot.line.logic
  (:gen-class)
  (:require [environ.core :refer [env]]
            [ring.util.response :as response]))

(defn line-callback [request]
  (response/response (str request)))
