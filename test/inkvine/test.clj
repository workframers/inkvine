(ns inkvine.test
  (:require [clojure.test :refer :all]
            [inkvine.core :as inkvine]
            [java-time :as jt]
            [clojure.tools.logging :as log]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]
            [com.walmartlabs.lacinia :as lacinia]
            [clojure.java.io :as io]
            [yaml.core :as yaml]
            [com.walmartlabs.lacinia.resolve :as resolve]))

(defn get-query [qname]
  (-> "queries.yaml"
      io/resource
      io/reader
      slurp
      yaml/parse-string
      (get-in [:queries qname])))

(def now-obj-resolver
  ^resolve/ResolverResult
  (fn now-obj [c a v]
    (resolve/resolve-as (jt/offset-date-time))))

(defn now-scalar-resolver [c a v]
  (jt/offset-date-time))

(def resolver-map
  {:inkvine-test/now-obj    now-obj-resolver
   :inkvine-test/now-scalar now-scalar-resolver})

(defn setup-schema []
  (-> {}
      (inkvine/decorate {})
      (util/attach-resolvers (inkvine/decorate-resolver-map resolver-map))
      (schema/compile)))

(defn execute [schema query-name]
  (lacinia/execute schema (get-query query-name) nil nil))

(deftest objects
  (let [schema (setup-schema)]

    (testing "Send and receive Date scalars"
      (let [result (execute schema :basic)
            data   (get-in result [:data :inkvine_now])]
        (log/spy result)
        (is (some? (:epoch data)))
        (is (some? (:toString data)))))

    (testing "Pattern")))

