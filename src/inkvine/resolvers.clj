(ns inkvine.resolvers
  (:require [java-time :as jt]
            [camel-snake-kebab.core :as csk]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [com.walmartlabs.lacinia.resolve :as resolve])
  (:import (java.time OffsetDateTime)
           (com.walmartlabs.lacinia.resolve ResolverResult)))

(defn inkvine-object-schema
  [options]
  {:description
   "An object representing a java.time instance"
   :fields
   {:toString
    {:type        '(non-null String)
     :resolve     :inkvine/to-string
     :description "ISO-8601 format of the date"}
    :epoch
    {:type        '(non-null String)
     :resolve     :inkvine/epoch
     :description "Seconds since the epoch. Returned as a String since it is generally larger than 32 bits."}
    :format
    {:type        'String
     :resolve     :inkvine/format
     :args        {:pattern
                   {:type '(non-null String)}
                   :tz
                   {:type `(~'non-null ~(:inkvine/timezone-enum-name options))}}
     :description "Format a string in the given timezone"}}})

(defn inkvine-now-query-schema
  [{:keys [:inkvine/object-name]}]
  {:type    object-name
   :args    {}
   :resolve :inkvine-test/now-obj
   :description "Return the current time."})

(def ^ResolverResult inkvine-now-query-resolver
  (fn inkvine-now-query-resolver-fn
    [c a v]
    (resolve/resolve-as (jt/offset-date-time))))

(defn to-string [c a ^OffsetDateTime v]
  (.toString v))

(defn epoch [c a ^OffsetDateTime v]
  (-> v jt/to-millis-from-epoch str))

(defn inkvine-format [c a ^OffsetDateTime v]
  (-> v jt/to-millis-from-epoch str))

(defn resolver-map [{:keys [:inkvine/now-query-name]}]
  {:inkvine/to-string to-string
   :inkvine/epoch     epoch
   :inkvine/format    inkvine-format
   now-query-name     inkvine-now-query-resolver})

(defn assoc-resolvers [schema options]
  (let [{:keys [:inkvine/object-name :inkvine/now-query-name]} options]
    (-> schema
        (assoc-in [:objects object-name] (inkvine-object-schema options))
        (assoc-in [:queries now-query-name] (inkvine-now-query-schema options)))))
