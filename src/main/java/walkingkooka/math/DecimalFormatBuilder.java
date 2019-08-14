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

import walkingkooka.build.Builder;
import walkingkooka.build.BuilderException;
import walkingkooka.text.HasText;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

/**
 * A {@link Builder} which constructs {@link DecimalFormat} by building a {@link String pattern}.<br>
 * <a href="https://docs.oracle.com/javase/9/docs/api/java/text/DecimalFormat.html">
 * <pre>
 * Symbol	Location	Localized?	Meaning
 * 0	Number	Yes	Digit
 * #	Number	Yes	Digit, zero shows as absent
 * .	Number	Yes	Decimal separator or monetary decimal separator
 * -	Number	Yes	Minus sign
 * ,	Number	Yes	Grouping separator
 * E	Number	Yes	Separates mantissa and exponent in scientific notation. Need not be quoted in prefix or suffix.
 * ;	Subpattern boundary	Yes	Separates positive and negative subpatterns
 * %	Prefix or suffix	Yes	Multiply by 100 and show as percentage
 * \u2030	Prefix or suffix	Yes	Multiply by 1000 and show as per mille value
 * ¤ (\u00A4)	Prefix or suffix	No	Currency sign, replaced by currency symbol. If doubled, replaced by international currency symbol. If present in a pattern, the monetary decimal separator is used instead of the decimal separator.
 * '	Prefix or suffix	No	Used to quote special characters in a prefix or suffix, for example, "'#'#" formats 123 to "#123". To create a single quote itself, use two in a row: "# o''clock".
 * </pre>
 */
public final class DecimalFormatBuilder implements Builder<DecimalFormat>, HasText {

    /**
     * Creates an empty {@link DecimalFormatBuilder}.
     */
    public static DecimalFormatBuilder empty() {
        return new DecimalFormatBuilder();
    }

    private DecimalFormatBuilder() {
        super();
    }

    public DecimalFormatBuilder currency() {
        return this.append('\u00a4');
    }

    public DecimalFormatBuilder decimalSeparator() {
        return this.append('.');
    }

    public DecimalFormatBuilder digit() {
        return this.append('#');
    }

    public DecimalFormatBuilder digitOrZero() {
        return this.append('0');
    }

    public DecimalFormatBuilder exponent() {
        return this.append('E');
    }

    public DecimalFormatBuilder groupingSeparator() {
        return this.append(',');
    }

    public DecimalFormatBuilder groupingSize(final int groupingSize) {
        if (groupingSize < 1) {
            throw new IllegalArgumentException("Grouping size " + groupingSize + " < 1");
        }
        this.groupingSize = groupingSize;
        return this;
    }

    public DecimalFormatBuilder locale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        this.locale = locale;
        return this;
    }

    public DecimalFormatBuilder multiplyBy1000() {
        return this.append('\u2030');
    }

    public DecimalFormatBuilder negativeSign() {
        return this.append('-');
    }

    public DecimalFormatBuilder negativeSubPattern() {
        if (this.patternStart > 0) {
            throw new IllegalArgumentException("Already within negative sub pattern " + this.text());
        }

        this.pattern.insert(0, this.prefix);
        this.pattern.append(this.suffix);
        this.append(';');

        this.patternStart = this.pattern.length();

        this.prefix = "";
        this.suffix = "";

        return this;
    }

    private int patternStart = 0;

    public DecimalFormatBuilder percentage() {
        return this.append('%');
    }

    public DecimalFormatBuilder prefix(final String prefix) {
        Objects.requireNonNull(prefix, "prefix");
        this.prefix = escape(prefix);
        return this;
    }

    public DecimalFormatBuilder roundingMode(final RoundingMode roundingMode) {
        Objects.requireNonNull(roundingMode, "roundingMode");

        this.roundingMode = roundingMode;
        return this;
    }

    public DecimalFormatBuilder suffix(final String suffix) {
        Objects.requireNonNull(suffix, "suffix");
        this.suffix = escape(suffix);
        return this;
    }

    private static String escape(final String text) {
        return text.replace("'", "''");
    }

    // Builder..........................................................................................................

    /**
     * Builds a new {@link DecimalFormat}
     */
    @Override
    public DecimalFormat build() throws BuilderException {
        final StringBuilder pattern = this.pattern;
        if (pattern.length() == 0) {
            throw new BuilderException("Pattern empty");
        }

        pattern.insert(this.patternStart, this.prefix);
        pattern.append(this.suffix);

        final DecimalFormat decimalFormat = new DecimalFormat(pattern.toString());

        final int groupingSize = this.groupingSize;
        if (-1 != groupingSize) {
            decimalFormat.setGroupingSize(groupingSize);
        }

        final Locale locale = this.locale;
        if (null != locale) {
            try {
                decimalFormat.setCurrency(Currency.getInstance(locale));
            } catch (final IllegalArgumentException cause) {
                throw new BuilderException("Invalid locale " + locale.toLanguageTag());
            }
            decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(locale));
        }

        final RoundingMode roundingMode = this.roundingMode;
        if (null != roundingMode) {
            decimalFormat.setRoundingMode(roundingMode);
        }

        return decimalFormat;
    }

    private DecimalFormatBuilder append(final char pattern) {
        this.pattern.append(pattern);
        return this;
    }

    private final StringBuilder pattern = new StringBuilder();

    int groupingSize = -1;

    Locale locale;

    String prefix = "";

    RoundingMode roundingMode;

    String suffix = "";

    // HasText..........................................................................................................

    /**
     * Returns the text or pattern.
     */
    @Override
    public String text() {
        final StringBuilder pattern = new StringBuilder();
        pattern.append(this.prefix);
        pattern.append(this.pattern);
        pattern.append(this.suffix);

        return pattern.toString();
    }

    // ToString.........................................................................................................

    @Override
    public String toString() {
        return this.text();
    }
}
