(ns inkvine.core
  (:require [inkvine.resolvers :as resolvers]
            [inkvine.scalars :as scalars]
            [inkvine.timezone :as timezone]
            [com.walmartlabs.lacinia.util :as util]
            [clojure.tools.logging :as log]))

(def default-options
  {:inkvine/object-name        :InkvineDateTime
   :inkvine/scalar-name        :JavaOffsetDateTime
   :inkvine/timezone-enum-name :TimezoneId})

(defn decorate-resolver-map
  ([resolver-map]
   (decorate-resolver-map resolver-map nil))
  ([resolver-map options]
   (merge resolver-map (resolvers/resolver-map options))))

(defn decorate [schema options]
  (let [with-defaults (merge default-options options)]
    (-> schema
        (resolvers/attach-resolvers with-defaults)
        (timezone/attach-timezone-enums with-defaults)
        (scalars/attach-scalars with-defaults))))
