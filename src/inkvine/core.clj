(ns inkvine.core
  (:require [inkvine.resolvers :as resolvers]
            [inkvine.scalars :as scalars]
            [inkvine.queries :as queries]
            [inkvine.timezone :as timezone]
            [com.walmartlabs.lacinia.util :as util]
            [clojure.tools.logging :as log]))

(def default-options
  {:inkvine/object-name        :InkvineDateTime
   :inkvine/scalar-name        :JavaOffsetDateTime
   :inkvine/timezone-enum-name :TimezoneId
   :inkvine/now-query-name     :inkvine_now_utc
   :inkvine/tz-query-name      :inkvine_timezone})

(defn decorate-resolver-map
  ([resolver-map]
   (decorate-resolver-map resolver-map nil))
  ([resolver-map options]
   (let [with-defaults (merge default-options options)]
     (merge resolver-map (resolvers/resolver-map with-defaults)
                         (queries/resolver-map with-defaults)))))

(defn decorate-schema
  ([schema]
   (decorate-schema schema nil))
  ([schema options]
   (let [with-defaults (merge default-options options)]
     (-> schema
         (resolvers/assoc-resolvers with-defaults)
         (queries/assoc-queries with-defaults)
         (timezone/assoc-timezone-enums with-defaults)
         (scalars/assoc-scalars with-defaults)))))
