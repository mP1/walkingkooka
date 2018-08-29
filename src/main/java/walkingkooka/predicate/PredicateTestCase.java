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

package walkingkooka.predicate;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.function.Predicate;

/**
 * Base class for testing a {@link Predicate} with mostly parameter checking tests and various
 * utility methods toassert matching and non matching.
 */
abstract public class PredicateTestCase<P extends Predicate<T>, T>
        extends PackagePrivateClassTestCase<P> {

    protected PredicateTestCase() {
        super();
    }

    @Test
    public final void testCheckNaming() {
        this.checkNaming(Predicate.class);
    }

    @Test
    public void testNullFails() {
        try {
            this.test(null);
            Assert.fail();
        } catch (final NullPointerException ignored) {
        }
    }

    @Test final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected P createPredicate();

    final protected boolean test(final T value) {
        return this.createPredicate().test(value);
    }

    final protected void testTrue(final T value) {
        this.testTrue(this.createPredicate(), value);
    }

    final protected void testTrue(final Predicate<T> predicate, final T value) {
        if (false == predicate.test(value)) {
            Assert.fail(predicate + " did not match=" + value);
        }
    }

    final protected void testFalse(final T value) {
        this.testFalse(this.createPredicate(), value);
    }

    final protected void testFalse(final Predicate<T> predicate, final T value) {
        if (predicate.test(value)) {
            Assert.fail(predicate + " should not have matched=" + value);
        }
    }
}
