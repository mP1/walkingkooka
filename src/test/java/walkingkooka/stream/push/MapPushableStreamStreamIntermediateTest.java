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

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MapPushableStreamStreamIntermediateTest extends NonLimitOrSkipPushableStreamStreamIntermediateTestCase<MapPushableStreamStreamIntermediate> {

    @Test
    public void testWithNullMapperFails() {
        assertThrows(NullPointerException.class, () -> {
            MapPushableStreamStreamIntermediate.with(null);
        });
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPushableStreamStreamIntermediate(), "map " + MAPPER);
    }

    @Override
    MapPushableStreamStreamIntermediate createPushableStreamStreamIntermediate() {
        return MapPushableStreamStreamIntermediate.with(MAPPER);
    }

    private final static Function<String, String> MAPPER = Function.identity();

    @Override
    public Class<MapPushableStreamStreamIntermediate> type() {
        return MapPushableStreamStreamIntermediate.class;
    }
}
