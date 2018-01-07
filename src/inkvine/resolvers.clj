(ns inkvine.resolvers
  (:require [java-time :as jt]
            [camel-snake-kebab.core :as csk]
            [clojure.string :as string]
            [clojure.tools.logging :as log]))

(defn inkvine-object-schema
  [options]
  {:description
   "An object representing a java.time instance"
   :fields
   {:toString
    {:type        'String
     :resolver    :inkvine/to-string
     :description "ISO-8601 format of the date"}
    :epoch
    {:type        'Int
     :resolver    :inkvine/epoch
     :description "Seconds since the epoch"}}})

(defn to-string [c a v]
  (.toString v))

(defn epoch [c a v]
  (log/spy [a v])
  nil)

(defn resolver-map [options]
  {:inkvine/to-string to-string
   :inkvine/epoch     epoch})

(defn attach-resolvers [schema {:keys [:inkvine/object-name] :as options}]
  (assoc-in schema [:objects object-name] (inkvine-object-schema options)))
