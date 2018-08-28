### [Parser](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/Parser.java)
Parsers consume characters from a source and creates tokens from chunks of text. There is one significant difference in
operation between these Parsers and the parse methods (eg Long.parse) found in the JDK. The later requires the
boundaries of the token to be computed prior to parsing, with the source `String` extracted using some logic.

The Parser interface below denotes the main collaborators in the parsing process.

```java
public interface Parser<T extends ParserToken, C extends ParserContext> {

    /**
     * Attempts to parse the text given by the {@link TextCursor}.
     */
    Optional<T> parse(final TextCursor cursor, final C context);
}
```

The result of any parse attempt is an `Optional` because parsing often involves trying multiple alternatives until success.



### [ParserToken](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/ParserToken.java)
Each `Parser` creates its own `ParserToken` sub class to hold the text consumed and the value it identified.



### [TextCursor](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/TextCursor.java)
A `TextCursor` provides an abstraction that supports consuming character by character in a forward manner. A `TextCursor`
operates in a similar manner to an `Iterator` with methods to test if more characters are available and to the ability
to read the current character and advance. Just like an Iterator reading only continues forwards.



### [TextCursorSavePoint](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/TextCursorSavePoint.java)
A `TextCursorSavePoint` may be saved from the current `TextCursor` position and may be used to restore the position at
some future time. There is no limit to the number of `TextCursorSavePoint` that can be saved and used during a parsing
operation.

Typically several parsers will be tried in sequence, one for each of the possible tokens. Each previous parser might 
consume several characters and then give up and need to restore the `TextCursor` back to its original position so the
next can try. An example of this might be a java method definition where some tokens (keywords) are optional and order
is not strictly defined (eg `public`, `protected`, `private`, `static`, type etc).



### [Error reporting](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/ReportingParser.java)
In some cases a parse failure is an actual un-recoverable error, the most basic case is that all parsers have been tried
but text remains. This of course means, the `TextCursor` is positioned at an unrecognized character and an error should
be reported. A `ReportingParser` can be used to report an error in such cases. `TextCursor` and `TextCursorSavePoint`
also contain methods to return details of their respective positions, including the line number, column and line of text.



### [Tests](https://github.com/mP1/walkingkooka/tree/master/src/test/java/walkingkooka/text/cursor/parser)
The test package contains many tests that accompany each and every parser and are a good read to better understand how
all the moving parts work and interact with other components.



### [text.cursor.parser.* package](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/text/cursor/parser)
This is the core package that contains all the above interfaces and many implementations.

