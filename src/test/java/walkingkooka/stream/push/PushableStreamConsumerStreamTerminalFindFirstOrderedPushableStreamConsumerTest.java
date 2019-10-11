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
import walkingkooka.stream.StreamException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumerTest extends PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase<PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String>, Optional<String>> {

    @Test
    public void testAcceptNone() {
        final PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String> findFirst = this.createPushableStreamConsumer();
        this.checkResult(findFirst, Optional.empty());
        this.checkIsFinished(findFirst, false);
    }

    @Test
    public void testAcceptOne() {
        final String found = "found123";
        final PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String> findFirst = this.createPushableStreamConsumer();
        this.accept(findFirst, found);
        this.checkResult(findFirst, Optional.of(found));
    }

    @Test
    public void testAcceptSecondFails() {
        final String found = "first";

        final PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String> findFirst = this.createPushableStreamConsumer();
        this.accept(findFirst, found);

        assertThrows(StreamException.class, () -> findFirst.accept("2!!"));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamConsumer(), "findFirstOrdered closeables: " + this.closeables);
    }

    @Override
    PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String> createPushableStreamConsumer(final PushableStreamConsumerCloseableCollection closeables) {
        return PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer.with(closeables);
    }

    @Override
    public Class<PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer<String>> type() {
        return Cast.to(PushableStreamConsumerStreamTerminalFindFirstOrderedPushableStreamConsumer.class);
    }
}
