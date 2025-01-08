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

package walkingkooka.stream.push;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;

public final class PushableStreamConsumerCloseableCollectionNonEmptyTest extends PushableStreamConsumerCloseableCollectionTestCase<PushableStreamConsumerCloseableCollectionNonEmpty>
    implements HashCodeEqualsDefinedTesting2<PushableStreamConsumerCloseableCollectionNonEmpty> {

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

        final PushableStreamConsumerCloseableCollectionNonEmpty collection = this.createCloseableCollection()
            .add(second)
            .add(third);

        this.addAndCheck(collection,
            fourth,
            this.first, second, third, fourth);

        collection.closeables
            .forEach(c -> ((TestCloseableRunnable) c).checkNotClosed());
    }

    @Test
    public void testClose() {
        final TestCloseableRunnable second = TestCloseableRunnable.with("2b");
        final TestCloseableRunnable third = TestCloseableRunnable.with("3c");

        final PushableStreamConsumerCloseableCollectionNonEmpty collection = this.createCloseableCollection()
            .add(second)
            .add(third);
        collection.close();

        collection.closeables
            .forEach(c -> ((TestCloseableRunnable) c).checkClosed());
    }

    @Test
    public void testCloseTwiceSecondIgnored() {
        this.closeCount = 0;

        final PushableStreamConsumerCloseableCollectionNonEmpty collection = this.createCloseableCollection()
            .add(() -> this.closeCount++);
        collection.close();

        collection.closeables.stream()
            .filter(p -> p instanceof TestCloseableRunnable)
            .forEach(c -> ((TestCloseableRunnable) c).checkClosed());

        collection.close();

        collection.closeables.stream()
            .filter(p -> p instanceof TestCloseableRunnable)
            .forEach(c -> ((TestCloseableRunnable) c).checkClosed());

        this.checkEquals(1, this.closeCount);
    }

    private int closeCount;

    // HashCodeEqualsDefined...........................................................................................

    @Test
    public void testEqualsDifferentExtra() {
        this.checkNotEquals(this.createCloseableCollection().add(this.different));
    }

    @Test
    public void testEqualsDifferentClosed() {
        final PushableStreamConsumerCloseableCollectionNonEmpty other = PushableStreamConsumerCloseableCollectionEmpty.empty().add(this.different);
        other.close();

        this.checkNotEquals(other);
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(PushableStreamConsumerCloseableCollectionEmpty.empty().add(this.different));
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
    PushableStreamConsumerCloseableCollectionNonEmpty createCloseableCollection() {
        return PushableStreamConsumerCloseableCollectionEmpty.empty()
            .add(this.first);
    }

    private final Runnable first = TestCloseableRunnable.with("1a");
    private final Runnable different = TestCloseableRunnable.with("2b");

    @Override
    public String typeNameSuffix() {
        return "NonEmpty";
    }

    @Override
    public Class<PushableStreamConsumerCloseableCollectionNonEmpty> type() {
        return PushableStreamConsumerCloseableCollectionNonEmpty.class;
    }

    @Override
    public PushableStreamConsumerCloseableCollectionNonEmpty createObject() {
        return Cast.to(this.createCloseableCollection());
    }
}
