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

(def base-schema
  {:queries
   {:now
    {:type    (:inkvine/object-name inkvine/default-options)
     :args    {}
     :resolve :inkvine-test/now-obj}
    :nowScalar
    {:type    (:inkvine/scalar-name inkvine/default-options)
     :args    {}
     :resolve :inkvine-test/now-scalar}}})

(defn now-obj-resolver [c a v]
  (resolve/resolve-as (jt/offset-date-time)))

(defn now-scalar-resolver [c a v]
  (jt/offset-date-time))

(def resolver-map
  {:inkvine-test/now-obj    now-obj-resolver
   :inkvine-test/now-scalar now-scalar-resolver})

(defn setup-schema []
  (-> base-schema
      (inkvine/decorate {})
      (util/attach-resolvers (inkvine/decorate-resolver-map resolver-map))
      (schema/compile)))

(defn execute [schema query-name]
  (lacinia/execute schema (get-query query-name) nil nil))

(deftest objects
  (testing "Send and receive Date scalars"
    (let [schema (setup-schema)
          result (execute schema :basic)]
      ;(log/spy (inkvine/decorate-resolver-map resolver-map))
      ;(log/spy (-> base-schema (inkvine/decorate {})))
      (log/spy result))))

