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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptEncodingNonWildcardTest extends AcceptEncodingTestCase<AcceptEncodingNonWildcard> {

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            AcceptEncoding.with(null);
        });
    }

    @Test
    public void testWithEmptyValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncoding.with("");
        });
    }

    @Test
    public void testWithInvalidCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            AcceptEncoding.with("\u001f");
        });
    }

    @Test
    public void testWith() {
        this.checkValue(this.createHeaderValue());
    }

    @Test
    public void testWith2() {
        final String text = "unknown";
        this.checkValue(AcceptEncoding.with(text),
                text,
                AcceptEncoding.NO_PARAMETERS);
    }

    // constants ......................................................................................................

    @Test
    public void testWithExistingGzipNoParametersConstant() {
        final AcceptEncoding constant = AcceptEncoding.GZIP;
        assertSame(constant, AcceptEncoding.with("gzip"));
    }

    @Test
    public void testWithExistingBrConstant() {
        final AcceptEncoding constant = AcceptEncoding.BR;
        assertSame(constant, AcceptEncoding.with("BR"));
    }

    @Test
    public void testWithExistingCompressConstantCaseInsignificant() {
        final AcceptEncoding constant = AcceptEncoding.COMPRESS;
        assertSame(constant, AcceptEncoding.with("compRESS"));
    }

    // test.............................................................................................................

    @Test
    public void testTestContentEncodingSame() {
        this.testTrue(AcceptEncodingNonWildcard.with("GZIP"),
                ContentEncoding.GZIP);
    }

    @Test
    public void testTestContentEncodingDifferentCase() {
        this.testTrue(AcceptEncodingNonWildcard.with("XYZ"),
                ContentEncoding.with("xyz"));
    }

    @Test
    public void testTestContentEncodingSameIgnoresParameters() {
        this.testTrue(AcceptEncodingNonWildcard.with("XYZ").setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)),
                ContentEncoding.with("XYZ"));
    }

    @Test
    public void testTestContentEncodingDifferent() {
        this.testFalse(AcceptEncodingNonWildcard.with("DEFLATE"), ContentEncoding.GZIP);
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseAndCheck("gzip, deflate, br",
                Lists.of(AcceptEncoding.GZIP,
                        AcceptEncoding.DEFLATE,
                        AcceptEncoding.BR));
    }

    @Test
    public void testParseExtraWhitespace() {
        this.parseAndCheck("gzip,  deflate,  br",
                Lists.of(AcceptEncoding.GZIP,
                        AcceptEncoding.DEFLATE,
                        AcceptEncoding.BR));
    }

    @Test
    public void testHeaderText2() {
        final String text = "identity";
        this.toHeaderTextAndCheck(AcceptEncoding.with(text), text);
    }

    // equals ..........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(AcceptEncoding.with("different"));
    }

    // Comparison ......................................................................................................

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(AcceptEncoding.DEFLATE, AcceptEncoding.GZIP);
    }

    @Test
    public void testCompareLessCaseInsignificant() {
        this.compareToAndCheckLess(AcceptEncoding.with("abc"),
                AcceptEncoding.with("XYZ"));
    }

    // toString ........................................................................................................

    @Test
    public void testToString2() {
        final String text = "compress";
        this.toStringAndCheck(AcceptEncoding.with(text), text);
    }

    @Override
    public AcceptEncodingNonWildcard createHeaderValueWithParameters() {
        return this.createHeaderValue(this.value());
    }

    private AcceptEncodingNonWildcard createHeaderValue(final String value) {
        return AcceptEncoding.nonWildcard(value, AcceptEncodingNonWildcard.NO_PARAMETERS);
    }

    @Override
    String value() {
        return "gzip";
    }

    @Override
    Class<AcceptEncodingNonWildcard> acceptEncodingType() {
        return AcceptEncodingNonWildcard.class;
    }
}
