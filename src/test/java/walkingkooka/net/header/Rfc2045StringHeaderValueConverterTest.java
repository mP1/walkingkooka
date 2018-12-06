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
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CharSequences;

import static org.junit.Assert.fail;

public final class Rfc2045StringHeaderValueConverterTest extends
        HeaderValueConverterTestCase<Rfc2045StringHeaderValueConverter,
                String> {

    private final static String TEXT = "abc123";

    @Override
    protected String requiredPrefix() {
        return "Rfc2045" + String.class.getSimpleName();
    }

    @Test
    public void testParseAllInvalidCharacterFails() {
        final CharPredicate predicate = CharPredicates.rfc2045Token();
        final Rfc2045StringHeaderValueConverter converter = Rfc2045StringHeaderValueConverter.INSTANCE;
        final Name name = this.name();

        for (int i = 0; i < Character.MAX_VALUE; i++) {
            final char c = (char) i;
            if (predicate.test(c)) {
                continue;
            }
            try {
                final String s = String.valueOf(c);
                converter.parse(s, name);
                fail("Predicate test failed, expected HeaderValueException for " + CharSequences.quoteAndEscape(s));
            } catch (final HeaderValueException expected) {
            }
        }
    }

    @Test
    public void testParseAllValidCharacterFails() {
        final CharPredicate predicate = CharPredicates.rfc2045Token();
        final Rfc2045StringHeaderValueConverter converter = Rfc2045StringHeaderValueConverter.INSTANCE;
        final Name name = this.name();

        for (int i = 0; i < Character.MAX_VALUE; i++) {
            final char c = (char) i;
            if (predicate.test(c)) {
                converter.parse(String.valueOf(c), name);
            }
        }
    }

    @Test
    public void testValue() {
        this.parseAndToTextAndCheck(TEXT, TEXT);
    }

    @Override
    protected Rfc2045StringHeaderValueConverter converter() {
        return Rfc2045StringHeaderValueConverter.INSTANCE;
    }

    @Override
    protected StringName name() {
        return Names.string("string-name");
    }

    @Override
    protected String invalidHeaderValue() {
        return ",";
    }

    @Override
    protected String value() {
        return TEXT;
    }

    @Override
    protected String converterToString() {
        return String.class.getSimpleName();
    }

    @Override
    protected Class<Rfc2045StringHeaderValueConverter> type() {
        return Rfc2045StringHeaderValueConverter.class;
    }
}
