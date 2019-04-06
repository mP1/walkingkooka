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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.PredicateTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class MediaTypeTest extends HeaderValueWithParametersTestCase<MediaType, MediaTypeParameterName<?>>
        implements ParseStringTesting<MediaType>,
        PredicateTesting<MediaType, MediaType> {

    // constants

    private final static String TYPE = "type";
    private final static String SUBTYPE = "subtype";
    private final static String PARAMETER_NAME = "parameter123";
    private final static String PARAMETER_VALUE = "value456";

    // tests

    @Override
    public void testTypeNaming() {
    }

    @Test
    public void testNullTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaType.with(null, SUBTYPE);
        });
    }

    @Test
    public void testEmptyTypeAndSubTypeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with("", SUBTYPE);
        });
    }

    @Test
    public void testTypeContainsSlashFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with("ty/pe", SUBTYPE);
        });
    }

    @Test
    public void testTypeContainsInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with("ty?pe", SUBTYPE);
        });
    }

    @Test
    public void testNullSubTypeFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaType.with(TYPE, null);
        });
    }

    @Test
    public void testTypeAndEmptySubTypeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with(TYPE, "");
        });
    }

    @Test
    public void testEmptyIncludesSlashFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with(TYPE, "s/ub");
        });
    }

    @Test
    public void testEmptyIncludesInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.with(TYPE, "s?ub");
        });
    }

    @Test
    public void testWithTypeAndSubType() {
        check(MediaType.with(TYPE, SUBTYPE), TYPE, SUBTYPE);
    }

    @Test
    public void testWithTypeAndWildcardSubType() {
        check(MediaType.with(TYPE, MediaType.WILDCARD.string()),
                TYPE,
                MediaType.WILDCARD.string());
    }

    @Test
    public void testWithWildcardTypeAndWildcardSubType() {
        final String wildcard = MediaType.WILDCARD.string();
        check(MediaType.with(wildcard, wildcard),
                wildcard,
                wildcard);
    }

    // constants .........................................................................

    @Test
    public void testWithExistingConstant() {
        final MediaType constant = MediaType.APPLICATION_JAVASCRIPT;
        assertSame(constant, MediaType.with(constant.type().toUpperCase(), constant.subType().toUpperCase()));
    }

    @Test
    public void testWithExistingConstant2() {
        final MediaType constant = MediaType.APPLICATION_JAVASCRIPT;
        assertSame(constant, MediaType.with(constant.type().toLowerCase(), constant.subType().toLowerCase()));
    }

    // setType .........................................................................

    @Test
    public void testSetTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.mediaType().setType(null);
        });
    }

    @Test
    public void testSetTypeEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setType("");
        });
    }

    @Test
    public void testSetTypeInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setType("type/");
        });
    }

    @Test
    public void testSetTypeInvalidCharacterFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setType("type?");
        });
    }

    @Test
    public void testSetTypeSame() {
        final MediaType mediaType = this.mediaType();
        assertSame(mediaType, mediaType.setType(TYPE));
    }

    @Test
    public void testSetTypeSameDifferentCase() {
        final MediaType mediaType = this.mediaType();
        assertNotEquals(TYPE, TYPE.toUpperCase());
        assertSame(mediaType, mediaType.setType(TYPE.toUpperCase()));
    }

    @Test
    public void testSetTypeDifferent() {
        final MediaType mediaType = this.mediaType();
        final String type = "different";
        final MediaType different = mediaType.setType(type);
        check(different, type, SUBTYPE, parameters());
    }

    // setSubType ..........................................................................................

    @Test
    public void testSetSubTypeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.mediaType().setSubType(null);
        });
    }

    @Test
    public void testSetSubTypeEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setSubType("");
        });
    }

    @Test
    public void testSetSubTypeInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setSubType("type/");
        });
    }

    @Test
    public void testSetSubTypeInvalidCharacterFails2() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.mediaType().setSubType("type?");
        });
    }

    @Test
    public void testSetSubTypeSame() {
        final MediaType mediaType = this.mediaType();
        assertSame(mediaType, mediaType.setSubType(SUBTYPE));
    }

    @Test
    public void testSetSubTypeSameDifferentCase() {
        final MediaType mediaType = this.mediaType();
        assertNotEquals(SUBTYPE, SUBTYPE.toUpperCase());
        assertSame(mediaType, mediaType.setSubType(SUBTYPE.toUpperCase()));
    }

    @Test
    public void testSetSubTypeDifferent() {
        final MediaType mediaType = this.mediaType();
        final String subtype = "different";
        final MediaType different = mediaType.setSubType(subtype);
        check(different, TYPE, subtype, parameters());
    }

    // setParameters ..........................................................................................

    @Test
    public void testSetParametersInvalidQWeight() {
        assertThrows(HeaderValueException.class, () -> {
            this.mediaType().setParameters(Maps.of(MediaTypeParameterName.Q_FACTOR, -1.0f));
        });
    }

    @Test
    public void testSetParametersSameDifferentCase() {
        final MediaType mediaType = this.mediaType();

        final String parameter = PARAMETER_NAME.toUpperCase();
        assertNotEquals(parameter, PARAMETER_NAME);
        assertSame(mediaType, mediaType.setParameters(this.parameters(parameter, PARAMETER_VALUE)));
    }

    @Test
    public void testSetParametersDifferent() {
        final MediaType mediaType = this.mediaType();
        final Map<MediaTypeParameterName<?>, Object> parameters = MediaType.NO_PARAMETERS;
        final MediaType different = mediaType.setParameters(parameters);
        check(different, TYPE, SUBTYPE, parameters);
    }

    @Test
    public void testSetParametersDifferent2() {
        final MediaType mediaType = this.mediaType();
        final Map<MediaTypeParameterName<?>, Object> parameters = Maps.of(MediaTypeParameterName.with("different"), "value789");
        final MediaType different = mediaType.setParameters(parameters);
        check(different, TYPE, SUBTYPE, parameters);
    }

    private void check(final MediaType mediaType, final String type, final String subtype) {
        check(mediaType, type, subtype, MediaType.NO_PARAMETERS);
    }

    private void check(final MediaType mediaType,
                       final String type,
                       final String subtype,
                       final Map<MediaTypeParameterName<?>, Object> parameters) {
        assertEquals(type, mediaType.type(), "type=" + mediaType);
        assertEquals(subtype, mediaType.subType(), "subType=" + mediaType);
        assertEquals(parameters, mediaType.parameters(), "parameters=" + mediaType);
    }

    @Test
    public void testSetParametersConstant() {
        final MediaType constant = MediaType.TEXT_PLAIN;
        final MediaType withParameters = constant.setParameters(this.parameters());
        assertNotEquals(constant, withParameters);
        assertSame(constant, withParameters.setParameters(MediaType.NO_PARAMETERS));
    }

    // setCharset .......................................................................

    @Test
    public void testSetCharsetNullFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaType.TEXT_PLAIN.setCharset(null);
        });
    }

    @Test
    public void testSetCharset() {
        final CharsetName utf8 = CharsetName.UTF_8;
        this.check(MediaType.TEXT_PLAIN.setCharset(utf8),
                "text",
                "plain",
                Maps.of(MediaTypeParameterName.CHARSET, utf8));
    }

    @Test
    public void testSetCharsetSame() {
        final CharsetName utf8 = CharsetName.UTF_8;
        final MediaType textPlainWithCharset = MediaType.TEXT_PLAIN.setCharset(utf8);
        assertSame(textPlainWithCharset, textPlainWithCharset.setCharset(utf8));
    }

    @Test
    public void testSetCharsetDifferent() {
        final CharsetName utf8 = CharsetName.UTF_8;
        final MediaType with8 = MediaType.TEXT_PLAIN.setCharset(utf8);

        final CharsetName utf16 = CharsetName.UTF_16;
        final MediaType with16 = with8.setCharset(utf16);
        assertNotSame(with8, with16);

        this.check(with16,
                "text",
                "plain",
                Maps.of(MediaTypeParameterName.CHARSET, utf16));
        this.check(with8,
                "text",
                "plain",
                Maps.of(MediaTypeParameterName.CHARSET, utf8));
    }

    @Test
    public void testSetCharsetDifferent2() {
        final Map<MediaTypeParameterName<?>, Object> parameters = Maps.of(MediaTypeParameterName.CHARSET, CharsetName.UTF_8,
                MediaTypeParameterName.Q_FACTOR, 0.5f);

        final MediaType with8 = MediaType.TEXT_PLAIN.setParameters(parameters);

        final CharsetName utf16 = CharsetName.UTF_16;
        final MediaType with16 = with8.setCharset(utf16);

        assertNotSame(with8, with16);

        final Map<MediaTypeParameterName<?>, Object> parameters2 = Maps.of(MediaTypeParameterName.CHARSET, utf16,
                MediaTypeParameterName.Q_FACTOR, 0.5f);

        this.check(with16,
                "text",
                "plain",
                parameters2);

        this.check(with8,
                "text",
                "plain",
                parameters);
    }

    @Test
    public void testSetCharset2() {
        final Map<MediaTypeParameterName<?>, Object> parameters = Maps.of(MediaTypeParameterName.Q_FACTOR, 0.5f);
        final MediaType without = MediaType.TEXT_PLAIN.setParameters(parameters);

        final CharsetName charset = CharsetName.UTF_16;
        final MediaType with16 = without.setCharset(charset);

        assertNotSame(without, with16);

        this.check(with16,
                "text",
                "plain",
                Maps.of(MediaTypeParameterName.CHARSET, charset,
                        MediaTypeParameterName.Q_FACTOR, 0.5f));

        this.check(without,
                "text",
                "plain",
                parameters);
    }

    // qWeight .......................................................................

    @Test
    public void testQParameterPresent() {
        this.qFactorWeightAndCheck(this.mediaType()
                        .setParameters(parameters(MediaTypeParameterName.Q_FACTOR.value(), 0.5f)),
                0.5f);
    }

    @Test
    public void testQParameterPresentInvalidFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.mediaType().setParameters(parameters(MediaTypeParameterName.Q_FACTOR.value(), "XYZ")).qFactorWeight();
        });
    }

    @Test
    public void testQParameterAbsent() {
        this.qFactorWeightAndCheck(this.mediaType(), MediaType.Q_FACTOR_WEIGHT_ABSENT);
    }

    private void qFactorWeightAndCheck(final MediaType type, final float weight) {
        qFactorWeightAndCheck(type, Optional.of(weight));
    }

    private void qFactorWeightAndCheck(final MediaType type, final Optional<Float> weight) {
        assertEquals(weight, type.qFactorWeight(), ()-> type + " q weight");
    }

    // parse .........................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("type1/subtype1",
                MediaType.with("type1", "subtype1"));
    }

    @Test
    public void testParseWithUnquotedParameter() {
        this.parseAndCheck("type1/subtype1;abc=def",
                MediaType.with("type1", "subtype1")
                        .setParameters(Maps.of(MediaTypeParameterName.with("abc"), "def")));
    }

    @Test
    public void testParseWithQuotedParameter() {
        this.parseAndCheck("type1/subtype1;abc=\"d,\\\\ef\"",
                MediaType.with("type1", "subtype1")
                        .setParameters(Maps.of(MediaTypeParameterName.with("abc"), "d,\\ef")));
    }

    @Test
    public void testParseWithBoundary() {
        this.parseAndCheck("type1/subtype1;boundary=def",
                MediaType.with("type1", "subtype1")
                        .setParameters(Maps.of(MediaTypeParameterName.BOUNDARY, MediaTypeBoundary.with("def"))));
    }

    @Test
    public void testParseWithTitleStar() {
        this.parseAndCheck("type1/subtype1;title*=UTF-8''abc%20123",
                MediaType.with("type1", "subtype1")
                        .setParameters(Maps.of(MediaTypeParameterName.TITLE_STAR, EncodedText.with(CharsetName.UTF_8, EncodedText.NO_LANGUAGE, "abc 123"))));
    }

    // ParseStringTesting ........................................................................................

    @Override
    public MediaType parse(final String text) {
        return MediaType.parse(text);
    }

    // ParseList ........................................................................................

    @Test
    public void testParseListNullFails() {
        assertThrows(NullPointerException.class, () -> {
            MediaType.parseList(null);
        });
    }

    @Test
    public void testParseListEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            MediaType.parseList("");
        });
    }

    @Test
    public void testParseList() {
        assertEquals(Lists.of(
                MediaType.with("type1", "subtype1"),
                MediaType.with("type2", "subtype2")),
                MediaType.parseList("type1/subtype1,type2/subtype2"));
    }

    // test .........................................................................

    @Test
    public void testTestWithSelfDifferentCase() {
        this.testTrue(MediaType.with("type", "subtype"),
                MediaType.with("TYPE", "SUBTYPE"));
    }

    @Test
    public void testTestAnyAlwaysMatches() {
        this.testTrue(MediaType.ALL, MediaType.with("custom", "custom2"));
    }

    @Test
    public void testTestAnyAndAnyMatches() {
        this.testTrue(MediaType.ALL, MediaType.ALL);
    }

    @Test
    public void testTestSameTypeWildcardSubType() {
        final String type = "custom";
        this.testTrue(MediaType.with(type, MediaType.WILDCARD.string()),
                MediaType.with(type, SUBTYPE));
    }

    @Test
    public void testTestDifferentTypeWildcardSubType() {
        this.testFalse(MediaType.with("custom", MediaType.WILDCARD.string()),
                MediaType.with("different", SUBTYPE));
    }

    @Test
    public void testTestSameTypeSubType() {
        this.testTrue(this.mediaType(), this.mediaType());
    }

    @Test
    public void testTestDifferentTypeSameSubType() {
        this.testFalse(this.mediaType(), MediaType.with("different", SUBTYPE));
    }

    @Test
    public void testTestSameTypeDifferentSubType() {
        final String type = "type";
        this.testFalse(MediaType.with(type, "subtype"), MediaType.with(type, "different"));
    }

    // toHeaderText........................................................................................................

    @Test
    public void testToHeaderTextParse() {
        final String text = "type/subtype";

        this.toHeaderTextAndCheck(MediaType.parse(text),
                TYPE + "/" + SUBTYPE);
    }

    @Test
    public void testToHeaderTextParseWithParameters() {
        this.toHeaderTextAndCheck(MediaType.parse("type/subtype;a=b;c=d"),
                "type/subtype; a=b; c=d");
    }

    @Test
    public void testToHeaderTextParseWithParametersWithQuotes() {
        this.toHeaderTextAndCheck(MediaType.parse("type/subtype;a=b;c=\"d e\""),
                "type/subtype; a=b; c=\"d e\"");
    }

    @Test
    public void testToHeaderText() {
        this.toHeaderTextAndCheck(MediaType.with(TYPE, SUBTYPE),
                TYPE + "/" + SUBTYPE);
    }

    @Test
    public void testToHeaderTextWithParameters() {
        this.toHeaderTextAndCheck(mediaType(), TYPE + "/" + SUBTYPE + "; parameter123=value456");
    }

    @Test
    public void testToHeaderTextWithParametersRequireQuotesWhitespace() {
        this.toHeaderTextAndCheck(MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b c")),
                "type/subtype; a=\"b c\"");
    }

    @Test
    public void testToHeaderTextWithParametersRequireQuotesBackslash() {
        this.toHeaderTextAndCheck(MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b\\c")),
                "type/subtype; a=\"b\\\\c\"");
    }

    @Test
    public void testIsWildcardAll() {
        this.isWildcardAndCheck(MediaType.ALL, true);
    }

    @Test
    public void testIsWildcardNotAll() {
        this.isWildcardAndCheck(false);
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsTypeDifferentCase() {
        this.checkEquals(MediaType.with("major", "minor"),
                MediaType.with("MAJOR", "minor"));
    }

    @Test
    public void testEqualsSubTypeDifferentCase() {
        this.checkEquals(MediaType.with("major", "MINOR"),
                MediaType.parse("major/MINOR"));
    }

    @Test
    public void testEqualsParameterNameDifferentCase() {
        this.checkEquals(MediaType.parse("type/subType; parameter123=value456"),
                MediaType.parse("type/subType; PARAMETER123=value456"));
    }

    @Test
    public void testEqualsParameterValueDifferentCase() {
        this.checkNotEquals(MediaType.parse("type/subType; parameter123=value456"),
                MediaType.parse("type/subType; parameter123=VALUE456"));
    }

    @Test
    public void testEqualsDifferentMimeType() {
        this.checkNotEquals(MediaType.parse("major/different"));
    }

    @Test
    public void testEqualsDifferentParameter() {
        this.checkNotEquals(MediaType.parse("major/minor;parameter=value"));
    }

    @Test
    public void testEqualsDifferentParameter2() {
        checkNotEquals(MediaType.parse("major/minor;parameter=value"),
                MediaType.parse("major/minor;different=value"));
    }

    @Test
    public void testEqualsDifferentParameterOrderStillEqual() {
        checkEqualsAndHashCode(MediaType.parse("a/b;x=1;y=2"),
                MediaType.parse("a/b;y=2;x=1"));
    }

    @Test
    public void testEqualsParsedAndBuild() {
        checkEquals(MediaType.with("major", "minor"), MediaType.parse("major/minor"));
    }

    @Test
    public void testEqualsParameterOrderIrrelevant() {
        checkEquals(MediaType.parse("type/subtype;a=1;b=2;c=3"), MediaType.parse("type/subtype;c=3;b=2;a=1"));
    }

    @Test
    public void testEqualsDifferentWhitespaceSameParametersStillEqual() {
        checkEqualsAndHashCode(MediaType.parse("a/b;   x=1"), MediaType.parse("a/b;x=1"));
    }
    
    // toString........................................................................................................

    @Test
    public void testToStringParse() {
        final String text = "type/subtype";
        this.toStringAndCheck(MediaType.parse(text), text);
    }

    @Test
    public void testToStringParseWithParameters() {
        this.toStringAndCheck(MediaType.parse("type/subtype;a=b;c=d"),
                "type/subtype; a=b; c=d");
    }

    @Test
    public void testToStringParseWithParametersWithQuotes() {
        this.toStringAndCheck(MediaType.parse("type/subtype;a=b;c=\"d e\""),
                "type/subtype; a=b; c=\"d e\"");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(MediaType.with(TYPE, SUBTYPE),
                TYPE + "/" + SUBTYPE);
    }

    @Test
    public void testToStringWithParameters() {
        this.toStringAndCheck(mediaType(), TYPE + "/" + SUBTYPE + "; parameter123=value456");
    }

    @Test
    public void testToStringWithParametersRequireQuotesWhitespace() {
        this.toStringAndCheck(MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b c")),
                "type/subtype; a=\"b c\"");
    }

    @Test
    public void testToStringWithParametersRequireQuotesBackslash() {
        this.toStringAndCheck(MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b\\c")),
                "type/subtype; a=\"b\\\\c\"");
    }

    @Test
    public void testToStringWithParametersRequireQuotesDoubleQuoteChar() {
        this.toStringAndCheck(MediaType.with(TYPE, SUBTYPE).setParameters(parameters("a", "b\"c")),
                "type/subtype; a=\"b\\\"c\"");
    }

    // toHeaderTextList...............................................................................................

    @Test
    public void testToHeaderTextListOne() {
        this.toHeaderTextListAndCheck("type1/subtype1",
                MediaType.with("type1", "subtype1"));
    }

    @Test
    public void testToHeaderTextListMany() {
        this.toHeaderTextListAndCheck("type1/subtype1, type2/subtype2",
                MediaType.with("type1", "subtype1"),
                MediaType.with("type2", "subtype2"));
    }

    // helpers........................................................................................................

    @Override
    public MediaType createHeaderValueWithParameters() {
        return this.mediaType();
    }

    @Override
    public MediaType createPredicate() {
        return this.mediaType();
    }

    private MediaType mediaType() {
        return MediaType.withParameters(TYPE, SUBTYPE, parameters());
    }

    private Map<MediaTypeParameterName<?>, Object> parameters() {
        return this.parameters(PARAMETER_NAME, PARAMETER_VALUE);
    }

    private Map<MediaTypeParameterName<?>, Object> parameters(final String name, final Object value) {
        return Maps.of(MediaTypeParameterName.with(name), value);
    }

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // ClassTesting....................................................................................................

    @Override
    public Class<MediaType> type() {
        return MediaType.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
