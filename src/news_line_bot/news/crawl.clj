(ns news-line-bot.news.crawl
  (:gen-class)
  (:require [environ.core :refer [env]]
            [feedme :as fm]
            [clojure.tools.logging :as log]
            [clojure.java.jdbc :as jdbc]
            [news-line-bot.const :refer [pg-db]]))

;;; Create them individually and share them if they are the same to some extent.
(defn hatena-crawl-data->db [url]
  (Thread/sleep 3000)
  (let [from-url url
        insert-list (doall (map (fn [x] ()
                                  {:title (:title x)
                                   :from_url from-url
                                   :url (:link x)
                                   :description (:content x)
                                   })
                                (:entries (fm/parse from-url))))
        url-list (doall (map (fn [x] (:url x))
                             (jdbc/query pg-db ["select url from posts"])))]
    (try
      (jdbc/insert-multi! pg-db :posts
                          (remove (fn [x] (some (fn [y] (= (:url x) y)) url-list))
                                  insert-list))
      (catch Exception e (log/debug (.getMessage e))))
    ))

(defn links->db [links]
  (try
    (jdbc/insert-multi! pg-db :sites
                        links)
    (catch Exception e (log/debug (.getMessage e)))))

(defn -main
  [x] (cond (= x "crawl")
            (doall (map hatena-crawl-data->db
                        (map (fn [x] (get x :url))
                             (jdbc/query pg-db ["select url from sites"]))))
            (= x "add-links")
            (links->db '())))
