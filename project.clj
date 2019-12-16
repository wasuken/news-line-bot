(defproject news-line-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.7.10"]
                 [org.postgresql/postgresql "9.4-1205-jdbc42"]
                 [environ "1.1.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [org.clojure/data.json "0.2.7"]
                 [ring/ring-core "1.8.0"]
                 [com.linecorp.bot/line-bot-api-client "3.2.0"]
                 [com.linecorp.bot/line-bot-model "3.2.0"]
                 [org.slf4j/slf4j-log4j12 "1.7.9"]
                 [org.slf4j/slf4j-api "1.7.0"]
                 [org.clojure/tools.logging "0.5.0"]
                 [org.clojure/data.codec "0.1.1"]]
  :min-lein-version "2.0.0"
  :uberjar-name "news-line-bot.jar"
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler news-line-bot.core/app}
  :target-path "target/%s"
  :profiles {:production {:env {:production true}}})
