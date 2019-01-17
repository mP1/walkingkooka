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

package walkingkooka.naming;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link Name} with mostly helpers to assert construction failure.
 */
abstract public class NameTestCase<N extends Name, C extends Comparable<C> & HashCodeEqualsDefined> extends ClassTestCase<N>
        implements ComparableTesting<C>{

    protected NameTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Name.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFails() {
        this.createName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFails() {
        this.createName("");
    }

    @Test
    public void testWith() {
        this.createNameAndCheck(this.nameText());
    }

    // Comparable.................................................................................

    @Test
    public void testDifferentText() {
        this.checkNotEquals(this.createComparable(this.differentNameText()));
    }

    @Test
    public void testCompareDifferentCase() {
        final String value = this.nameText();

        final C lower = this.createComparable(value.toLowerCase());
        final C upper = this.createComparable(value.toUpperCase());

        if (this.caseSensitivity() == CaseSensitivity.INSENSITIVE) {
            this.compareToAndCheckEqual(
                    lower,
                    upper);
        } else {
            this.compareToAndCheckLess(
                    upper,
                    lower);
        }
    }

    @Test
    public void testCompareLess() {
        this.compareToAndCheckLess(
                this.createComparable(this.nameTextLess()),
                this.createComparable(this.nameText()));
    }

    @Test
    public void testCompareLessDifferentCase() {
        if (CaseSensitivity.INSENSITIVE == this.caseSensitivity()) {
            this.compareToAndCheckLess(
                    this.createComparable(this.nameTextLess().toUpperCase()),
                    this.createComparable(this.nameText().toLowerCase()));
        }
    }

    // toString.................................................................................

    @Test
    public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    protected abstract N createName(final String name);

    private C createComparable(final String name) {
        return Cast.to(this.createName(name));
    }

    protected abstract CaseSensitivity caseSensitivity();

    protected abstract String nameText();

    protected abstract String differentNameText();

    protected abstract String nameTextLess();

    protected void createNameAndCheck(final String value) {
        final N name = this.createName(value);
        this.checkValue(name, value);
    }

    protected void checkValue(final Name name, final String value) {
        assertEquals("value", value, name.value());
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public final C createComparable() {
        return Cast.to(this.createName(this.nameText()));
    }
}
