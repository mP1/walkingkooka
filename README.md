[![Build Status](https://travis-ci.com/mP1/walkingkooka.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# Basic Project

A very brief overview of some larger more powerful features and the small are available in this and child projects, 
that do much much more leveraging the basic fundamentals present in this library. One example of the utility is presented
below.

## [ToStringBuilder](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/ToStringBuilder.java)

Yet another toString builder, that actually produces significantly more readable output when compared to Guava and
Apache Common equivalents.

It includes smarts with sensible defaults which can be turned on or off, to escape strings,
skill defaults such as null and zero to produce better toString representations for viewing in logs, debugging and more.
`ToStringBuilder` is used in a lot of toString implementations to make pretty representations.

Click [here](ToStringBuilder.md) for samples and a summary of features and goals.

## Design
A one line summary of design and implementation is that everything is immutable, functional with careful checks at all
boundaries.

Click [here](Design.md) to read about the opinions on design and java use in the system.

## Dependencies

Only junit!

## Getting the source

You can either download the source using the "ZIP" button at the top
of the github page, or you can make a clone using git:

```
git clone git://github.com/mP1/walkingkooka.git
```
