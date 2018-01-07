`inkvine` is a library for dealing with Java DateTime objects from
[lacinia](https://github.com/walmartlabs/lacinia), with the goal of making it easier
to use date objects in GraphQL.

## Installation

Add inkvine to your dependencies:

`[com.workframe/inkvine "0.1.0"]`

## Usage

The main inkvine API consists of two routines that you apply to your schema and resolver
map prior to compilation. Between them, these routines will add a new object type
`:InkvineDate` to your schema, and a set of resolvers that implement various fields
and methods corresponding to the underlying `java.time.OffsetDateTime` objects.

```clojure
(ns my-ns
  (:require [inkvine.core :as inkvine])

```

#### Why "inkvine"?

In the _Dune_ novels, inkvine is used as a punishment device which leaves permanent
marks on its recipients. This is similar to working with time and date APIs in Java.

> He looked at the beet-colored inkvine scar on the man's jaw, remembering the story
  of how it had been put there.

-- Frank Herbert, _Dune_
