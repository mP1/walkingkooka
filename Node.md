## [Node](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/Node.java)
A node is a member of a graph or tree of objects. Nodes come in two basic types, branches, that is nodes that can have
further nodes as children, and leafs, nodes that no have no further children. All nodes have a single parent, except for
the root node.

Whilst sharing the same name, this has nothing to do with NodeJS.

```java
Node {
    Node appendChild(Node);
    Map<ANAME, AVALUE> attributes();
    List<Node> children();
    int index();
    NAME name(); // return the "name" of this node, mostly relative to its parent.
    Optional<Node> parent();
    Node setAttribute(Map<ANAME, AVALUE>);
    Node setChildren(List<Node>);
    
    // NAME, ANAME, AVALUE are generic types.
    // and a few other convenience methods such as nextSibling()...
}
```

Some examples of a node graph include json, an xml document, the file system where files and directories are no nodes.

### Immutability
All node implementations in this project are immutable, all their apparent mutator methods actually return a different graph
with the new mutation. If a node is for example, changed, added or remove, the remainder of the tree will appear in the
same spots as expected. All node packages include a `Visitor` to handle visiting their respective types.

eg:
- [JsonNodeVisitor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/json/JsonNode.java)

### Navigation
Nodes unlike java objects have an interesting property in that they also have references back to their parent. All nodes
support two types of navigation besides the simple navigation gained by calling methods such as `Node.parent` and `Node.children`.
Both forms work with all implementations of the `Node` interface.

#### [NodePointer](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer.java)
A `NodePointer` functions similar to JsonPointer.
Each node includes a [pointer()](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pointer/NodePointer#pointer.java) getter which may be used
to find the same node again. A NodeSelector is basically Xpath allows selection of zero or more nodes that satisfy the selector.

#### [NodeSelector](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java) 
A `NodeSelector` is basically XPATH for Nodes, which are built using a type safe builder rather than strings.
### 

The fragment below taken from [DomDocumentTest.java](https://github.com/mP1/walkingkooka/blob/master/src/test/java/walkingkooka/xml/DomDocumentTest.java)
shows a selector being applied to a DOM loaded from an XML file.

```java
    @Test
    public void testSelectorUsage() throws Exception {
        final DomDocument document = this.fromXml();
        final NodeSelector<DomNode, DomName, DomAttributeName, String> selector = DomNode.nodeSelectorBuilder()
                .descendant()
                .named(DomName.element("img"))
                .build();
        final Set<DomNode> matches = selector.accept(document);
        assertEquals("should have matched img tags\n" + matches, 20, matches.size());
    }
```

### Node implementations

#### [DomNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/dom/DomNode.java)
Wraps a regular `www.w3.org.Node` or document with the Node interface and is of course Immutable.

- Wraps any `org.w3c.dom.Node`
- An immutable/functional Document Object Model
- Setters return a new instance rather than mutate the old.
- Properties returning `java.util.Optional` rather than `null`.
- Uses standard java collections rather than `org.w3c.dom.NodeList` and `org.w3c.dom.NamedNodeMap` to leverage powerful streams. 
- All other standard functionality like namespaces are supported.
- Node selector support, via the [walkingkooka.tree.selector.*](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select)
- Source at [walkingkooka.xml.*](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/xml)
- Tests at [walkingkooka.xml.* Tests](https://github.com/mP1/walkingkooka/blob/master/src/test/java/walkingkooka/xml)

#### [ExpressionNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionNode)
An AST that can be executed, supporting type coercion, functions and more.


Sample [test](https://github.com/mP1/walkingkooka/blob/master/src//walkingkooka/tree/expression/ExpressionNodeEvaluationTest.java) showing parsing of a spreadsheet formula into a `SpreadsheetParserToken`, converting it to an `ExpressionNode` and then evaluating it into a value.

```java
@Test
public void testParenthesis5() {
    this.parseEvaluateAndCheck("(1+2)+(3+4.5)*-6",  (1+2)+(3+4.5)*-6);
}

// skip tests and show interesting methods...that perform parsing and evaluation.

private void parseEvaluateAndCheck(final String formulaText, final String expectedText) {
    final SpreadsheetParserToken formula = this.parse(formulaText);
    final Optional<ExpressionNode> maybeExpression = formula.expressionNode();
    if(!maybeExpression.isPresent()){
        fail("Failed to convert spreadsheet formula to expression " + CharSequences.quoteAndEscape(formulaText));
    }
    final ExpressionNode expression = maybeExpression.get();
    final String value = expression.toText(ExpressionNodeTestCase.context());
    assertEquals("expression " + CharSequences.quoteAndEscape(formulaText) + " as text is", expectedText, value);
}

private SpreadsheetParserToken parse(final String parse){
    final TextCursor cursor = TextCursors.charSequence(parse);

```

Currently this only works with the spreadsheet parser tokens, but with some minor work it could represent javascript and
other similar style languages.

#### [FileNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/file/FileNode.java)
A read-only Node abstractions over the file system. A file support many interesting properties including a text representation of the actual file. A provider api is available allowing a custom strategy to get any text from a file. Some ideas for this include returning the actual text for a plain text file, extracting text from a pdf and more. THe text property is one of many that can be tested in a `NodeSelector`.

#### [JsonNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/json/JsonNode.java)
An immutable Json abstraction, with support for getting, and updating json.

#### [ParserTokenNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/ParserTokenNode.java)
All the ParserTokens of which there are many types are also nodes. ParserToken is the base class for all tokens by Parsers. Many Parsers are available which can build an AST of various types.

#### [PojoNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pojo/PojoNode.java)
An abstraction that supports mutation over java pojos making them look and behave like Nodes. Unlike regular java POJOs, all PojoNode also know their parents without any extra code.

- Wraps any bean exposing individual properties and elements of lists/sets and entries from maps as nodes or lenses.
- Properties must be public and named using a typical javabean standards along with simple property names without any prefix.
- Mutable bean properties may be updated via their setters.
- Immutable bean exposing setters that return new instances (think fluent apis) are supported.
- Beans wrapped as nodes, can have their properties and descendants individually selected using the [selector api](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/selector)
- Supports a plugable interface to assist with discovery of getters and setters on a POJO, if the default strategy needs tweaking or performance improvements.
- Includes a [reflection based bean property discovery](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pojo/ReflectionPojoNodeContext.java) implementation, which can be wrapped/extended etc.
- [walkingkooka.tree.pojo.* Tests](https://github.com/mP1/walkingkooka/blob/master/src/test/java/walkingkooka/tree/pojo)


