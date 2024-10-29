[![Build Status](https://github.com/mP1/walkingkooka/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



# Basic Project

A very brief overview of some larger more powerful features and the small are available in this and child projects, 
that do much much more leveraging the basic fundamentals present in this library. One example of the utility is presented
below. Most if not all values and abstractions are immutable, functional and provide helpers to assist in creation of implementations.


## [*.collect](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/collect)

- A collection of `List`, `Set`, `Map`, and `Stack` abstractions, may of which are immutable.
- `ImmutableList` an immutable `List` with would be mutator methods such as concat, replace and more.
- `ImmutableListDefaults` an `ImmutableList` implementation leaving only a single method to be implemented (aka equivalent of `AbstractList` for `List`).
- Many additional adaptors missing from the JDK
- Many factory methods are available for the various implementations.
- Factory methods are available that provide a Concurrent implementations in a JRE but a regular when translated to javascript, which is single threaded anyway.

## [*.comparator]

- NullAwareAfterComparator - wraps another comparator, so nulls sort *AFTER* non null sorted values.
- NullAwareBeforeComparator - wraps another comparator, so nulls sort *BEFORE* non null sorted values.

## [*.predicate](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate)

- Many additional `Predicate`(s).



## [*.predicate.character](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate/character)

- A new interface `CharPredicate` replacing the need for `IntPredicate`.
- Many useful and interesting implementations.


## [*.reflect](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/reflect)

-`ClassVisitor` advanced visitor over different members of a single class and its hierarchy. Useful to visit class members, super classes and similar.
-`JavaVisibility` object oriented abstraction of class and member visibilities with numerous useful operations.
- New functional abstractions for many JDK class components, such as `ClassName`, `FieldName`, `MethodName` replacing the need for `String` for said names in some APIS.
- Abstractions like `ClassAttributes`, `FieldAttributes`, `MethodAttributes` which provide oo methods rather than using bit patterns.
- Several interfaces with defender methods (think mixin) that assist with testing class structure and members.



## [*.stream.push](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/stream/push)

- A Stream implementation that sources Nodes from a Consumer.



## [*.text](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text)

- CaseSensitivity Useful case sensitivity String to CharSequences, like startsWith, endsWidth, index, equals
- CharSequences Supports many of the useful String methods and more for CharSequence using a CaseSensitivity
- [GlobPattern](https://en.wikipedia.org/wiki/Glob_\(programming\)) May be used to match glob patterns where question mark matches a single char, and star zero or more, eg /user/Miroslav/*.txt (all text files under) or /user/Miroslav/???.txt (three letter txt files)



## [*.text.cursor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor)

- `TextCursor` provides a cursor that may be used to advance over text, one character at a time. 
- `TextCursorSavePoint` Multiple save points may be created and used to reset the `TextCursor` at any time.
- `TextCursorLineInfo` Describe the column, line position and more of the `TextCursor` within its text.


## [*.util](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/util)

- Collection of misc interfaces and useful abstractions.



## [Context](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/Context.java)

Goals and problems attempting to solve:
- A Context typically accompanies one or more other values which need some task to be performed.
- The JDK typically mixes user context such as a `Locale` along with the `NumberFormat` and its symbols.
- In this design the compiled pattern separate from the user context `Locale`, which means customisation of the `Locale` and similar symbols can be done on at the request level keeping the original parser or formatter unmodified.
- This means that all parsers/formatters and similar workers are immutable, functional and threadsafe, unlike `DateFormat` and other JRE classes.

Numerous projects provide specialised `Context` which add properties and methods to assist a task, eg the users `Locale` or number formatting symbols. Some examples include:
- [DateTimeContext](https://github.com/mP1/walkingkooka-datetime/blob/master/src/main/java/walkingkooka/datetime/DateTimeContext.java) symbols, locale, month names and more. Simple another parameter accompanying the `LocalDate`.
- [DecimalNumberContext](https://github.com/mP1/walkingkooka-math/blob/master/src/main/java/walkingkooka/math/DecimalNumberContext.java) symbols, locale, grouping separator count and more. Simple another parameter accompanying the `Number`.



## [Either](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/Either.java)

- `Either` is immutable and holds either of two values, but never none or both.
- Numerous functional methods (map etc) are available to operate on either value.



## [ToStringBuilder](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/ToStringBuilder.java)

A better `Object#toString` builder that actually produces significantly more readable output when compared to Guava and Apache Common equivalents.

- Automatic quoting and escaping of `char` (within single quotes) and `CharSequences` (within double quotes) even when within collections.
- Skip default values for primitives and Strings (eg skip properties that are boolean=true, numeric=0 etc)
- Assign labels with values, labels can be skipped if the value is skipped.
- Support for hex representation of bytes
- All the above can be enabled or disabled.
- Customizable separators for non scalar values (arrays, collections etc)
- See [Public API](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/ToStringBuilder.java).
- See [Unit tests](https://github.com/mP1/walkingkooka/tree/master/src/test/java/walkingkooka) for more examples of usage and the product.



