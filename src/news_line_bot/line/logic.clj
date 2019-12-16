(ns news-line-bot.line.logic
  (:gen-class)
  (:require [environ.core :refer [env]]
            [ring.util.response :as response]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [news-line-bot.news.message :refer [create-news-list-text]])
  (:import [com.linecorp.bot.client LineMessagingClient LineSignatureValidator]
           [com.linecorp.bot.model ReplyMessage]
           [com.linecorp.bot.model.message TextMessage]
           [com.linecorp.bot.model.event FollowEvent MessageEvent Event]))

(def line-bot-client
  (.build (LineMessagingClient/builder (env :line-channel-token))))

(defn reply-message [reply-token msg]
  (let [resp (.get (.replyMessage line-bot-client (ReplyMessage. reply-token msg)))]
    (log/debug "***********************")
    (log/debug (.getRequestId resp))
    (log/debug (.getMessage resp))
    (log/debug (.getDetails resp))))

(defn process-event [event]
  (let [reply-token (get event "replyToken")
        type (get event "type")
        message (get event "message")
        message-type (get message "type")]
    (log/debug "=====================")
    (log/debug type)
    (log/debug message)
    (log/debug (get message "text"))
    (cond (= type "follow")
          (reply-message (TextMessage. "フォローありがとうございます。"))
          (= type "message")
          (if (not (clojure.string/blank? (get message "text")))
            (cond (re-find (re-matcher #"news|ニュース|list" (get message "text")))
                  (reply-message (TextMessage. (create-news-list-text)))))
          :else (log/debug (str "not suppoert type: " type)))))

(defn response->check [body x-line-sig]
  (.validateSignature (LineSignatureValidator.
                                       (.getBytes (env :line-channel-token)))
                                      (.getBytes body)
                                      x-line-sig))

(defn line-callback [body headers]
  (cond (response->check body (get headers "x-line-signature" ""))
        (doall (map process-event (get (json/read-str body) "events")))))
