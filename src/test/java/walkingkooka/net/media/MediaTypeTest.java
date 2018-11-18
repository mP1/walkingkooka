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
import walkingkooka.test.PublicClassTestCase;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

final public class MediaTypeTest extends PublicClassTestCase<MediaType> {

    // constants

    private final static String TYPE = "type";
    private final static String SUBTYPE = "subtype";

    // tests

    @Test(expected = NullPointerException.class)
    public void testNullTypeFails() {
        MediaType.with(null, SUBTYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTypeAndSubTypeFails() {
        MediaType.with("", SUBTYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeContainsSlashFails() {
        MediaType.with("ty/pe", SUBTYPE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeContainsInvalidCharacterFails() {
        MediaType.with("ty?pe", SUBTYPE);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSubTypeFails() {
        MediaType.with(TYPE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeAndEmptySubTypeFails() {
        MediaType.with(TYPE, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyIncludesSlashFails() {
        MediaType.with(TYPE, "s/ub");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyIncludesInvalidCharacterFails() {
        MediaType.with(TYPE, "s?ub");
    }

    @Test
    public void testWithTypeAndSubType() {
        check(MediaType.with(TYPE, SUBTYPE), TYPE, SUBTYPE);
    }

    @Test
    public void testWithTypeAndWildcardSubType() {
        check(MediaType.with(TYPE, MediaType.WILDCARD), TYPE, MediaType.WILDCARD);
    }

    @Test
    public void testWithWildcardTypeAndWildcardSubType() {
        check(MediaType.with(MediaType.WILDCARD, MediaType.WILDCARD), MediaType.WILDCARD, MediaType.WILDCARD);
    }

    // setType .........................................................................

    @Test(expected = NullPointerException.class)
    public void testSetTypeNullFails() {
        this.mediaType().setType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypeEmptyFails() {
        this.mediaType().setType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypeInvalidCharacterFails() {
        this.mediaType().setType("type/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypeInvalidCharacterFails2() {
        this.mediaType().setType("type?");
    }

    @Test
    public void testSetTypeSame() {
        final MediaType mediaType = this.mediaType();
        assertSame(mediaType, mediaType.setType(TYPE));
    }

    @Test
    public void testSetTypeDifferent() {
        final MediaType mediaType = this.mediaType();
        final String type = "different";
        final MediaType different = mediaType.setType(type);
        check(different, type, SUBTYPE, parameters());
    }

    // setSubType ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetSubTypeNullFails() {
        this.mediaType().setSubType(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSubTypeEmptyFails() {
        this.mediaType().setSubType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSubTypeInvalidCharacterFails() {
        this.mediaType().setSubType("type/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSubTypeInvalidCharacterFails2() {
        this.mediaType().setSubType("type?");
    }

    @Test
    public void testSetSubTypeSame() {
        final MediaType mediaType = this.mediaType();
        assertSame(mediaType, mediaType.setSubType(SUBTYPE));
    }

    @Test
    public void testSetSubTypeDifferent() {
        final MediaType mediaType = this.mediaType();
        final String subtype = "different";
        final MediaType different = mediaType.setSubType(subtype);
        check(different, TYPE, subtype, parameters());
    }

    // setParameters ..........................................................................................

    @Test(expected = NullPointerException.class)
    public void testSetParametersNullFails() {
        this.mediaType().setParameters(null);
    }

    @Test
    public void testSetParametersSame() {
        final MediaType mediaType = this.mediaType();
        assertSame(mediaType, mediaType.setParameters(parameters()));
    }

    @Test
    public void testSetParametersDifferent() {
        final MediaType mediaType = this.mediaType();
        final Map<MediaTypeParameterName, String> parameters = MediaType.NO_PARAMETERS;
        final MediaType different = mediaType.setParameters(parameters);
        check(different, TYPE, SUBTYPE, parameters);
    }

    @Test
    public void testSetParametersDifferent2() {
        final MediaType mediaType = this.mediaType();
        final Map<MediaTypeParameterName, String> parameters = Maps.one(MediaTypeParameterName.with("different"), "value789");
        final MediaType different = mediaType.setParameters(parameters);
        check(different, TYPE, SUBTYPE, parameters);
    }

    // parse .........................................................................

    @Test(expected = NullPointerException.class)
    public void testParseNullFails() {
        MediaType.parse(null);
    }

    @Test
    public void testParseInvalidFails() {
        this.parseFails("\u0100\u0101", 0);
    }

    @Test
    public void testParseEmptyTypeFails() {
        this.parseFails("/subtype", "Missing type at 0 in \"/subtype\"");
    }

    @Test
    public void testParseTypeMissingSubTypeFails() {
        this.parseFails("type", "Missing sub type at 4 in \"type\"");
    }

    @Test
    public void testParseEmptySubTypeFails() {
        this.parseFails("type/", "Missing sub type at 5 in \"type/\"");
    }

    @Test
    public void testParseInvalidTypeCharacterFails() {
        this.parseFails("prima?ry/subtype", '?');
    }

    @Test
    public void testParseTypeWhitespaceFails() {
        this.parseFails("type /subtype", ' ');
    }

    @Test
    public void testParseSubTypeWhitespaceFails() {
        this.parseFails("type/ subtype", ' ');
    }

    @Test
    public void testParseSubTypeMissingFails() {
        this.parseFails("type/;", "Missing sub type at 5 in \"type/;\"");
    }

    @Test
    public void testParseParameterNameInvalidFails() {
        this.parseFails("type/subtype;parameter;");
    }

    @Test
    public void testParseParameterValueInvalidFails() {
        this.parseFails("type/subtype;parameter=value/");
    }

    @Test
    public void testParseParameterValueFails() {
        this.parseFails("type/subtype;parameter", "Missing parameter value at 22 in \"type/subtype;parameter\"");
    }

    @Test
    public void testParseParameterValueFails2() {
        this.parseFails("type/subtype;parameter=", "Missing parameter value at 23 in \"type/subtype;parameter=\"");
    }

    @Test
    public void testParseParameterValueFails3() {
        this.parseFails("type/subtype;p1=v1;p2", "Missing parameter value at 21 in \"type/subtype;p1=v1;p2\"");
    }

    @Test
    public void testParseParameterValueFails4() {
        this.parseFails("type/subtype;p1=v1;p2=", "Missing parameter value at 22 in \"type/subtype;p1=v1;p2=\"");
    }

    @Test
    public void testParseParameterValueUnclosedQuoteFails() {
        this.parseFails("type/subtype;parameter=\"");
    }

    @Test
    public void testParseParameterValueInvalidEscapeFails() {
        this.parseFails("type/subtype;parameter=\"\\z\"", 'z');
    }

    @Test
    public void testParseParameterValueQuotedInvalidFails() {
        this.parseFails("type/subtype;p=\"v\";[", '[');
    }

    @Test
    public void testParseParameterValueQuotedInvalidFails2() {
        this.parseFails("type/subtype;p=\"v\"[", '[');
    }

    private void parseFails(final String text) {
        this.parseFails(text, text.length() - 1);
    }

    private void parseFails(final String text, final char pos) {
        this.parseFails(text, text.indexOf(pos));
    }

    private void parseFails(final String text, final int pos) {
        parseFails(text, MediaType.invalidCharacter(text.charAt(pos), pos, text));
    }

    private void parseFails(final String text, final String message) {
        try {
            MediaType.parse(text);
            fail();
        } catch (final IllegalArgumentException expected) {
            assertEquals("Incorrect failure message", message, expected.getMessage());
        }
    }

    @Test
    public void testParseTypeSubType() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE, TYPE, SUBTYPE);
    }

    @Test
    public void testParseTypeWildcardSubType() {
        this.parseAndCheck(MediaType.WILDCARD + "/" + SUBTYPE, MediaType.WILDCARD, SUBTYPE);
    }

    @Test
    public void testParseTypeSubType2() {
        this.parseAndCheck("a/b", "a", "b");
    }

    @Test
    public void testParseTypeSubTypeWildcard() {
        this.parseAndCheck("a/*", "a", MediaType.WILDCARD);
    }

    @Test
    public void testParseTypeSubTypeParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public void testParseTypeSubTypeParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=value", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public void testParseTypeSubTypeParameterValueQuoted() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"value\"", TYPE, SUBTYPE, parameters("parameter", "value"));
    }

    @Test
    public void testParseTypeSubTypeParameterValueQuotedOtherwiseInvalidCharacters() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val?ue\"", TYPE, SUBTYPE, parameters("parameter", "val?ue"));
    }

    @Test
    public void testParseTypeSubTypeParameterValueQuotedBackslashEscaped() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\\ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\\ue"));
    }

    @Test
    public void testParseTypeSubTypeParameterValueQuotedEscapedDoubleQuote() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";parameter=\"val\\\"ue\"", TYPE, SUBTYPE, parameters("parameter", "val\\\"ue"));
    }

    @Test
    public void testParseTypeSubTypeParameterValueQuotedParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p1=\"v1\";p2=v2", TYPE, SUBTYPE, parameters("p1", "v1", "p2", "v2"));
    }

    @Test
    public void testParseTypeSubTypeWhitespaceParameter() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + "; p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public void testParseTypeSubTypeWhitespaceParameter2() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";  p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public void testParseTypeSubTypeWhitespaceParameter3() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";   p=v", TYPE, SUBTYPE, parameters("p", "v"));
    }

    @Test
    public void testParseTypeSubTypeWhitespaceParameter4() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v; p2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
    }

    @Test
    public void testParseTypeSubTypeWhitespaceParameter5() {
        this.parseAndCheck(TYPE + "/" + SUBTYPE + ";p=v;  p2=v2", TYPE, SUBTYPE, parameters("p", "v", "p2", "v2"));
    }

    private void parseAndCheck(final String text, final String type, final String subtype) {
        this.check(MediaType.parse(text), type, subtype);
    }

    private void parseAndCheck(final String text,
                               final String type,
                               final String subtype,
                               final Map<MediaTypeParameterName, String> parameters) {
        this.check(MediaType.parse(text), type, subtype, parameters);
    }

    private void check(final MediaType mediaType, final String type, final String subtype) {
        check(mediaType, type, subtype, MediaType.NO_PARAMETERS);
    }

    private void check(final MediaType mediaType,
                       final String type,
                       final String subtype,
                       final Map<MediaTypeParameterName, String> parameters) {
        assertEquals("type=" + mediaType, type, mediaType.type());
        assertEquals("subType=" + mediaType, subtype, mediaType.subType());
        assertEquals("parameters=" + mediaType, parameters, mediaType.parameters());
    }

    @Test(expected = NullPointerException.class)
    public void testIsCompatibleNullFails() {
        MediaType.WILDCARD_WILDCARD.isCompatible(null);
    }

    @Test
    public void testIsCompatibleWithSelf() {
        final MediaType mediaType = MediaType.with("type", "subtype");
        this.isCompatibleCheckTrue(mediaType, mediaType);
    }

    @Test
    public void testIsCompatibleAnyAlwaysMatches() {
        this.isCompatibleCheckTrue(MediaType.WILDCARD_WILDCARD, MediaType.with("custom", "custom2"));
    }

    @Test
    public void testIsCompatibleAnyAndAnyMatches() {
        this.isCompatibleCheckTrue(MediaType.WILDCARD_WILDCARD, MediaType.WILDCARD_WILDCARD);
    }

    @Test
    public void testIsCompatibleSameTypeWildcardSubType() {
        final String type = "custom";
        this.isCompatibleCheckTrue(MediaType.with(type, MediaType.WILDCARD), MediaType.with(type, SUBTYPE));
    }

    @Test
    public void testIsCompatibleDifferentTypeWildcardSubType() {
        this.isCompatibleCheckFalse(MediaType.with("custom", MediaType.WILDCARD), MediaType.with("different", SUBTYPE));
    }

    @Test
    public void testIsCompatibleSameTypeSubType() {
        this.isCompatibleCheckTrue(this.mediaType(), this.mediaType());
    }

    @Test
    public void testIsCompatibleDifferentTypeSameSubType() {
        this.isCompatibleCheckFalse(this.mediaType(), MediaType.with("different", SUBTYPE));
    }

    @Test
    public void testIsCompatibleSameTypeDifferentSubType() {
        final String type = "type";
        this.isCompatibleCheckFalse(MediaType.with(type, "subtype"), MediaType.with(type, "different"));
    }

    private void isCompatibleCheckTrue(final MediaType mediaType, final MediaType other) {
        if (false == mediaType.isCompatible(other)) {
            fail(mediaType + " should be compatible with " + other + " but FALSE was returned.");
        }
    }

    private void isCompatibleCheckFalse(final MediaType mediaType, final MediaType other) {
        if (mediaType.isCompatible(other)) {
            fail(mediaType + " should NOT be compatible with " + other + " but TRUE was returned.");
        }
    }

    // toString........................................................................................................

    @Test
    public void testToStringParse() {
        final String text = "type/subtype";
        assertEquals(text, MediaType.parse(text).toString());
    }

    @Test
    public void testToStringParseWithParameters() {
        final String text = "type/subtype;a=b;c=d";
        assertEquals(text, MediaType.parse(text).toString());
    }

    @Test
    public void testToStringParseWithParametersWithQuotes() {
        final String text = "type/subtype;a=b;c=\"d\"";
        assertEquals(text, MediaType.parse(text).toString());
    }

    @Test
    public void testToString() {
        final MediaType mediaType = MediaType.with(TYPE, SUBTYPE);
        assertEquals(TYPE + "/" + SUBTYPE, mediaType.toString());
    }

    @Test
    public void testToStringWithParameters() {
        assertEquals(TYPE + "/" + SUBTYPE + ";parameter123=value456", mediaType().toString());
    }

    @Test
    public void testToStringWithParametersRequireQuotesWhitespace() {
        final MediaType mediaType = MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b c"));
        assertEquals("type/subtype; a=\"b c\"", mediaType.toString());
    }

    @Test
    public void testToStringWithParametersRequireQuotesBackslash() {
        final MediaType mediaType = MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b\\c"));
        assertEquals("type/subtype; a=\"b\\\\c\"", mediaType.toString());
    }

    @Test
    public void testToStringWithParametersRequireQuotesDoubleQuoteChar() {
        final MediaType mediaType = MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b\"c"));
        assertEquals("type/subtype; a=\"b\\\"c\"", mediaType.toString());
    }

    // helpers........................................................................................................

    private MediaType mediaType() {
        return MediaType.with(TYPE, SUBTYPE, parameters(), "type/subtype;parameter123=value456");
    }

    private Map<MediaTypeParameterName, String> parameters() {
        return this.parameters("parameter123", "value456");
    }

    private Map<MediaTypeParameterName, String> parameters(final String name, final String value) {
        return Maps.one(MediaTypeParameterName.with(name), value);
    }

    private Map<MediaTypeParameterName, String> parameters(final String name, final String value, final String name2, final String value2) {
        final Map<MediaTypeParameterName, String> parameters = Maps.ordered();
        parameters.put(MediaTypeParameterName.with(name), value);
        parameters.put(MediaTypeParameterName.with(name2), value2);

        return parameters;
    }

    @Override
    protected Class<MediaType> type() {
        return MediaType.class;
    }
}
