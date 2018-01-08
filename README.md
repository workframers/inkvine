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

In addition, inkvine will add a few root-level queries which can be used for various
date and time operations, such as getting the current date or parsing and
manipulating an existing one, and an `enum` interface representing `tz` codes
for timezones.

Internally, inkvine largely operates on UTC `OffsetDateTime` objects; therefore
most operations which produce a date as output will need to be told which
timezone to display the date in.

```clojure
(ns my-ns
  (:require [inkvine.core :as inkvine])

```

### Names

Because inkvine is adding stuff to your schema, most of its top-level names
include "inkvine" in them for better namespacing. You can change the names
of all of the top-level names it generates by setting keys in the `options`
map passed to the `(decorate)` and `(decorate-resolver)` functions.

### Object Types

inkvine creates a single top-level `:object` type, `:InkvineDateTime`.

### Enums

inkvine creates a single Enum. By default this is called `:TimezoneId` but
you can update its name via the `:inkvine/timezone-enum-name` options.

The enum has entries for all the timezones returned from
[`(java.time.ZoneId/getAvailableZoneIds)`](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html#getAvailableZoneIds--).
The names are translated from half-heartedly formatted strings to
SCREAMING_SNAKE_CASE enum values, so AMERICA_NEW_YORK and so on.

### Queries

* `:inkvine_now_utc`

#### Why "inkvine"?

In the _Dune_ novels, inkvine is used as a punishment device which leaves permanent
marks on its recipients. This process is reminiscent of working with time and date
APIs in Java.

> He looked at the beet-colored inkvine scar on the man's jaw, remembering the story
  of how it had been put there.

-- Frank Herbert, _Dune_
