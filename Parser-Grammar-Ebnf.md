## Grammars
A grammar is a text description of the tokens of a language. The definition of any token may be a series of characters
or other tokens.

Some important attributes and ordering of tokens includes:
- *Required*: Some tokens are required.
- *Optional*: Some tokens are optional, eg whitespace within an expression.
- *Repetition*: An example of this might be a whitespace token which may be defined as any sequence of repeating
  whitespace characters.
- *Concatenation*: A concatenation is a series of required/optional tokens in a prescribed order. 
- *Alternatives*: A series of choices, tried in order until one succeeds.

The parsing framework, supports abstractions for the above mentioned concepts.

- [Parsers](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/Parsers.java)

### Extended Backus Naur form
Rather than building grammars in java which can be very verbose a text DSL is preferable for readability and brevity.
A popular format is [EBNF](https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form), in one of its various
standard but different forms. The differences between these variations is the use of different symbols to express
the cardinality of a token.

The file below is an example of the json grammar used to define JSON in the system.

[EBNF Json grammar](https://github.com/mP1/walkingkooka/blob/master/src/main/resources/walkingkooka/text/cursor/parser/json/json-parsers.grammar)
```ebnf
VALUE=                  NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT;
VALUE_REQUIRED=         VALUE;

ARRAY_ELEMENT=          [ WHITESPACE ], VALUE;
ARRAY_ELEMENT_REQUIRED= [ WHITESPACE ], VALUE_REQUIRED;

ARRAY=                  ARRAY_BEGIN,
                        ARRAY_REQUIRED;

ARRAY_REQUIRED=         [ ARRAY_ELEMENT, [{ [ WHITESPACE ], SEPARATOR, ARRAY_ELEMENT_REQUIRED }]],
                        [ WHITESPACE ],
                        ARRAY_END;

OBJECT_PROPERTY_REQUIRED=OBJECT_PROPERTY;
OBJECT_PROPERTY        =[ WHITESPACE ], STRING, [ WHITESPACE ], OBJECT_ASSIGNMENT, [ WHITESPACE ], VALUE_REQUIRED;

OBJECT=                 OBJECT_BEGIN,
                        OBJECT_REQUIRED;
OBJECT_REQUIRED=        [ OBJECT_PROPERTY, [{[ WHITESPACE ], SEPARATOR, OBJECT_PROPERTY_REQUIRED }]],
                        [ WHITESPACE ],
                        OBJECT_END;
```

To assist understanding the grammar::
- square brackets "[]" mean that token is optional.
- curly brackets "{}" denote repetition.
- any token without either square or curly brackets is required.
- a list of tokens separated by pipes "|" is an alternation list.
- a list of tokens separated by commas "," is a concatenation of tokens.



### [text.cursor.parser.ebnf.*](https://github.com/mP1/walkingkooka/tree/master/src/main/java/walkingkooka/text/cursor/parser/ebnf)
This package contains parsers and parser tokens that may be used to parse a well formed ebnf file including comments
into a `EbnfGrammarParserToken` for further processing.



#### [Transform EbnfGrammarParserToken into parsers](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/ebnf/EbnfGrammarParserToken.java)
This token holds an entire Ebnf text file. The `EbnfGrammarParserToken.combinator(...)` method then accepts
[EbnfParserCombinatorSyntaxTreeTransformer](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/cursor/parser/ebnf/combinator/EbnfParserCombinatorSyntaxTreeTransformer.java)
which can then perform tasks such as creating parsers. As mentioned previously this is how the grammar file above is
turned into parsers.



### [Transform Ebnf text file into CharPredicates](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/predicate/character/CharPredicates.java)
The `CharPredicates.fromGrammar(...)` accepts a EBNF text file and returns a Map of name to `CharPredicate`. This is
another use case of the possibilities of grammars and using a visitor to turn tokens into something else.



### Internet standards
As mentioned previously many internet standards use EBNF or a flavour to express text format. The files can then be
used to create `CharPredicate` or `Parsers`.

Some examples include:

- [Http](https://tools.ietf.org/html/rfc7230)
- [BNF for URL schemes](https://www.w3.org/Addressing/URL/5_BNF.html)

Its relatively easy to find more simply search for `EBNF` followed by the name of the technology or artifact.
