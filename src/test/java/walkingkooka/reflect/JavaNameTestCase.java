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

package walkingkooka.reflect;

import walkingkooka.naming.NameTesting2;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.CaseSensitivity;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class JavaNameTestCase<N extends JavaName<N>> implements NameTesting2<N, N> {

    JavaNameTestCase() {
        super();
    }

    @Override
    public final int minLength() {
        return 1;
    }

    @Override
    public final int maxLength() {
        return 65535;
    }

    @Override
    public final String possibleValidChars(final int position) {
        return this.matchingCharacters(this.predicate(position));
    }

    @Override
    public final String possibleInvalidChars(final int position) {
        return this.matchingCharacters(this.predicate(position).negate());
    }

    final String matchingCharacters(final CharPredicate predicate) {
        return IntStream.range(Character.MIN_VALUE, 100)
            .filter(i -> predicate.test((char) i))
            .mapToObj(i -> String.valueOf((char) i))
            .collect(Collectors.joining(""));
    }

    final CharPredicate predicate(final int position) {
        return 0 == position ?
            Character::isJavaIdentifierStart :
            Character::isJavaIdentifierPart;
    }

    @Override
    public final CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }
}
