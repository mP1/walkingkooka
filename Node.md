## [Node](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/Node.java)
A node is a member of a graph or tree of objects. The interface definition below has been simplified, with the major 
omission being generics have been removed.
 
```java
interface Node {
  Node appendChild(Node);
  Map attributes();
  Node hasUniqueNameAmongstSiblings();
  int index();
  Name name();
  Node parentOrFail();
  Node parentWithout();
  NodePointer pointer();
  Node removeChild(int);
  Node removeParent();
  Node replace(Node);
  Node replaceChild(Node, Node);
  Node setAttributes(Map);
  Node setChild(int, Node);
  Node setChild(Name, Node);
  Node setChildren(List<Node>);
}
```

Other methods 

## Available implementations
Several implementations are available along with helpers that can assist in writing unit tests if new implementations are required.

- [Expression](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/expression) Abstraction over an expression AST
- [File](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/file) Abstraction over a file system
- [Json](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/json) Json objects
- [Pojo](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/pojo) Node abstraction over java objects.
- [Search](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/search) Text with highlight support
- [Xml](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/xml) Xml documents


## Immutability
All `Node` implementations in this project are immutable, all their apparent mutator methods actually return a different graph
with the new mutation.


## Navigation
All `Node` include a reference to their parent and children, along with helpers to find previous and following siblings.
There are a variety of other navigation aides each best suited to different use cases with a brief summary below.


## Visitor

All `Node` implementations include a type safe `Visitor` making for easy walking of a tree with navigation control of
children and descendants.

eg:
- [JsonNodeVisitor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/json/JsonNode.java)


## [NodePointer](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer.java)
A `NodePointer` provides an equivalent functionality for `Nodes` as what `Json pointer` provides for a Json object graph,
namely a path mechanism to locate a single `Node` with a tree. The `Node.pointer()` may be used to fetch the pointer for
an individual node relative to its root.

[JsonPointer RFC6901](https://tools.ietf.org/html/rfc6901)


## [Patch](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/patch)
A `patch` is a `Node` equivalent as what `json-patch` is for json objects or diff patch is for files, namely a set
of discrete operations to test and apply changes to some artifact. The advantage over json-patch implementations is that
any failed patch leaves the original source unmodified and successful patches return a new tree.

The remainder of this section below contains the sample taken from [jsonpatch.com](http://jsonpatch.com/).
The original document

```json
  {
    "baz": "qux",
    "foo": "bar"
  }
```  

The patch

```json
  [
    { "op": "replace", "path": "/baz", "value": "boo" },
    { "op": "add", "path": "/hello", "value": ["world"] },
    { "op": "remove", "path": "/foo" }
  ]
```

The result
```json
  {
    "baz": "boo",
    "hello": ["world"]
  }
```

## [NodeSelector](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java) 
A `NodeSelector` provides all xpath selectors to all `Nodes`. Predicate expressions and many xpath like functions are
implemented, with support for custom functions. Many xpath like functions (eg: `starts-with()`) are already implemented
with tickets for others outstanding.

Selectors may be built by parsing a String holding the expression or by using the would be builder methods that
`NodeSelector` provides. The sample in the stream section below shows a selector being built and the helper
eventually creates a stream and verifies the result of a collect and other stream operations against the provided expectations.

### [NodeSelector mapping](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select/NodeSelectorContexts.java#L41) 
It is possible to provide a `function` which applies a mapper function over selected or matched `Node`, with the new
tree root returned.

An example of this utility may be to load a HTML document, assemble a selector to match only certain elements and map or
modify just those, with the result being the new updated tree.

### [java.util.stream.Stream support](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java#L446)
It is also possible to leverage the power of all the intermediate operations like: skip, limits, filtering, mapping
along with familiar terminal operations such as collect, count, findAny and so on. Operations are efficient and navigation
and selections stop once a operator such as limit is satisfied.

The fragment below is a sample taken from a test.

```java
    @Test
    public void testDescendantOrSelfNamedStream() {
        TestNode.disableUniqueNameChecks(); // Most other tests require unique names for each and every TestNode.

        final TestNode root = TestNode.with("root",
                TestNode.with("branch1", TestNode.with("leaf")),
                TestNode.with("branch2",
                        TestNode.with("skip"),
                        TestNode.with("leaf")));

        this.collectAndCheck(() -> this.stream(TestNode.absoluteNodeSelector()
                        .descendantOrSelf()
                        .named(Names.string("leaf")),
                root),
                root.child(0).child(0),
                root.child(1).child(1));
    }
```

The [stream package](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/stream/push) provides a push provided stream source, rather than the pull stream support in the JDK.