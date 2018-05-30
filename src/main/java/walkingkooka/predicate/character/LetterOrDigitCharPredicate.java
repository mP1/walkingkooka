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
final class LetterOrDigitCharPredicate implements CharPredicate, Serializable {

    private static final long serialVersionUID = 2021196835111247371L;

    /**
     * Singleton
     */
    final static LetterOrDigitCharPredicate INSTANCE = new LetterOrDigitCharPredicate();

    /**
     * Private constructor
     */
    private LetterOrDigitCharPredicate() {
        super();
    }

    @Override
    public boolean test(final char c) {
        return Character.isLetterOrDigit(c);
    }

    private Object readResolve() {
        return LetterOrDigitCharPredicate.INSTANCE;
    }

    @Override
    public String toString() {
        return "Letter/Digit";
    }
}
