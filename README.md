[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka/badge.svg?branch=feature%2Fcoveralls-travis-ci-integration)](https://coveralls.io/github/mP1/walkingkooka?branch=feature%2Fcoveralls-travis-ci-integration)

# Basic Project

A very brief summary of some powerful features available in this project, with some large and small powerful items.

## [Node](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/Node.java)
A node is a member of a graph or tree of objects. Nodes come in two basic types, branches, that is nodes that can have further nodes as children, and leafs, nodes that no have no further children. All nodes have a single parent, except for the root node. Some notable examples of a node graph include json, an xml document, the file system where files and directories are no nodes.

Click [here](Node.md) for a more in depth readme about the interface and implementation with sample code.

## Lens
The dom and pojo abstractions both work as immutable lens, an proper common Lens interface needs to be extracted (TODO).

## [ToStringBuilder](https://github.com/mP1/walkingkooka/blob/master/src/walkingkooka/build/tostring/ToStringBuilder)

Yet another toString builder, that actually produces significantly more readable output when compared to Guava and Apache Common equivalents.

Click [here](ToStringBuilder.md) for samples and a summary of features and goals.

## Dependencies

Only junit!

## Getting the source

You can either download the source using the "ZIP" button at the top
of the github page, or you can make a clone using git:

```
git clone git://github.com/mP1/walkingkooka.git
```