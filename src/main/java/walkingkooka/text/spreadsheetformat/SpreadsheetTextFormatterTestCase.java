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

package walkingkooka.text.spreadsheetformat;

import org.junit.Test;
import walkingkooka.test.PackagePrivateClassTestCase;
import walkingkooka.text.CharSequences;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class SpreadsheetTextFormatterTestCase<F extends SpreadsheetTextFormatter<V>, V> extends PackagePrivateClassTestCase<F> {

    @Test(expected = NullPointerException.class)
    public void testFormatNullValueFails() {
        this.createFormatter().format(null, this.createContext());
    }

    @Test(expected = NullPointerException.class)
    public void testFormatNullContextFails() {
        this.createFormatter().format(this.value(), null);
    }

    @Test
    public abstract void testToString();

    protected abstract F createFormatter();

    protected abstract V value();

    protected abstract SpreadsheetTextFormatContext createContext();

    protected void formatAndCheck(final V value,
                                  final String formattedText) {
        this.formatAndCheck(value, this.formattedText(formattedText));
    }

    protected void formatAndCheck(final V value,
                                  final SpreadsheetFormattedText formattedText) {
        this.formatAndCheck(this.createFormatter(), value, formattedText);
    }

    protected void formatAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                  final V value,
                                  final String formattedText) {
        this.formatAndCheck(formatter, value, this.formattedText(formattedText));
    }

    protected void formatAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                  final V value,
                                  final SpreadsheetFormattedText formattedText) {
        this.formatAndCheck(formatter, value, this.createContext(), formattedText);
    }

    protected void formatAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                  final V value,
                                  final SpreadsheetTextFormatContext context,
                                  final String formattedText) {
        this.formatAndCheck(formatter, value, context, this.formattedText(formattedText));
    }

    protected void formatAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                  final V value,
                                  final SpreadsheetTextFormatContext context,
                                  final SpreadsheetFormattedText formattedText) {
        this.formatAndCheck(formatter, value, context, Optional.of(formattedText));
    }

    private SpreadsheetFormattedText formattedText(final String text) {
        return SpreadsheetFormattedText.with(SpreadsheetFormattedText.WITHOUT_COLOR, text);
    }

    // format fail and check

    protected void formatFailAndCheck(final V value) {
        this.formatFailAndCheck(value, this.createContext());
    }

    protected void formatFailAndCheck(final V value,
                                      final SpreadsheetTextFormatContext context) {
        this.formatFailAndCheck(this.createFormatter(), value, context);
    }

    protected void formatFailAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                      final V value,
                                      final SpreadsheetTextFormatContext context) {
        this.formatAndCheck(formatter, value, context, Optional.empty());
    }

    // general format and check

    protected void formatAndCheck(final SpreadsheetTextFormatter<V> formatter,
                                  final V value,
                                  final SpreadsheetTextFormatContext context,
                                  final Optional<SpreadsheetFormattedText> formattedText) {
        assertEquals(formatter + " " + CharSequences.quoteIfChars(value), formattedText, formatter.format(value, context));
    }
}
