(defproject com.workframe/inkvine "0.1.0-SNAPSHOT"
  :description "Library for accessing java.time objects in lacinia"
  :url "https://github.com/workframers/stillsuit"
  :pedantic? :warn
  :license {:name "EPL"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [com.walmartlabs/lacinia "0.24.0-rc-2"]
                 [clojure.java-time "0.3.1"]
                 [org.clojure/tools.logging "0.4.0"]]

  :source-paths ["src"]

  :test-selectors {:watch :watch}

  :profiles {:dev  {:plugins      [[lein-ancient "0.6.15"]
                                   [com.jakemccrary/lein-test-refresh "0.22.0"]]
                    :dependencies [[io.forward/yaml "1.0.6"]
                                   [org.apache.logging.log4j/log4j-core "2.10.0"]
                                   [org.apache.logging.log4j/log4j-slf4j-impl "2.10.0"]]}

             :test {:resource-paths ["test/resources"]}})
