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

import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PushableStreamConsumerStreamIntermediate2FilterTest extends PushableStreamConsumerStreamIntermediate3TestCase<PushableStreamConsumerStreamIntermediate2Filter> {

    @Test
    public void testWithNullCollectorFails() {
        assertThrows(NullPointerException.class, () -> PushableStreamConsumerStreamIntermediate2Filter.with(null));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamStreamIntermediate(), "filter " + PREDICATE);
    }

    @Override
    PushableStreamConsumerStreamIntermediate2Filter createPushableStreamStreamIntermediate() {
        return PushableStreamConsumerStreamIntermediate2Filter.with(PREDICATE);
    }

    private final static Predicate<String> PREDICATE = Predicates.fake();

    @Override
    public Class<PushableStreamConsumerStreamIntermediate2Filter> type() {
        return PushableStreamConsumerStreamIntermediate2Filter.class;
    }
}
