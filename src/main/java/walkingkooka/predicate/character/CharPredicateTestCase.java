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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Base class for any tests involving a {@link CharPredicate} including helpers to invoke andassert
 * {@link CharPredicate#test(char)} results.
 */
abstract public class CharPredicateTestCase<P extends CharPredicate>
        extends ClassTestCase<P> {

    protected CharPredicateTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(CharPredicate.class);
    }

    @Test
    public void testAnd() {
        final P predicate = this.createCharacterPredicate();
        assertSame(predicate, predicate.and(predicate));
    }

    @Test
    public void testNotNot() {
        final P predicate = this.createCharacterPredicate();
        assertEquals(predicate, predicate.negate().negate());
    }

    @Test
    public void testOr() {
        final P predicate = this.createCharacterPredicate();
        assertSame(predicate, predicate.or(predicate));
    }

    @Test
    public void testSetToStringSame() {
        final P predicate = this.createCharacterPredicate();
        assertSame(predicate, predicate.setToString(predicate.toString()));
    }

    @Test
    public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected P createCharacterPredicate();

    protected boolean test(final char c) {
        return this.createCharacterPredicate().test(c);
    }

    final protected void testTrue(final char c) {
        this.testTrue(this.createCharacterPredicate(), c);
    }

    final protected void testTrue(final CharPredicate predicate, final char c) {
        if (false == predicate.test(c)) {
            Assert.fail(predicate + " did not match=" + CharSequences.quoteAndEscape(c));
        }
    }

    final protected void testFalse(final char c) {
        this.testFalse(this.createCharacterPredicate(), c);
    }

    final protected void testFalse(final CharPredicate predicate, final char c) {
        if (predicate.test(c)) {
            Assert.fail(predicate + " should not have matched=" + CharSequences.quoteAndEscape(c));
        }
    }
    
    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
