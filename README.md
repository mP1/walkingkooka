[![Build Status](https://github.com/mP1/walkingkooka/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



# Basic Project

A very brief overview of some larger more powerful features and the small are available in this and child projects, 
that do much much more leveraging the basic fundamentals present in this library. One example of the utility is presented
below. Most if not all values and abstractions are immutable, functional and provide helpers to assist in creation of implementations.


## [*.collect](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/collect)

- A collection of `List`, `Set`, `Map`, and `Stack` abstractions, may of which are immutable.
- Many additional adaptors missing from the JDK
- Many factory methods are available for the various implementations.
- For j2cl, these factory methods provide `Concurrent` replacements missing the supplied emulated JRE.



## [*.predicate](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate)

- Many additional `Predicate`(s).



## [*.predicate.character](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate/character)

- A new interface `CharPredicate` replacing the need for `IntPredicate`.
- Many useful and interesting implementations.


## [*.reflect](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/reflect)

- `ClassVisitor` advanced visitor over different members of a single class and its hierarchy.
- `JavaVisibility` Object oriented abstraction of class and member visibilities with numerous useful operations.
- New functional abstractions for many JDK class components, such as `Class`, `Field`, `Method` and more...
- Several interfaces with defender methods (think mixin) that assist with testing class structure and members.



## [*.stream.push](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/stream/push)

- Brings `Stream` support from a `Consumer`.



## [*.text](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text)

- `CaseSensitivity` Useful case sensitivity operations as useful as a parameter.
- `CharSequences` Supports many of the useful `String` methods and more for `CharSequence`.
- [GlobPattern](https://en.wikipedia.org/wiki/Glob_\(programming\)) May be used to match glob patterns where question mark matches a single char, and star zero or more, eg /user/Miroslav/*.txt (all text files under) or /user/Miroslav/???.txt (three letter txt files)



## [*.text.cursor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor)

- `TextCursor` provides a cursor that may be used to advance over text, one character at a time. 
- `TextCursorSavePoint` Multiple save points may be created and used to reset the `TextCursor` at any time.
- `TextCursorLineInfo` Describe the column, line position and more of the `TextCursor` within its text.


## [*.util](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/util)

- Collection of misc interfaces and useful abstractions.



## [Context](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/Context.java)

Goals and problems attempting to solve:

- A `Context` typically accompanies one or more other values which need some task to be performed.
- JDK `NumberFormat` classes mix pattern with user locale settings `NumberFormatSymbol` which requires new instances in a multi-user environment or when settings changed.
- JDK `DateFormat` include a `DateFormatSymbols`, `DateTimeFormatter` include numerous pattern customisations such as two-digit-year, Locale.
- Complex assemblies of collaborators eg [Parser](https://github.com/mP1/walkingkooka-text-cursor-parser/blob/master/src/main/java/walkingkooka/text/cursor/parser/Parser.java) 
  only including patterns for formatting and parsing do not need to change when user settings change, just pass in another `Context`.
  


Numerous projects provide specialised `Context` which add properties and methods to assist a task, eg the users `Locale` or number formatting symbols. Some examples include:
- [DateTimeContext](https://github.com/mP1/walkingkooka-datetime/blob/master/src/main/java/walkingkooka/datetime/DateTimeContext.java) symbols, locale, month names and more. Simple another parameter accompanying the `LocalDate`.
- [DecimalNumberContext](https://github.com/mP1/walkingkooka-math/blob/master/src/main/java/walkingkooka/math/DecimalNumberContext.java) symbols, locale, grouping separator count and more. Simple another parameter accompanying the `Number`.



## [Either](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/Either.java)

- Either is immutable and holds two values.
- Numerous functional methods (map etc) are available to operate on either value.



## [ToStringBuilder](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/ToStringBuilder.java)

Yet another toString builder, that actually produces significantly more readable output when compared to Guava and
Apache Common equivalents.

It includes smarts with sensible defaults which can be turned on or off, to escape strings,
skill defaults such as null and zero to produce better toString representations for viewing in logs, debugging and more.
`ToStringBuilder` is used in a lot of toString implementations to make pretty representations.

Click [here](ToStringBuilder.md) for samples and a summary of features and goals.



