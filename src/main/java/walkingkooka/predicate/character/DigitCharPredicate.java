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
 */

package walkingkooka.predicate.character;

import java.io.Serializable;

/**
 * Always returns true if a character is a letter or digit of determined by {@link
 * Character#isLetterOrDigit(char)}.
 */
final class DigitCharPredicate implements CharPredicate, Serializable {

    private static final long serialVersionUID = -7735968262582751027L;

    /**
     * Singleton
     */
    final static DigitCharPredicate INSTANCE = new DigitCharPredicate();

    /**
     * Private constructor
     */
    private DigitCharPredicate() {
        super();
    }

    @Override
    public boolean test(final char c) {
        return Character.isDigit(c);
    }

    private Object readResolve() {
        return DigitCharPredicate.INSTANCE;
    }

    @Override
    public String toString() {
        return "digit";
    }
}
