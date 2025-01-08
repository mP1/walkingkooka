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
import walkingkooka.text.CharSequences;

import java.util.Comparator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PushableStreamConsumerStreamTerminal2PushableStreamConsumerTestCase<C extends PushableStreamConsumerStreamTerminal2PushableStreamConsumer<String>> extends PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase<C, Optional<String>> {

    PushableStreamConsumerStreamTerminal2PushableStreamConsumerTestCase() {
        super();
    }

    @Test
    public final void testWithNullComparatorFails() {
        assertThrows(NullPointerException.class, () -> this.createPushableStreamStreamPushableStreamConsumer(null, this.closeables));
    }

    @Test
    public final void testAcceptZeroValues() {
        this.acceptAndCheck("");
    }

    @Test
    public final void testAcceptOneValue() {
        this.acceptAndCheck("abc123", "abc123");
    }

    final void acceptAndCheck(final String values) {
        this.acceptAndCheck0(values, Optional.empty());
    }

    final void acceptAndCheck(final String values, final String expected) {
        this.acceptAndCheck0(values, Optional.of(expected));
    }

    private void acceptAndCheck0(final String values, final Optional<String> expected) {
        final C consumer = this.createPushableStreamConsumer();

        for (String value : this.commaSeparated(values)) {
            this.accept(consumer, value);
        }

        this.checkEquals(expected,
            consumer.result(),
            () -> "Values " + CharSequences.quoteAndEscape(values));
    }

    final Comparator<String> comparator() {
        return String.CASE_INSENSITIVE_ORDER;
    }

    @Override final C createPushableStreamConsumer(final PushableStreamConsumerCloseableCollection closeables) {
        return this.createPushableStreamStreamPushableStreamConsumer(this.comparator(), closeables);
    }

    abstract C createPushableStreamStreamPushableStreamConsumer(final Comparator<String> comparator, final PushableStreamConsumerCloseableCollection closeables);
}
