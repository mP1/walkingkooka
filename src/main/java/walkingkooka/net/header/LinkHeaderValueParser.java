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

import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.net.Url;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;

/**
 * A parser that handles link header values.
 */
final class LinkHeaderValueParser extends HeaderValueParserWithParameters<Link,
        LinkParameterName<?>> {

    static List<Link> parseLink(final String text) {
        final LinkHeaderValueParser parser = new LinkHeaderValueParser(text);
        parser.parse();
        return parser.links;
    }

    // @VisibleForTesting
    LinkHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return true;
    }

    @Override
    Link wildcardValue() {
        return this.failInvalidCharacter();
    }

    /**
     * <pre>
     * Link           = "Link" ":" #link-value
     * link-value     = "<" URI-Reference ">" *( ";" link-param )
     * </pre>
     */
    @Override
    Link value() {
        if (!this.hasMoreCharacters()) {
            this.missingValue();
        }

        if ('<' != this.character()) {
            this.failInvalidCharacter();
        }
        this.position++;

        final StringBuilder text = new StringBuilder();
        final int start = this.position;
        Link link;

        for (; ; ) {
            if (!this.hasMoreCharacters()) {
                this.missingValue();
            }

            final char c = this.character();
            this.position++;

            if ('>' == c) {
                try {
                    link = Link.with(Url.parse(text.toString()));
                } catch (final InvalidCharacterException invalid) {
                    throw invalid.setTextAndPosition(this.text, start + invalid.position());
                }
                break;
            }

            text.append(c);
        }

        return link;
    }

    @Override
    void missingValue() {
        this.failMissingValue(LINK);
    }

    private static final String LINK = "Link value";

    @Override
    LinkParameterName<?> parameterName() {
        return this.parameterName(PARAMETER_NAME, LinkParameterName::with);
    }

    private final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override
    String quotedParameterValue(final LinkParameterName<?> parameterName) {
        return this.quotedText(QUOTED_PARAMETER_VALUE, false);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = CharPredicates.ascii();

    @Override
    String unquotedParameterValue(final LinkParameterName<?> parameterName) {
        return this.token(UNQUOTED_PARAMETER_VALUE);
    }

    /**
     * <a href="https://tools.ietf.org/search/rfc5988#page-6"></a>
     * <pre>
     * Link           = "Link" ":" #link-value
     * link-value     = "<" URI-Reference ">" *( ";" link-param )
     * link-param     = ( ( "rel" "=" relation-types )
     *                | ( "anchor" "=" <"> URI-Reference <"> )
     *                | ( "rev" "=" relation-types )
     *                | ( "hreflang" "=" Language-Tag )
     *                | ( "media" "=" ( MediaDesc | ( <"> MediaDesc <"> ) ) )
     *                | ( "title" "=" quoted-string )
     *                | ( "title*" "=" ext-value )
     *                | ( "type" "=" ( media-type | quoted-mt ) )
     *                | ( link-extension ) )
     * link-extension = ( parmname [ "=" ( ptoken | quoted-string ) ] )
     *                | ( ext-name-star "=" ext-value )
     * ext-name-star  = parmname "*" ; reserved for RFC2231-profiled
     *                               ; extensions.  Whitespace NOT
     *                               ; allowed in between.
     * ptoken         = 1*ptokenchar
     * ptokenchar     = "!" | "#" | "$" | "%" | "&" | "'" | "("
     *                | ")" | "*" | "+" | "-" | "." | "/" | DIGIT
     *                | ":" | "<" | "=" | ">" | "?" | "@" | ALPHA
     *                | "[" | "]" | "^" | "_" | "`" | "{" | "|"
     *                | "}" | "~"
     * media-type     = type-name "/" subtype-name
     * quoted-mt      = <"> media-type <">
     * relation-types = relation-type
     *                | <"> relation-type *( 1*SP relation-type ) <">
     * relation-type  = reg-rel-type | ext-rel-type
     * reg-rel-type   = LOALPHA *( LOALPHA | DIGIT | "." | "-" )
     * ext-rel-type   = URI
     * </pre>
     */
    final static CharPredicate UNQUOTED_PARAMETER_VALUE = CharPredicates.builder()
            .any("!#$%&'()*+-./:<=>?@[]^_`{|}~")
            .range('0', '9')
            .range('A', 'Z')
            .range('a', 'z')
            .build()
            .setToString("rfc5988");

    @Override
    void valueComplete(final Link link) {
        this.links.add(link);
    }

    private final List<Link> links = Lists.array();
}
