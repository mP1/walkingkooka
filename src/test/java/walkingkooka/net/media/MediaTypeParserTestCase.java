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

package walkingkooka.net.media;

import org.junit.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public abstract class MediaTypeParserTestCase<P extends MediaTypeParser> extends PackagePrivateClassTestCase<P> {

    MediaTypeParserTestCase() {
        super();
    }

    // constants

    final static String TYPE = "type";
    final static String SUBTYPE = "subtype";

    @Test
    public final void testInvalidFails() {
        this.parseFails("\u0100\u0101", 0);
    }

    @Test
    public final void testEmptyTypeFails() {
        this.parseFails("/subtype", "Missing type at 0 in \"/subtype\"");
    }

    @Test
    public final void testTypeMissingSubTypeFails() {
        this.parseFails("type", "Missing sub type at 4 in \"type\"");
    }

    @Test
    public final void testEmptySubTypeFails() {
        this.parseFails("type/", "Missing sub type at 5 in \"type/\"");
    }

    @Test
    public final void testInvalidTypeCharacterFails() {
        this.parseFails("prima?ry/subtype", '?');
    }

    @Test
    public final void testTypeWhitespaceFails() {
        this.parseFails("type /subtype", ' ');
    }

    @Test
    public final void testSubTypeWhitespaceFails() {
        this.parseFails("type/ subtype", ' ');
    }

    @Test
    public final void testSubTypeMissingFails() {
        this.parseFails("type/;", "Missing sub type at 5 in \"type/;\"");
    }

    @Test
    public final void testParameterNameInvalidFails() {
        this.parseFails("type/subtype;parameter;");
    }

    @Test
    public final void testParameterValueInvalidFails() {
        this.parseFails("type/subtype;parameter=value/");
    }

    @Test
    public final void testParameterValueFails() {
        this.parseFails("type/subtype;parameter", "Missing parameter value at 22 in \"type/subtype;parameter\"");
    }

    @Test
    public final void testParameterValueFails2() {
        this.parseFails("type/subtype;parameter=", "Missing parameter value at 23 in \"type/subtype;parameter=\"");
    }

    @Test
    public final void testParameterValueFails3() {
        this.parseFails("type/subtype;p1=v1;p2", "Missing parameter value at 21 in \"type/subtype;p1=v1;p2\"");
    }

    @Test
    public final void testParameterValueFails4() {
        this.parseFails("type/subtype;p1=v1;p2=", "Missing parameter value at 22 in \"type/subtype;p1=v1;p2=\"");
    }

    @Test
    public final void testParameterValueUnclosedQuoteFails() {
        this.parseFails("type/subtype;parameter=\"");
    }

    @Test
    public final void testParameterValueInvalidEscapeFails() {
        this.parseFails("type/subtype;parameter=\"\\z\"", 'z');
    }

    @Test
    public final void testParameterValueQuotedInvalidFails() {
        this.parseFails("type/subtype;p=\"v\";[", '[');
    }

    @Test
    public final void testParameterValueQuotedInvalidFails2() {
        this.parseFails("type/subtype;p=\"v\"[", '[');
    }

    @Test
    public void testCommaFails() {
        this.parseFails(",");
    }

    @Test
    public void testCommaFails2() {
        this.parseFails(",type/subtype", ',');
    }

    final void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    final void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    final void parseFails(final String text, final int pos) {
        parseFails(text, MediaType.invalidCharacter(text.charAt(pos), pos, text));
    }

    final void parseFails(final String text, final String message) {
        try {
            MediaType.parseOne(text);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    @Test
    public final void testTypeSubType() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE, TYPE, SUBTYPE);
    }

    @Test
    public final void testTypeWildcardSubType() {
        this.parseAndCheck(MediaType.WILDCARD + "/" + SUBTYPE, MediaType.WILDCARD, SUBTYPE);
    }

    @Test
    public final void testTypeSubType2() {
        this.parseAndCheck("a/b", "a", "b");
    }

    @Test
    public final void testTypeSubTypeWithSuffix() {
        this.parseAndCheck("a/b+suffix", "a", "b+suffix");
    }

    @Test
    public final void testTypeSubTypeWildcard() {
        this.parseAndCheck("a/*", "a", MediaType.WILDCARD);
    }

    @Test
    public final void testTypeSubTypeParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=value", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeParameterValueQuoted() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\"", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeParameterValueQuotedOtherwiseInvalidCharacters() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val?ue\"", TYPE, SUBTYPE, parameters("parameter", "val?ue"));
    }

    @Test
    public final void testTypeSubTypeParameterValueQuotedBackslashEscaped() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\\ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\\ue"));
    }

    @Test
    public final void testTypeSubTypeParameterValueQuotedEscapedDoubleQuote() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\"ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\"ue"));
    }

    @Test
    public final void testTypeSubTypeParameterValueQuotedParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p1=\"v1\";p2=v2", TYPE, SUBTYPE, parameters("p1", "v1", "p2", "v2"));
    }

    @Test
    public final void testTypeSubTypeWhitespaceParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + "; p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeWhitespaceParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";  p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeWhitespaceParameter3() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";   p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeWhitespaceParameter4() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v; p2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
    }

    @Test
    public final void testTypeSubTypeWhitespaceParameter5() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v;  p2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
    }

    final Map<MediaTypeParameterName, String> parameters(final String name, final String value) {
        return Maps.one(MediaTypeParameterName.with(name), value);
    }

    final Map<MediaTypeParameterName, String> parameters(final String name, final String value, final String name2, final String value2) {
        final Map<MediaTypeParameterName, String> parameters = Maps.ordered();
        parameters.put(MediaTypeParameterName.with(name), value);
        parameters.put(MediaTypeParameterName.with(name2), value2);

        return parameters;
    }

    private void parseAndCheck(final String text, final String type, final String subtype) {
        this.check(MediaType.parseOne(text), type, subtype);
    }

    abstract void parseAndCheck(final String text,
                                final String type,
                                final String subtype,
                                final Map<MediaTypeParameterName, String> parameters);

    final void check(final MediaType mediaType, final String type, final String subtype) {
        check(mediaType, type, subtype, MediaType.NO_PARAMETERS);
    }

    final void check(final MediaType mediaType,
                     final String type,
                     final String subtype,
                     final Map<MediaTypeParameterName, String> parameters) {
        assertEquals("type=" + mediaType, type, mediaType.type());
        assertEquals("subType=" + mediaType, subtype, mediaType.subType());
        assertEquals("parameters=" + mediaType, parameters, mediaType.parameters());
    }
}
