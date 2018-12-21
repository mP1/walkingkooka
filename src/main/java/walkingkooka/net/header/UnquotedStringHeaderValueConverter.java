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

import walkingkooka.InvalidCharacterException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;

/**
 * A {@link HeaderValueConverter} that handles string values without any escaping or quotes.
 */
final class UnquotedStringHeaderValueConverter extends StringHeaderValueConverter {

    /**
     * Factory that creates a new {@link UnquotedStringHeaderValueConverter}.
     */
    final static UnquotedStringHeaderValueConverter with(final CharPredicate predicate) {
        return new UnquotedStringHeaderValueConverter(predicate);
    }

    /**
     * Private ctor use factory.
     */
    private UnquotedStringHeaderValueConverter(final CharPredicate predicate) {
        super(predicate);
    }

    @Override
    String parse0(final String text, final Name name) {
        this.checkText(text);
        return text;
    }

    @Override
    void check0(final Object value) {
        this.checkType(value, String.class);
    }

    @Override
    String toText0(final String value, final Name name) {
        this.checkText(value);
        return value;
    }

    private void checkText(final String text) {
        final CharPredicate predicate = this.predicate;

        int i = 0;
        for (char c : text.toCharArray()) {
            if (!predicate.test(c)) {
                throw new InvalidCharacterException(text, i);
            }
            i++;
        }
    }
}
