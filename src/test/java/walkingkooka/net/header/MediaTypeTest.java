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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.PublicClassTestCase;

import java.util.Map;
import java.util.Optional;

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

    // qWeight .......................................................................

    @Test
    public void testQParameterPresent() {
        this.qFactorWeightAndCheck(this.mediaType().setParameters(parameters(MediaTypeParameterName.Q_FACTOR.value(), "0.5")), 0.5f);
    }

    @Test
    public void testQParameterPresentInvalidFails() {
        try {
            this.mediaType().setParameters(parameters(MediaTypeParameterName.Q_FACTOR.value(), "XYZ")).qFactorWeight();
            fail("Getter should have failed due to invalid q weight");
        } catch (final IllegalStateException expected) {
            assertEquals("Invalid q weight parameter XYZ in type/subtype; q=XYZ", expected.getMessage());
        }
    }

    @Test
    public void testQParameterAbsent() {
        this.qFactorWeightAndCheck(this.mediaType(), MediaType.Q_FACTOR_WEIGHT_ABSENT);
    }

    private void qFactorWeightAndCheck(final MediaType type, final float weight) {
        qFactorWeightAndCheck(type, Optional.of(weight));
    }

    private void qFactorWeightAndCheck(final MediaType type, final Optional<Float> weight) {
        assertEquals(type + " q weight", weight, type.qFactorWeight());
    }

    // parse .........................................................................

    @Test(expected = NullPointerException.class)
    public void testParseOneNullFails() {
        MediaType.parseOne(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOneEmptyFails() {
        MediaType.parseOne("");
    }

    @Test
    public void testParseOne() {
        assertEquals(MediaType.with("type1", "subtype1"), MediaType.parseOne("type1/subtype1"));
    }

    @Test(expected = NullPointerException.class)
    public void testParseOneOrManyNullFails() {
        MediaType.parseMany(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseOneOrManyEmptyFails() {
        MediaType.parseMany("");
    }

    @Test
    public void testParseOneOrMany() {
        assertEquals(Lists.of(
                MediaType.with("type1", "subtype1"),
                MediaType.with("type2", "subtype2")),
                MediaType.parseMany("type1/subtype1,type2/subtype2"));
    }

    // isCompatible .........................................................................

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
        assertEquals(text, MediaType.parseOne(text).toString());
    }

    @Test
    public void testToStringParseWithParameters() {
        final String text = "type/subtype;a=b;c=d";
        assertEquals(text, MediaType.parseOne(text).toString());
    }

    @Test
    public void testToStringParseWithParametersWithQuotes() {
        final String text = "type/subtype;a=b;c=\"d\"";
        assertEquals(text, MediaType.parseOne(text).toString());
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

    @Override
    protected Class<MediaType> type() {
        return MediaType.class;
    }
}
