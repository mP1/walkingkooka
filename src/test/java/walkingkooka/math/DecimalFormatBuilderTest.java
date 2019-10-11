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

package walkingkooka.math;

import org.junit.jupiter.api.Test;
import walkingkooka.build.BuilderTesting;
import walkingkooka.text.HasTextTesting;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DecimalFormatBuilderTest implements BuilderTesting<DecimalFormatBuilder, DecimalFormat>, HasTextTesting {

    @Test
    public void testEmptyFails() {
        this.buildFails(DecimalFormatBuilder.empty());
    }

    @Test
    public void testRoundingModeNull() {
        assertThrows(NullPointerException.class, () -> DecimalFormatBuilder.empty().roundingMode(null));
    }

    @Test
    public void testRoundingMode() {
        Arrays.stream(RoundingMode.values())
                .forEach(r -> {
                    final DecimalFormatBuilder b = DecimalFormatBuilder.empty();
                    b.roundingMode(r);
                    assertSame(r, b.roundingMode, "roundingMode");
                });
    }

    @Test
    public void testCurrency() {
        this.appendAndCheck(DecimalFormatBuilder::currency, "\u00A4");
    }

    @Test
    public void testDecimalSeparator() {
        this.appendAndCheck(DecimalFormatBuilder::decimalSeparator, ".");
    }

    @Test
    public void testDigit() {
        this.appendAndCheck(DecimalFormatBuilder::digit, "#");
    }

    @Test
    public void testDigitOrZero() {
        this.appendAndCheck(DecimalFormatBuilder::digitOrZero, "0");
    }

    @Test
    public void testExponent() {
        this.appendAndCheck(DecimalFormatBuilder::exponent, "E");
    }

    @Test
    public void testGroupingSizeInvalidFails() {
        assertThrows(IllegalArgumentException.class, () -> DecimalFormatBuilder.empty().groupingSize(0));
    }

    @Test
    public void testGroupingSeparator() {
        this.appendAndCheck(DecimalFormatBuilder::groupingSeparator, ",");
    }

    @Test
    public void testLocaleNullFails() {
        assertThrows(NullPointerException.class, () -> DecimalFormatBuilder.empty().locale(null));
    }

    @Test
    public void testMultiplyBy1000() {
        this.appendAndCheck(DecimalFormatBuilder::multiplyBy1000, "\u2030");
    }

    @Test
    public void testNegativeSign() {
        this.appendAndCheck(DecimalFormatBuilder::negativeSign, "-");
    }

    @Test
    public void testPercent() {
        this.appendAndCheck(DecimalFormatBuilder::percentage, "%");
    }

    private void appendAndCheck(final Function<DecimalFormatBuilder, DecimalFormatBuilder> append, final String pattern) {
        final DecimalFormatBuilder builder = DecimalFormatBuilder.empty();
        assertSame(builder, append.apply(builder), "didnt return same Builder");
        this.textAndCheck(builder, pattern);
    }

    @Test
    public void testMultipleAppends() {
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .digit()
                .digit()
                .decimalSeparator()
                .digitOrZero()
                .digitOrZero();
        this.textAndCheck(b, "##.00");
    }

    @Test
    public void testNegativeSubPattern() {
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .digit()
                .digit()
                .negativeSubPattern()
                .digitOrZero()
                .digitOrZero();
        this.textAndCheck(b, "##;00");
    }

    public void testNegativeSubPatternTwiceFails() {
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .digit()
                .digit()
                .negativeSubPattern();
        assertThrows(IllegalArgumentException.class, b::negativeSubPattern);
    }

    @Test
    public void testPrefixNullFails() {
        assertThrows(NullPointerException.class, () -> DecimalFormatBuilder.empty().prefix(null));
    }

    @Test
    public void testPrefix() {
        final String prefix = "prefix-123";
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .prefix(prefix);
        assertEquals(prefix, b.prefix, "prefix");
    }

    @Test
    public void testPrefixKeepsLast() {
        final String prefix = "prefix-123";
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .prefix("last")
                .prefix(prefix);
        assertEquals(prefix, b.prefix, "prefix");
    }

    @Test
    public void testRoundingModeAndBuild() {
        Arrays.stream(RoundingMode.values())
                .forEach(r -> {
                    final DecimalFormatBuilder b = DecimalFormatBuilder.empty();
                    b.roundingMode(r);
                    b.digit();

                    final DecimalFormat decimalFormat = b.build();
                    assertEquals(r, decimalFormat.getRoundingMode());
                });
    }

    @Test
    public void testSuffixNullFails() {
        assertThrows(NullPointerException.class, () -> DecimalFormatBuilder.empty().suffix(null));
    }

    @Test
    public void testSuffix() {
        final String suffix = "suffix-123";
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .suffix(suffix);
        assertEquals(suffix, b.suffix, "suffix");
    }

    @Test
    public void testSuffixKeepsLast() {
        final String suffix = "suffix-123";
        final DecimalFormatBuilder b = DecimalFormatBuilder.empty()
                .suffix("lost")
                .suffix(suffix);
        assertEquals(suffix, b.suffix, "suffix");
    }

    @Test
    public void testBuildInvalidLocaleFails() {
        this.buildFails(DecimalFormatBuilder.empty().locale(Locale.ENGLISH));
    }

    @Test
    public void testFormat() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .digit()
                        .digitOrZero()
                        .decimalSeparator()
                        .digitOrZero()
                        .digitOrZero(),
                12.5,
                "12.50");
    }

    @Test
    public void testFormatWithPrefix() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .digit()
                        .prefix("abc"),
                12.5,
                "abc12");
    }

    @Test
    public void testFormatWithPrefixRequiresEscaping() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .digit()
                        .prefix("ab'c"),
                12.5,
                "ab'c12");
    }

    @Test
    public void testFormatWithSuffix() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .digit()
                        .suffix("xyz"),
                12.5,
                "12xyz");
    }

    @Test
    public void testFormatWithSuffixRequiresEscaping() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .digit()
                        .suffix("x'yz"),
                12.5,
                "12x'yz");
    }

    @Test
    public void testFormatWithCurrency() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .currency()
                        .digit()
                        .digitOrZero()
                        .decimalSeparator()
                        .digitOrZero()
                        .digitOrZero(),
                12.5,
                "$12.50");
    }

    @Test
    public void testFormatWithLocaleUK() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .currency()
                        .groupingSeparator()
                        .groupingSize(3)
                        .digit()
                        .decimalSeparator()
                        .digitOrZero()
                        .digitOrZero()
                        .locale(Locale.UK),
                1234.56,
                "£1,234.56");
    }

    @Test
    public void testFormatWithLocaleGermany() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .currency()
                        .groupingSeparator()
                        .groupingSize(3)
                        .digit()
                        .decimalSeparator()
                        .digitOrZero()
                        .digitOrZero()
                        .locale(Locale.GERMANY),
                1234.56,
                "€1.234,56");
    }

    @Test
    public void testFormatWithPositiveAndNegative() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .prefix("A")
                        .digit()
                        .suffix("B")
                        .negativeSubPattern()
                        .prefix("C")
                        .digit()
                        .suffix("D"),
                12,
                "A12B");
    }

    @Test
    public void testFormatWithPositiveAndNegative2() {
        this.buildAndFormat(DecimalFormatBuilder.empty()
                        .prefix("A")
                        .digit()
                        .suffix("B")
                        .negativeSubPattern()
                        .prefix("C")
                        .digit()
                        .negativeSign()
                        .suffix("D"),
                -12,
                "C12-D");
    }

    private void buildAndFormat(final DecimalFormatBuilder builder,
                                final Number number,
                                final String text) {
        assertEquals(text, builder.build().format(number), () -> "format " + number + " using " + builder);
    }

    // toString..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(DecimalFormatBuilder.empty()
                        .digit()
                        .digitOrZero()
                        .decimalSeparator()
                        .digitOrZero()
                        .digitOrZero(),
                "#0.00");
    }

    // Builder..........................................................................................................

    @Override
    public DecimalFormatBuilder createBuilder() {
        return DecimalFormatBuilder.empty();
    }

    @Override
    public Class<DecimalFormat> builderProductType() {
        return DecimalFormat.class;
    }

    @Override
    public Class<DecimalFormatBuilder> type() {
        return DecimalFormatBuilder.class;
    }
}
