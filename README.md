# Basic Project

A collection of things missing from the JDK and other more famous projects.

## Something useful [ToStringBuilder](walkingkooka.build.tostring.ToStringBuilder)

There are plenty of open source ToString helpers, like those present in Guava and Apache Commons Lang ToStringBuilder. 
However they are fairly simple and can be reduced to appending labels and values in pairs. While functional, this can 
be improved, to help make the String a little more helpful for logs and debugging purposes.

###Some interesting/useful supported ideas

- Escaping AND/OR quoting char sources such as char arrays & CharSequences, helps spot control chars etc.
- Skipping falsey properties (nulls, zeros etc)
- Unwrapping values present Optionals
- Support for nested objects each supporting tuning of ToStringBuilderOption
- Hard limits on value and length
- Hex encoding byte and other numeric values.
- Read the javadoc & tests for more samples.

This is a very simple example using a Url record like class. Our record would have the following properties which would
be populated after parsing a URL String into its core components.

- scheme (http OR https)
- host
- port
- path
- params as a Map
- anchor

Many of the above properties will be null or empty string, the goal of ToStringBuilder is that it facilitates
rebuilding something that almost looks url, which means skipping properties that are not present.

- scheme "https://"
- host "example.com"
- params a=b c=d

```java
ToStringBuilder.create()
  .disable(ToStringBuilderOption.QUOTE) // added for clarity, sensible defaults exist.
  .disable(ToStringBuilderOption.ESCAPE)
  .separator("") // appear between value(XXX) calls
  .value(this.scheme)
  .value(this.host)
  .label(":")
  .value(this.port)
  .value(this.path)
  
   // tries to reproduce key/values joined by '&' but without URL encoding.
  .labelSeparator("=").valueSeparator("&").value(this.params)
  
  // will conditionally add the '#' if anchor is not empty(falsey) 
  .label("#").value(this.anchor)
  .build()
  
// gives ... https://example.com/?a=b&c=d 
```

Its not perfect, but it does make for more beautiful and readable strings.

- [walkingkooka.build.tostring.* Tests](https://github.com/mP1/walkingkooka/tree/master/src/test/java/walkingkooka/build/tostring)

## Node graph selector API

A new [fluent API](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/select) supporting
something very similar in appearance and functionality to XPATH. The atom is the node interface which looks something like:

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
    
    // and a few other convenience methods such as nextSibling()...
}
```

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

## Immutable Dom abstraction

The package `walkingkooka.xml` includes a more modern immutable DOM API, with several more modern improvements:

- Wraps any org.w3c.dom.Node
- An immutable/functional Document Object Model
- Setters return a new instance rather than mutate the old.
- Properties returning `java.util.Optional` rather than `null`.
- Uses standard java collections rather than `org.w3c.dom.NodeList` and `org.w3c.dom.NamedNodeMap` to leverage powerful streams. 
- All other standard functionality like namespaces are supported.
- Node selector support, via the [walkingkooka.tree.selector.*](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/select)
- Source at [walkingkooka.xml.*](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/xml)
- [walkingkooka.xml.* Tests](https://github.com/mP1/walkingkooka/tree/master/src/test/java/walkingkooka/xml)

## Immutable Pojo Nodes.

- Wraps any bean exposing individual properties and elements of lists/sets and entries from maps as nodes or lenses.
- Properties must be public and named using a typical javabean standards along with simple property names without any prefix.
- Mutable bean properties may be updated via their setters.
- Immutable bean exposing setters that return new instances (think fluent apis) are supported.
- Beans wrapped as nodes, can have their properties and descendants individually selected using the [selector api](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/tree/selector)
- Supports a plugable interface to assist with discovery of getters and setters on a POJO, if the default strategy needs tweaking or performance improvements.
- Includes a [reflection based bean property discovery](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/pojo/ReflectionPojoNodeContext.java) implementation, which can be wrapped/extended etc.
- [walkingkooka.tree.pojo.* Tests](https://github.com/mP1/walkingkooka/tree/master/src/test/java/walkingkooka/tree/pojo)

## Lens

The dom and pojo abstractions both work as immutable lens, an proper common Lens interface needs to be extracted (TODO).

## Getting the source

You can either download the source using the "ZIP" button at the top
of the github page, or you can make a clone using git:

```
git clone git://github.com/mP1/walkingkooka.git
```