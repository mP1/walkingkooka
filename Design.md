### Design
A simple short commentary about the design of java components.


### Immutability
All value types are immutable and most other classes are immutable except for 
[Builder](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/build/Builder.java). Builders
are not that frequent most of the time classes have would be setters which create a new instance when a set with a different
value is attempted.

[AbsoluteUrl](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/net/AbsoluteUrl.java)
``java
    public AbsoluteUrl setHost(final HostAddress host) {
        Objects.requireNonNull(host, "host");

        return this.host.equals(host) ?
               this :
               new AbsoluteUrl(this.scheme, this.credentials, host, this.port, this.path, this.query, this.fragment);
    }
``

The components of both `AbsoluteUrl` and [RelativeUrl](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/net/RelativeUrl.java) 
are all immutable and follow the same pattern for their respective components.


### Interfaces
Except for value types, everything is an interface. An example of this is a [Node](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/Node.java).



### Classes
Public classes only almost used exclusively to define new immutable value types. Some examples include:

- [AbsoluteUrl](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/net/AbsoluteUrl.java)

Besides value types, the only other use of public classes is as a home for static factory methods.

- [CharPredicates](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate/character/CharPredicates.java)

In this example of implementations of the
[CharPredicate](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate/character/CharPredicate.java)
are obviously package private and instances fetched/created using the static factory methods on `CharPredicates`.

Most of the time, everything is package private.




### Enums
Many enums are often public as necessary, particularly when they are used as a choice to a public interface.
Where useful enums may contain methods.

- [UrlScheme](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/net/UrlScheme.java)

Rather than using magic values, enums are the preferred choice and always used. Sometimes magic values do exist but
they are almost always package private/private and their scope is limited between a few collaborators in their home
package.


#### Abstract classes

Public abstract classes available to sub class do not exist in the system. Two exceptions in exist, the many
[Visitors](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/visit/Visitor.java) which are almost always
public and sub classes of [TestCase](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/test/TestCase.java).

Abstract classes with sub classes do exist, but sub classes are always limited to the same package.

- [JsonNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/json/JsonNode.java)
- [NodeSelector](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/select/NodeSelector.java)

All sub classes of the two examples above only exist in their respective packages. Using package private ctors and `final`
no other sub classes are possible.

#### [Visitor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/visit/Visitor.java)

Many immutable value types which contain some form of heirarchy such as a `JsonNode` include a `Visitor` enabling
traversal of a graph of objects. Other value types such as
[ExpressionNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/expression/ExpressionNode.java)
which contain dozens of sub types, would require a `Visitor` with an equal number of visit methods, which would introduce
significant clutter when only a few out of the many methods needs to be overridden to "add" a behaviour when often the
default, is sufficient. It is for this method, that `Visitors` are public and not an interface.

#### [TestCase](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/test/TestCase.java)
Many sub classes exist, most interfaces have their own `TestCase` which adds extra tests and helper methods to assist
with testing a method in both success and failure outcomes.

The `Node` interface has many implementations found in different packages and to aide testing a base class exists,
which each can extend and enhance as required.

- [NodeTestCase](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/tree/NodeTestCase.java)



### Overloading
Method overloading almost never exist but do appear in a few exceptions as the alternative would be a considerably worser
choice. Wherever possible, related methods are given a "different" but similar name.

One example where overloading is used is by `Visitors` as this aides the simplicity of using the double dispatch pattern,
and [ToStringBuilder](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/ToStringBuilder.java)
which has many overloaded methods that accept values, basically because java primitives and object dont have a common
base type.

Rather than using an overload and have it call the real method with some default, constants are defined for defaults.
Overloads do exist but they are primarily package private.



### Constructors
Public and protected constructors basically should not exist in the system, except for the two exceptions to public
classes mentioned above `Visitor` and `TestCase` and its variants.



### Constants
No magic values exist in the system except for compatiblity with existing java interfaces. Where possible `Enums` and
actual value classes exist as constants and are preferred, and makes for better readability.

- [UrlQueryString.EMPTY](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/net/UrlQueryString.java)

The use of this null value replaces the need for overloads when building a Url with all parameters including the need
to pass no query string.



### Naming
Naming is an important aspect of code readability and numerous rules are enforced about naming. All implementations of
the [Parser](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/Parser.java)
and [ParserNode](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/ParserToken.java)
must also end in the same suffix. That is all `Parsers` end with `Parser`. The `ParserTestCase` enforces this naming
style.

Other test cases also enforce other naming standards within their own package, eg
[ExpressionNodeTestCase](https://github.com/mP1/walkingkooka/blob/master/src/test/java/walkingkooka/tree/expression/ExpressionNodeTestCase.java).
In the later case, it asserts that all `Node` implementation simple class names start with `Expression` and end with
`Node`.


### Testing
Where possible tests exist to verify some of the above rules have not been broken.