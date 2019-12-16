(ns news-line-bot.news.message
  (:gen-class)
  (:require [environ.core :refer [env]]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as cstr]
            [clojure.tools.logging :as log]
            [news-line-bot.const :refer [pg-db]]))

(defn my-escape [val]
  (cstr/escape val {\< "&lt;", \> "&gt;", \& "&amp;", \= "", \% "%"}))


(defn create-news-list
  ([] (create-news-list "created_at"))
  ([sort-key] (create-news-list "created_at" "desc"))
  ([sort-key order] (create-news-list "created_at" "desc" 10))
  ([sort-key order limit]
   (jdbc/query pg-db [(str "select * from posts order by "
                           (my-escape sort-key) " "
                           order
                           " limit "　
                           (my-escape (str limit)))]))
  ([sort-key order limit keyword]
   (jdbc/query pg-db [(str "select * from posts where title like ? or description like ? order by "
                           (my-escape sort-key) " "
                           order
                           " limit "
                           (my-escape (str limit))
                           (my-escape (str limit)))
                      (str "%" (my-escape keyword) "%")])))

(defn create-news-search-list-text
  ([keyword]
   (try
     (cstr/join "\n"
                (map (fn [x] (str (subs (:title x "none") 0 (min (count (:title x "none")) 40))
                                  "\n"
                                  (:url x "none")))
                     (create-news-list "created_at" "desc" 10 keyword)))
     (catch Exception e (let [err-msg (str "caught exception: " (.getMessage e))]
                          (log/debug err-msg)
                          "記事の取得中にエラーが発生しました。")))))

(defn create-news-list-text
  ([] (create-news-list-text "created_at"))
  ([sort-key] (create-news-list-text sort-key "desc"))
  ([sort-key order] (create-news-list-text sort-key order 10))
  ([sort-key order limit]
   (try
     (cstr/join "\n"
                (map (fn [x] (str (subs (:title x "none") 0 (min (count (:title x "none")) 40))
                                  "\n"
                                  (:url x "none")))
                     (create-news-list)))
     (catch Exception e (let [err-msg (str "caught exception: " (.getMessage e))]
                          (log/debug err-msg)
                          "記事の取得中にエラーが発生しました。")))))
