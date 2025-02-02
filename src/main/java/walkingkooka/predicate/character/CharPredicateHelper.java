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

package walkingkooka.predicate.character;

import walkingkooka.InvalidCharacterException;
import walkingkooka.reflect.PublicStaticHelper;

/**
 * Extension method for {@link CharPredicate} because GWT is complaining about private methods in an interface.
 * <pre>
 * Tracing compile failure path for type 'walkingkooka.predicate.character.CharPredicate'
 *       Errors in 'jar:file:/Users/miroslav/repos-github/walkingkooka-store/target/it-repo/walkingkooka/walkingkooka-gwt/1.0-SNAPSHOT/walkingkooka-gwt-1.0-SNAPSHOT.jar!/walkingkooka/predicate/character/CharPredicate.java'
 *          Line 93: Illegal modifier for the interface method checkCharacters; only public, abstract, default, static and strictfp are permitted
 *          Line 93: Abstract methods do not specify a body
 *       Checked 1 dependencies for errors.
 * </pre>
 */
final class CharPredicateHelper implements PublicStaticHelper {

    /**
     * Checks that all characters pass the {@link CharPredicate} test.
     */
    static void checkCharacters(final CharPredicate that,
                                final CharSequence chars) {
        final int length = chars.length();

        for (int i = 0; i < length; i++) {
            final char c = chars.charAt(i);
            if (false == that.test(c)) {
                throw new InvalidCharacterException(chars.toString(), i);
            }
        }
    }

    /**
     * Stop creation
     */
    private CharPredicateHelper() {
        throw new UnsupportedOperationException();
    }
}
