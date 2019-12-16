(ns news-line-bot.news.message
  (:gen-class)
  (:require [environ.core :refer [env]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as cstr]
            [clojure.tools.logging :as log]
            [news-line-bot.const :refer [pg-db]]))



(defn create-news-list-text
  ([] (create-news-list-text "created_at"))
  ([sort-key] (create-news-list-text sort-key "desc"))
  ([sort-key order] (create-news-list-text sort-key order 10))
  ([sort-key order limit]
   (try
     (doall (cstr/join (map (fn [x] (str (subs (:title x) 0 (min (count (:title x "none")) 40))
                                          (newline)
                                          (:url x "none")))
                          (jdbc/query pg-db [(str "select * from posts order by "
                                                   (cstr/escape sort-key)　 " "
                                                   order
                                                   " limit "　
                                                   (cstr/escape (str limit)))]))
                       (newline)))
     (catch Exception e (let [err-msg (str "caught exception: " (.getMessage e))]
                          (log/debug err-msg)
                          "記事の取得中にエラーが発生しました。")))))
