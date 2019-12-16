(ns news-line-bot.const
  (:gen-class)
  (:require [environ.core :refer [env]]))

(def pg-db {:dbtype (env :db-dbtype)
            :dbname (env :db-dbname)
            :host (env :db-host)
            :port (env :db-port)
            :user (env :db-user)
            :password (env :db-password)
            :ssl true
            :sslfactory "org.postgresql.ssl.NonValidatingFactory"})
