(ns inkvine.scalars
  (:require [com.walmartlabs.lacinia.schema :as schema]
            [java-time :as jt]))

(defn scalar-spec []
  {:parse       (schema/as-conformer #(jt/offset-date-time %))
   :serialize   (schema/as-conformer #(.toString %))
   :description "A java.time.OffSetDateTime object, serialized as a string"})

(defn assoc-scalars [schema {:keys [:inkvine/scalar-name] :as options}]
  (assoc-in schema [:scalars scalar-name] (scalar-spec)))
