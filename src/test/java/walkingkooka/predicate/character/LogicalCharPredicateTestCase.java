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
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;

abstract public class LogicalCharPredicateTestCase<P extends LogicalCharPredicate>
        extends CharPredicateTestCase<P>
        implements HashCodeEqualsDefinedTesting<P>, SerializationTesting<P> {

    LogicalCharPredicateTestCase() {
        super();
    }

    @Test final public void testWrapNullFirstPredicateFails() {
        this.wrapFails(null, CharPredicates.fake());
    }

    @Test final public void testWrapNullSecondPredicateFails() {
        this.wrapFails(CharPredicates.fake(), null);
    }

    private void wrapFails(final CharPredicate first, final CharPredicate second) {
        try {
            this.createCharacterPredicate(first, second);
            Assert.fail();
        } catch (final NullPointerException expected) {
        }
    }

    @Test
    final public void testEqualsDifferentFirstCharPredicate() {
        this.checkNotEquals(this.createCharacterPredicate(CharPredicates.is('d'), CharPredicates.is('r')));
    }

    @Test
    final public void testEqualsDifferentSecondCharPredicate() {
        this.checkNotEquals(this.createCharacterPredicate(CharPredicates.is('l'), CharPredicates.is('d')));
    }

    @Test
    public void testToString() {
        final CharPredicate first = CharPredicates.fake();
        final CharPredicate second = CharPredicates.fake();
        final P predicate = this.createCharacterPredicate(first, second);
        assertEquals(first + " " + this.operator(predicate) + " " + second,
                predicate.toString());
    }

    private String operator(final LogicalCharPredicate predicate) {
        return predicate.operator();
    }

    @Override
    final protected P createCharacterPredicate() {
        return this.createCharacterPredicate(CharPredicates.fake(), CharPredicates.fake());
    }

    abstract P createCharacterPredicate(CharPredicate first, CharPredicate second);

    @Override
    public P createObject() {
        return this.createCharacterPredicate(CharPredicates.is('l'), CharPredicates.is('r'));
    }

    @Override
    public final P serializableInstance() {
        return this.createCharacterPredicate();
    }

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }
}
