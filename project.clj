(defproject news-line-bot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [environ "1.1.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler news-line-bot.core/app}
  :target-path "target/%s"
  :profiles {:production {:env {:production true}}})
