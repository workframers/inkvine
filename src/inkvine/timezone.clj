(ns inkvine.timezone
  (:require [java-time :as jt]
            [clojure.string :as string]))

(defn zone-id-to-enum
  "Given a timezone ID such as America/Los_Angeles, return a version of it which can be used
  as a lacinia Enum identifier."
  [z]
  (-> z
      (string/replace #"\+(\d+)", "_PLUS_$1")               ; "GMT-2"
      (string/replace #"\-(\d+)", "_MINUS_$1")
      (string/replace #"[^\p{Alnum}]+", "_")
      string/upper-case
      keyword))

;; TODO: should probably memoize bits of this

(defn enum-to-timezones []
  (let [zones (sort (jt/available-zone-ids))
        enums (map zone-id-to-enum zones)
        info  (map (fn [tz] {::name tz ::zone (jt/zone-id tz)}) zones)]
    (zipmap enums info)))

(defn timezone-enums
  [zone-map]
  (let [;zone-map (enum-to-timezones)
        values (for [enum (-> zone-map keys sort)
                     :let [tz-name (get-in zone-map [enum ::name])
                           descr   (format "Timezone ID `%s`" tz-name)]]
                 {:enum-value enum :description descr})]
    {:description "A timezone identifier"
     :values      values}))

(defn attach-timezone-enums [schema options]
  (let [enum-name (:inkvine/timezone-enum-name options)]
    (assoc-in schema [:enums enum-name] (timezone-enums (enum-to-timezones)))))
