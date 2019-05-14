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
 *
 */

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NonEmptyCloseableCollectionTest extends CloseableCollectionTestCase<NonEmptyCloseableCollection> implements HashCodeEqualsDefinedTesting<NonEmptyCloseableCollection> {

    @Test
    public void testAdd() {
        final Runnable second = TestCloseableRunnable.with("2");
        this.addAndCheck(this.createCloseableCollection(),
                second,
                this.first, second);
    }

    @Test
    public void testAdd2() {
        final Runnable second = TestCloseableRunnable.with("2b");
        final Runnable third = TestCloseableRunnable.with("3c");
        final Runnable fourth = TestCloseableRunnable.with("4d");

        final NonEmptyCloseableCollection collection = this.createCloseableCollection()
                .add(second)
                .add(third);

        this.addAndCheck(collection,
                fourth,
                this.first, second, third, fourth);

        collection.closeables.stream()
                .forEach(c -> TestCloseableRunnable.class.cast(c).checkNotClosed());
    }

    @Test
    public void testClose() {
        final TestCloseableRunnable second = TestCloseableRunnable.with("2b");
        final TestCloseableRunnable third = TestCloseableRunnable.with("3c");

        final NonEmptyCloseableCollection collection = this.createCloseableCollection()
                .add(second)
                .add(third);
        collection.close();

        collection.closeables.stream()
                .forEach(c -> TestCloseableRunnable.class.cast(c).checkClosed());
    }

    @Test
    public void testCloseTwiceFails() {
        final TestCloseableRunnable second = TestCloseableRunnable.with("2b");

        final NonEmptyCloseableCollection collection = this.createCloseableCollection()
                .add(second);
        collection.close();

        collection.closeables.stream()
                .forEach(c -> TestCloseableRunnable.class.cast(c).checkClosed());

        boolean failed = false;
        try {
            collection.close();
        } catch (final AssertionError expected) {
            failed = true;
        }
        assertEquals(true, failed, () -> "Expected subsequence close() to fail.");

        collection.closeables.stream()
                .forEach(c -> TestCloseableRunnable.class.cast(c).checkClosed());
    }

    // HashCodeEqualsDefined...........................................................................................

    @Test
    public void testDifferentExtra() {
        this.checkNotEquals(this.createCloseableCollection().add(this.different));
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(EmptyCloseableCollection.empty().add(this.different));
    }

    // Object..........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createCloseableCollection(), "1a");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(this.createCloseableCollection().add(this.different), "1a, 2b");
    }

    @Override
    NonEmptyCloseableCollection createCloseableCollection() {
        return EmptyCloseableCollection.empty()
                .add(this.first);
    }

    private final Runnable first = TestCloseableRunnable.with("1a");
    private final Runnable different = TestCloseableRunnable.with("2b");

    @Override
    public String typeNamePrefix() {
        return "NonEmpty";
    }

    @Override
    public Class<NonEmptyCloseableCollection> type() {
        return NonEmptyCloseableCollection.class;
    }

    @Override
    public NonEmptyCloseableCollection createObject() {
        return Cast.to(this.createCloseableCollection());
    }
}
