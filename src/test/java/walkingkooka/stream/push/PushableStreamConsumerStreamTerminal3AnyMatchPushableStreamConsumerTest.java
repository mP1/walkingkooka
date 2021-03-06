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
import walkingkooka.predicate.Predicates;

import java.util.function.Predicate;

public final class PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumerTest extends PushableStreamConsumerStreamTerminal3PushableStreamConsumerTestCase<PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumer<String>> {

    @Test
    public void testAllTrue() {
        this.acceptAndCheck(Predicates.always(), "a1,b2,c3", true, 1);
    }

    @Test
    public void testAllFalse() {
        this.acceptAndCheck(Predicates.never(), "a1,b2,c3", false, 3);
    }

    @Test
    public void testSomeFirst() {
        this.acceptAndCheck(Predicates.is("a1"), "a1,b2,c3", true, 1);
    }

    @Test
    public void testSomeMid() {
        this.acceptAndCheck(Predicates.is("b2"), "a1,b2,c3", true, 2);
    }

    @Test
    public void testSomeLast() {
        this.acceptAndCheck(Predicates.is("c3"), "a1,b2,c3", true, 3);
    }

    @Override
    PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumer<String> createPushableStreamStreamPushableStreamConsumer(final Predicate<String> predicate,
                                                                                                                                 final PushableStreamConsumerCloseableCollection closeables) {
        return PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumer.with(predicate, closeables);
    }

    @Override
    String label() {
        return "anyMatch";
    }

    @Override
    public Class<PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumer<String>> type() {
        return Cast.to(PushableStreamConsumerStreamTerminal3AnyMatchPushableStreamConsumer.class);
    }
}
