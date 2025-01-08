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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.HasTextTesting;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for testing a {@link Name} with mostly helpers to check construction failure.
 */
public interface NameTesting<N extends Name, C extends Comparable<C>> extends ComparableTesting2<C>,
    HasTextTesting,
    ToStringTesting<N>,
    TypeNameTesting<N> {

    @Test
    default void testPublicClass() {
        this.checkEquals(
            JavaVisibility.PUBLIC,
            this.typeVisibility(),
            "Visibility of name"
        );
    }

    @Test
    default void testNullFails() {
        assertThrows(NullPointerException.class, () -> this.createName(null));
    }

    @Test
    default void testEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> this.createName(""));
    }

    @Test
    default void testWith() {
        this.createNameAndCheck(this.nameText());
    }

    @Test
    default void testCaseSensitivity() {
        assertSame(this.caseSensitivity(), this.createName(this.nameText()).caseSensitivity());
    }

    // Comparable.................................................................................

    @Test
    default void testEqualsDifferentText() {
        this.checkNotEquals(this.createComparable(this.differentNameText()));
    }

    @Test
    default void testCompareDifferentCase() {
        final String value = this.nameText();

        final C lower = this.createComparable(value.toLowerCase());
        final C upper = this.createComparable(value.toUpperCase());

        if (this.caseSensitivity() == CaseSensitivity.INSENSITIVE) {
            this.compareToAndCheckEquals(
                lower,
                upper);
        } else {
            this.compareToAndCheckLess(
                upper,
                lower);
        }
    }

    @Test
    default void testCompareLess() {
        this.compareToAndCheckLess(
            this.createComparable(this.nameTextLess()),
            this.createComparable(this.nameText()));
    }

    @Test
    default void testCompareLessDifferentCase() {
        if (CaseSensitivity.INSENSITIVE == this.caseSensitivity()) {
            this.compareToAndCheckLess(
                this.createComparable(this.nameTextLess().toUpperCase()),
                this.createComparable(this.nameText().toLowerCase()));
        }
    }

    @Test
    default void testToString() {
        final String nameText = this.nameText();
        this.toStringAndCheck(this.createName(nameText), nameText);
    }

    N createName(final String name);

    default C createComparable(final String name) {
        return Cast.to(this.createName(name));
    }

    CaseSensitivity caseSensitivity();

    String nameText();

    String differentNameText();

    String nameTextLess();

    default void createNameAndCheck(final String value) {
        final N name = this.createName(value);
        this.checkValue(name, value);
    }

    default void checkValue(final Name name, final String value) {
        this.checkEquals(
            value,
            name.value(),
            "value"
        );
    }

    Class<N> type();

    default JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    default C createComparable() {
        return Cast.to(this.createName(this.nameText()));
    }

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Name.class.getSimpleName();
    }
}
