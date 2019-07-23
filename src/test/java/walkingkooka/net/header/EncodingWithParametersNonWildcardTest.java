/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.map.Maps;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EncodingWithParametersNonWildcardTest extends EncodingWithParametersTestCase<EncodingWithParametersNonWildcard> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            EncodingWithParameters.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            EncodingWithParameters.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            EncodingWithParameters.with("\u001f");
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(EncodingWithParameters.with(text),
                text,
                EncodingWithParameters.NO_PARAMETERS);
    }

    // constants ......................................................................................................

    @Test
    public void testWithExistingGzipNoParametersConstant() {
        final EncodingWithParameters constant = EncodingWithParameters.GZIP;
        assertSame(constant, EncodingWithParameters.with("gzip"));
    }

    @Test
    public void testWithExistingBrConstant() {
        final EncodingWithParameters constant = EncodingWithParameters.BR;
        assertSame(constant, EncodingWithParameters.with("BR"));
    }

    @Test
    public void testWithExistingCompressConstantCaseInsignificant() {
        final EncodingWithParameters constant = EncodingWithParameters.COMPRESS;
        assertSame(constant, EncodingWithParameters.with("compRESS"));
    }

    // test.............................................................................................................

    @Test
    public void testTestContentEncodingSame() {
        this.testTrue(EncodingWithParametersNonWildcard.with("GZIP"),
                ContentEncoding.GZIP);
    }

    @Test
    public void testTestContentEncodingDifferentCase() {
        this.testTrue(EncodingWithParametersNonWildcard.with("XYZ"),
                ContentEncoding.parse("xyz"));
    }

    @Test
    public void testTestContentEncodingSameIgnoresParameters() {
        this.testTrue(EncodingWithParametersNonWildcard.with("XYZ").setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)),
                ContentEncoding.parse("XYZ"));
    }

    @Test
    public void testTestContentEncodingDifferent() {
        this.testFalse(EncodingWithParametersNonWildcard.with("DEFLATE"), ContentEncoding.GZIP);
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("gzip",
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("gzip ",
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testParseTokenParameters() {
        this.parseAndCheck("abc;qrs=xyz",
                EncodingWithParameters.with("abc").setParameters(Maps.of(EncodingParameterName.with("qrs"), "xyz")));
    }

    @Test
    public void testHeaderText2() {
        final String text = "identity";
        this.toHeaderTextAndCheck(EncodingWithParameters.with(text), text);
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(EncodingWithParameters.with("different"));
    }

    // Comparison ......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(EncodingWithParameters.DEFLATE, EncodingWithParameters.GZIP);
    }

    @Test
    public void testCompareLessCaseInsignificant() {
        this.compareToAndCheckLess(EncodingWithParameters.with("abc"),
                EncodingWithParameters.with("XYZ"));
    }

    // toString ........................................................................................................

    @Test
    public void testToString2() {
        final String text = "compress";
        this.toStringAndCheck(EncodingWithParameters.with(text), text);
    }

    @Override
    public EncodingWithParametersNonWildcard createHeaderValueWithParameters() {
        return this.createHeaderValue(this.value());
    }

    private EncodingWithParametersNonWildcard createHeaderValue(final String value) {
        return EncodingWithParameters.nonWildcard(value, EncodingWithParametersNonWildcard.NO_PARAMETERS);
    }

    @Override
    String value() {
        return "gzip";
    }

    @Override
    Class<EncodingWithParametersNonWildcard> encodingType() {
        return EncodingWithParametersNonWildcard.class;
    }
}
