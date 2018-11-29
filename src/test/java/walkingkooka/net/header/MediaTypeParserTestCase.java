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
    public final void testTypeSpaceSubTypeFails() {
        this.parseFails("type /subtype", ' ');
    }

    @Test
    public final void testTypeTabSubTypeFails() {
        this.parseFails("type\t/subtype", '\t');
    }

    @Test
    public final void testTypeSlashSpaceSubTypeFails() {
        this.parseFails("type/ subtype", "Missing sub type at 5 in \"type/ subtype\"");
    }

    @Test
    public final void testTypeSlashTabSubTypeFails() {
        this.parseFails("type/\tsubtype", "Missing sub type at 5 in \"type/\\tsubtype\"");
    }

    @Test
    public final void testSubTypeMissingFails() {
        this.parseFails("type/;", "Missing sub type at 5 in \"type/;\"");
    }

    @Test
    public final void testTypeSlashSubTypeSpaceInvalidFails() {
        this.parseFails("type/subtype Q", 'Q');
    }

    @Test
    public final void testTypeSlashSubTypeTabInvalidFails() {
        this.parseFails("type/subtype\tQ", 'Q');
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
    public final void testParameterValueMissingFails() {
        this.parseFails("type/subtype;parameter", "Missing parameter value at 22 in \"type/subtype;parameter\"");
    }

    @Test
    public final void testParameterValueMissingFails2() {
        this.parseFails("type/subtype;parameter=", "Missing parameter value at 23 in \"type/subtype;parameter=\"");
    }

    @Test
    public final void testParameterValueMissingFails3() {
        this.parseFails("type/subtype;p1=v1;p2", "Missing parameter value at 21 in \"type/subtype;p1=v1;p2\"");
    }

    @Test
    public final void testParameterValueMissingFails4() {
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
            MediaType.parse(text);
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
    public final void testTypeSubTypeSpace() {
        this.parseAndCheck("a/b ", "a", "b");
    }

    @Test
    public final void testTypeSubTypeTab() {
        this.parseAndCheck("a/b\t", "a", "b");
    }

    @Test
    public final void testTypeSubTypeSpaceSpace() {
        this.parseAndCheck("a/b  ", "a", "b");
    }

    @Test
    public final void testTypeSubTypeSpaceTab() {
        this.parseAndCheck("a/b \t", "a", "b");
    }

    @Test
    public final void testTypeSubTypeTabTab() {
        this.parseAndCheck("a/b\t\t", "a", "b");
    }

    @Test
    public final void testTypeSubTypeWhitespaceTabSpace() {
        this.parseAndCheck("abc/def\t ", "abc", "def");
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
    public final void testTypeSubTypeParameterSpace() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=value ", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeParameterTab() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=value\t", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeParameterSpaceSpace() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=value  ", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValue() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\"", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueSpace() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\" ", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueTab() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\" ", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueSpaceTabSpaceTab() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\" \t \t", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueOtherwiseInvalidCharacters() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val?ue\"", TYPE, SUBTYPE, parameters("parameter", "val?ue"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueBackslashEscaped() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\\ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\\ue"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueEscapedDoubleQuote() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\"ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\"ue"));
    }

    @Test
    public final void testTypeSubTypeQuotedParameterValueParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p1=\"v1\";p2=v2", TYPE, SUBTYPE, parameters("p1", "v1", "p2", "v2"));
    }

    @Test
    public final void testTypeSubTypeSpaceParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + "; p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeTabParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";\tp=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeSpaceTabSpaceTabParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + "; \t \tp=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public final void testTypeSubTypeParameterParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v;p2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
    }

    @Test
    public final void testTypeSubTypeSpaceTabSpaceTabParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v; \t \tp2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
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
        this.check(MediaType.parse(text), type, subtype);
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
