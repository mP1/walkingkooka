/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package walkingkooka.net.header;

import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;

/**
 * A parser that handles parsing a link relation parameter value.
 * <a href="https://tools.ietf.org/search/rfc5988#page-6"></a>
 * <pre>
 * The Link Header Field
 *
 * The Link entity-header field provides a means for serialising one or
 * more links in HTTP headers.  It is semantically equivalent to the
 * <LINK> element in HTML, as well as the atom:link feed-level element
 * in Atom [RFC4287].
 *
 *   Link           = "Link" ":" #link-value
 *   link-value     = "<" URI-Reference ">" *( ";" link-param )
 *   link-param     = ( ( "rel" "=" relation-types )
 *               | ( "anchor" "=" <"> URI-Reference <"> )
 *               | ( "rev" "=" relation-types )
 *               | ( "hreflang" "=" Language-Tag )
 *               | ( "media" "=" ( MediaDesc | ( <"> MediaDesc <"> ) ) )
 *               | ( "title" "=" quoted-string )
 *               | ( "title*" "=" ext-value )
 *               | ( "type" "=" ( media-type | quoted-mt ) )
 *               | ( link-extension ) )
 *   link-extension = ( parmname [ "=" ( ptoken | quoted-string ) ] )
 *               | ( ext-name-star "=" ext-value )
 *   ext-name-star  = parmname "*" ; reserved for RFC2231-profiled
 *                              ; extensions.  Whitespace NOT
 *                              ; allowed in between.
 *   ptoken         = 1*ptokenchar
 *   ptokenchar     = "!" | "#" | "$" | "%" | "&" | "'" | "("
 *               | ")" | "*" | "+" | "-" | "." | "/" | DIGIT
 *               | ":" | "<" | "=" | ">" | "?" | "@" | ALPHA
 *               | "[" | "]" | "^" | "_" | "`" | "{" | "|"
 *               | "}" | "~"
 *   media-type     = type-name "/" subtype-name
 *   quoted-mt      = <"> media-type <">
 *   relation-types = relation-type
 *               | <"> relation-type *( 1*SP relation-type ) <">
 *   relation-type  = reg-rel-type | ext-rel-type
 *   reg-rel-type   = LOALPHA *( LOALPHA | DIGIT | "." | "-" )
 *   ext-rel-type   = URI
 * </pre>
 */
final class LinkRelationHeaderValueParser extends HeaderValueParser {

    /**
     * Takes text which contains space separated relations and returns a list.
     */
    static List<LinkRelation<?>> parseLinkRelationList(final String text) {
        final LinkRelationHeaderValueParser parser = new LinkRelationHeaderValueParser(text);
        parser.parse();
        return parser.relations;
    }

    private LinkRelationHeaderValueParser(final String text) {
        super(text);
    }

    /**
     * Consume the first space / tab and fail if another follows.
     */
    @Override
    void whitespace() {
        // empty means the character is a NL or CR
        if (this.token(SPACE_HTAB).isEmpty()) {
            this.failInvalidCharacter();
        }
    }

    /**
     * Matches spaces and htabs.
     */
    private final static CharPredicate SPACE_HTAB = CharPredicates.any(" \t").setToString("SP | HTAB");

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void keyValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void multiValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void wildcard() {
        this.failInvalidCharacter();
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    /**
     * Breakup the contents into tokens separated by space or horizontal tab and then create
     * {@link LinkRelation} from each.
     */
    @Override
    void quotedText() {
        final StringBuilder token = new StringBuilder();

        final String quotedText = this.quotedText(QUOTED_PARAMETER_VALUE, false);
        final int last = quotedText.length() - 1;

        for (int i = 1; i < last; i++) {
            final char c = quotedText.charAt(i);
            if (spaceOrHorizontalTab(c)) {
                this.add(token);

                token.setLength(0);
                continue;
            }

            token.append(c);
        }

        this.add(token);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = ASCII;

    @Override
    void comment() {
        this.skipComment(); // consume and ignore comment text itself.
    }

    /**
     * <pre>
     * relation-types = relation-type
     *                | <"> relation-type *( 1*SP relation-type ) <">
     * relation-type  = reg-rel-type | ext-rel-type
     * reg-rel-type   = LOALPHA *( LOALPHA | DIGIT | "." | "-" )
     * ext-rel-type   = URI
     * </pre>
     */
    @Override
    void token() {
        // consume until a whitespace character is encountered.
        this.add(this.token(ASCII_NOT_WHITESPACE));
    }

    /**
     * Used to match valid ascii characters but not whitespace.
     */
    private final static CharPredicate ASCII_NOT_WHITESPACE = CharPredicates.asciiPrintable()
            .andNot(SPACE_HTAB);

    @Override
    void missingValue() {
        this.failInvalidCharacter();
    }

    @Override
    void endOfText() {
        // nothing extra to check.
    }

    /**
     * Adds a {@link LinkRelation} to the list of collected relations.
     */
    private void add(final CharSequence text) {
        this.relations.add(LinkRelation.with(text.toString()));
    }

    private final List<LinkRelation<?>> relations = Lists.array();
}
