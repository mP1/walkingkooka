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
import walkingkooka.predicate.Predicates;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class PushableStreamConsumerStreamTerminal3PushableStreamConsumerTestCase<P extends PushableStreamConsumerStreamTerminal3PushableStreamConsumer<String>> extends
    PushableStreamConsumerStreamTerminalPushableStreamConsumerTestCase<P, Boolean> {

    PushableStreamConsumerStreamTerminal3PushableStreamConsumerTestCase() {
        super();
    }

    @Test
    public final void testWithNullPredicateFails() {
        assertThrows(NullPointerException.class, () -> this.createPushableStreamStreamPushableStreamConsumer(null, this.closeables));
    }

    @Test
    public final void testToStringFalse() {
        final P consumer = this.createPushableStreamConsumer();
        consumer.result = false;

        this.toStringAndCheck(consumer, this.label() + " " + PREDICATE_TOSTRING + " false closeables: " + this.closeables.toString());
    }

    @Test
    public final void testToStringTrue() {
        final P consumer = this.createPushableStreamConsumer();
        consumer.result = true;

        this.toStringAndCheck(consumer, this.label() + " " + PREDICATE_TOSTRING + " true closeables: " + this.closeables.toString());
    }

    @Override final P createPushableStreamConsumer(final PushableStreamConsumerCloseableCollection closeables) {
        return this.createPushableStreamStreamPushableStreamConsumer(PREDICATE, closeables);
    }

    final static String PREDICATE_TOSTRING = "Predicate123";

    final static Predicate<String> PREDICATE = Predicates.customToString(Predicates.always(), PREDICATE_TOSTRING);

    abstract P createPushableStreamStreamPushableStreamConsumer(final Predicate<String> predicate,
                                                                final PushableStreamConsumerCloseableCollection closeables);

    abstract String label();

    final void acceptAndCheck(final Predicate<String> predicate,
                              final String commaSeperated,
                              final Boolean result,
                              final int consumedCount) {
        final P consumer = this.createPushableStreamStreamPushableStreamConsumer(predicate, PushableStreamConsumerCloseableCollection.empty());

        final List<String> values = this.commaSeparated(commaSeperated);

        int i = 0;
        while (i < values.size() && false == consumer.isFinished()) {

            this.accept(consumer, values.get(i));
            i++;
        }

        this.checkEquals(result,
            consumer.result(),
            () -> "values: " + CharSequences.quoteAndEscape(commaSeperated) + " " + consumer);
        this.checkEquals(consumedCount, i, () -> "consumed values count, values: " + CharSequences.quoteAndEscape(commaSeperated));
    }
}
