(ns inkvine.resolvers
  (:require [java-time :as jt]
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
    :epochSeconds
    {:type        '(non-null Int)
     :resolve     :inkvine/epoch-secs
     :description "True if the year of this date is a leap year."}
    :isLeapYear
    {:type        '(non-null Boolean)
     :resolve     :inkvine/leap?
     :description "Seconds since the epoch. Note that due to Int overflow, dates past 2038 will return errors."}
    :epochMilliseconds
    {:type        '(non-null String)
     :resolve     :inkvine/epoch-ms
     :description "Milliseconds since the epoch. Returned as a String since it is generally larger than 32 bits."}
    :format
    {:type        'String
     :resolve     :inkvine/format
     :args        {:pattern
                   {:type '(non-null String)
                    :description (str "An output pattern. See [the DateTimeFormatter docs]"
                                      "(https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns)"
                                      " for details.")}
                   :tz
                   {:type `(~'non-null ~(:inkvine/timezone-enum-name options))}}
     :description "Format a string in the given timezone"}}})

(defn to-string [c a ^OffsetDateTime v]
  (.toString v))

(defn epoch-resolver
  [c a ^OffsetDateTime v]
  (-> v jt/to-millis-from-epoch (/ 1000) int))

(def ^ResolverResult epoch-ms-resolver
  (fn epoch-ms-resolver-fn
    [c a ^OffsetDateTime v]
    (-> v jt/to-millis-from-epoch str)))

(def ^ResolverResult leap?-resolver
  (fn leap?-resolver-fn
    [c a ^OffsetDateTime v]
    (jt/leap? v)))

(defn inkvine-format [c a ^OffsetDateTime v]
  (-> v jt/to-millis-from-epoch (/ 1000) int))

(defn inkvine-format [c a ^OffsetDateTime v]
  (-> v jt/to-millis-from-epoch (/ 1000) int))

(defn resolver-map [{:keys [:inkvine/now-query-name]}]
  {:inkvine/to-string  to-string
   :inkvine/epoch-secs epoch-resolver
   :inkvine/leap?      leap?-resolver
   :inkvine/epoch-ms   epoch-ms-resolver
   :inkvine/format     inkvine-format})

(defn assoc-resolvers [schema options]
  (let [{:keys [:inkvine/object-name :inkvine/now-query-name]} options]
    (-> schema
        (assoc-in [:objects object-name] (inkvine-object-schema options)))))
