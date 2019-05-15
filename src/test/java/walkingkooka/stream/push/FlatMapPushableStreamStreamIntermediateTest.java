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

import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class FlatMapPushableStreamStreamIntermediateTest extends NonLimitOrSkipPushableStreamStreamIntermediateTestCase<FlatMapPushableStreamStreamIntermediate> {

    @Test
    public void testWithNullMapperFails() {
        assertThrows(NullPointerException.class, () -> {
            FlatMapPushableStreamStreamIntermediate.with(null);
        });
    }

    @Test
    public void testDifferentMapper() {
        this.checkNotEquals(FlatMapPushableStreamStreamIntermediate.with((i) -> Stream.of(i)));
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamStreamIntermediate(), "flatmap " + MAPPER);
    }

    @Override
    FlatMapPushableStreamStreamIntermediate createPushableStreamStreamIntermediate() {
        return FlatMapPushableStreamStreamIntermediate.with(MAPPER);
    }

    private final static Function<?, Stream<?>> MAPPER = (s)-> Stream.of(s);

    @Override
    public Class<FlatMapPushableStreamStreamIntermediate> type() {
        return FlatMapPushableStreamStreamIntermediate.class;
    }
}
