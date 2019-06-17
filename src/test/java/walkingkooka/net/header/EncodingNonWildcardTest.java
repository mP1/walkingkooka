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

public final class EncodingNonWildcardTest extends EncodingTestCase<EncodingNonWildcard> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            Encoding.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Encoding.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            Encoding.with("\u001f");
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(Encoding.with(text),
                text,
                Encoding.NO_PARAMETERS);
    }

    // constants ......................................................................................................

    @Test
    public void testWithExistingGzipNoParametersConstant() {
        final Encoding constant = Encoding.GZIP;
        assertSame(constant, Encoding.with("gzip"));
    }

    @Test
    public void testWithExistingBrConstant() {
        final Encoding constant = Encoding.BR;
        assertSame(constant, Encoding.with("BR"));
    }

    @Test
    public void testWithExistingCompressConstantCaseInsignificant() {
        final Encoding constant = Encoding.COMPRESS;
        assertSame(constant, Encoding.with("compRESS"));
    }

    // test.............................................................................................................

    @Test
    public void testTestContentEncodingSame() {
        this.testTrue(EncodingNonWildcard.with("GZIP"),
                ContentEncoding.GZIP);
    }

    @Test
    public void testTestContentEncodingDifferentCase() {
        this.testTrue(EncodingNonWildcard.with("XYZ"),
                ContentEncoding.with("xyz"));
    }

    @Test
    public void testTestContentEncodingSameIgnoresParameters() {
        this.testTrue(EncodingNonWildcard.with("XYZ").setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)),
                ContentEncoding.with("XYZ"));
    }

    @Test
    public void testTestContentEncodingDifferent() {
        this.testFalse(EncodingNonWildcard.with("DEFLATE"), ContentEncoding.GZIP);
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("gzip",
                Encoding.GZIP);
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("gzip ",
                Encoding.GZIP);
    }

    @Test
    public void testParseTokenParameters() {
        this.parseAndCheck("abc;qrs=xyz",
                Encoding.with("abc").setParameters(Maps.of(EncodingParameterName.with("qrs"), "xyz")));
    }

    @Test
    public void testHeaderText2() {
        final String text = "identity";
        this.toHeaderTextAndCheck(Encoding.with(text), text);
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(Encoding.with("different"));
    }

    // Comparison ......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(Encoding.DEFLATE, Encoding.GZIP);
    }

    @Test
    public void testCompareLessCaseInsignificant() {
        this.compareToAndCheckLess(Encoding.with("abc"),
                Encoding.with("XYZ"));
    }

    // toString ........................................................................................................

    @Test
    public void testToString2() {
        final String text = "compress";
        this.toStringAndCheck(Encoding.with(text), text);
    }

    @Override
    public EncodingNonWildcard createHeaderValueWithParameters() {
        return this.createHeaderValue(this.value());
    }

    private EncodingNonWildcard createHeaderValue(final String value) {
        return Encoding.nonWildcard(value, EncodingNonWildcard.NO_PARAMETERS);
    }

    @Override
    String value() {
        return "gzip";
    }

    @Override
    Class<EncodingNonWildcard> encodingType() {
        return EncodingNonWildcard.class;
    }
}
