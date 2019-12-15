(ns news-line-bot.line.logic
  (:gen-class)
  (:require [environ.core :refer [env]]
            [ring.util.response :as response])
  (:import [com.linecorp.bot.client LineMessagingClient]
            [com.linecorp.bot.model ReplyMessage]))

(def line-bot-client
  (.build (LineMessagingClient/builder (env :line-channel-token))))


(defn process-event [event-json]
  (let [reply-token (:replyToken event-json)]
    (cond (= (:type event-json) "message")
          (let [message-text (:text (:message event-json))]
            (.replyMessage line-bot-client (ReplyMessage. reply-token "test"))))))

(defn line-callback [body-json headers]
  (println body-json)
  (println headers)
  (let [xsig (get headers "X-Line-Signature")]
    (println xsig))
  (doall (map process-event (:events body-json))))
