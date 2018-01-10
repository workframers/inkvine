(ns inkvine.queries
  (:require [java-time :as jt]
            [com.walmartlabs.lacinia.resolve :as resolve]
            [clojure.tools.logging :as log])
  (:import (com.walmartlabs.lacinia.resolve ResolverResult)
           (java.time ZoneOffset)))

(defn inkvine-now-query-schema
  [{:keys [:inkvine/object-name :inkvine/now-query-name]}]
  {:type        object-name
   :args        {}
   :resolve     now-query-name
   :description "Return the current time."})

(def ^ResolverResult inkvine-now-utc-query-resolver
  (fn inkvine-now-query-resolver-fn
    [c a v]
    (resolve/resolve-as
      (jt/offset-date-time ZoneOffset/UTC))))

(defn inkvine-tz-query-schema
  [{:keys [:inkvine/timezone-enum-name :inkvine/tz-query-name]}]
  {:type        timezone-enum-name
   :args        {:name {:type '(non-null String)
                        :description "String version of the timezone, ie `America/New_York`"}}
   :resolve     tz-query-name
   :description "Return the current time."})

(def ^ResolverResult inkvine-now-tz-resolver
  (fn inkvine-now-tz-resolver-fn
    [c a v]
    (resolve/resolve-as
      (jt/offset-date-time ZoneOffset/UTC))))

(defn resolver-map [{:keys [:inkvine/now-query-name :inkvine/tz-query-name]}]
  {now-query-name inkvine-now-utc-query-resolver
   tz-query-name  inkvine-now-tz-resolver})

(defn assoc-queries [schema options]
  (let [{:keys [:inkvine/now-query-name :inkvine/tz-query-name]} options]
    (-> schema
        (assoc-in [:queries now-query-name] (inkvine-now-query-schema options))
        (assoc-in [:queries tz-query-name] (inkvine-tz-query-schema options)))))
