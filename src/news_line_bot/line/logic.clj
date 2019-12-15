(ns news-line-bot.line.logic
  (:gen-class)
  (:require [environ.core :refer [env]]
            [ring.util.response :as response]
            [clojure.tools.logging :as log])
  (:import [com.linecorp.bot.client LineMessagingClient]
            [com.linecorp.bot.model ReplyMessage]))

(def line-bot-client
  (.build (LineMessagingClient/builder (env :line-channel-token))))


(defn process-event [event-json]
  (let [reply-token (get event-json "replyToken")]
    (cond (= (:type event-json) "message")
          (let [message-text (get (get event-json "message") "text")]
            (log/debug "=======================")
            (log/debug (.replyMessage line-bot-client (ReplyMessage. reply-token "test")))))))

(defn line-callback [body-json headers]
  
  (log/debug body-json)
  (log/debug headers)
  (let [xsig (get headers "x-line-signature")]
    (println xsig))
  (doall (map process-event (:events body-json))))
