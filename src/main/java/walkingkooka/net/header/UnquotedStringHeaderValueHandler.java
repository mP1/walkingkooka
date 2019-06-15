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

import walkingkooka.InvalidCharacterException;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicate;

/**
 * A {@link HeaderValueHandler} that handles string values without any escaping or quotes.
 */
final class UnquotedStringHeaderValueHandler extends QuotedOrUnquotedStringHeaderValueHandler {

    /**
     * Factory that creates a new {@link UnquotedStringHeaderValueHandler}.
     */
    static UnquotedStringHeaderValueHandler with(final CharPredicate predicate) {
        return new UnquotedStringHeaderValueHandler(predicate);
    }

    /**
     * Private ctor use factory.
     */
    private UnquotedStringHeaderValueHandler(final CharPredicate predicate) {
        super(predicate);
    }

    @Override
    String parse0(final String text, final Name name) {
        this.checkText(text);
        return text;
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, String.class, name);
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
