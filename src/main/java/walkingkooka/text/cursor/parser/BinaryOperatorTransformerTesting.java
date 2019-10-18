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
package walkingkooka.text.cursor.parser;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.test.Testing;
import walkingkooka.test.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixing testing interface for {@link BinaryOperatorTransformer}
 */
public interface BinaryOperatorTransformerTesting<T extends BinaryOperatorTransformer> extends Testing,
        ToStringTesting<T>,
        TypeNameTesting<T> {

    @Test
    default void testHighestGreaterThanLowestPriority() {
        final T transformer = this.createBinaryOperatorTransformer();
        final int lowest = transformer.lowestPriority();
        final int highest = transformer.highestPriority();
        assertEquals(true, lowest < highest, () -> transformer + " " + lowest + " < " + highest);
    }

    T createBinaryOperatorTransformer();

    @Override
    default String typeNameSuffix() {
        return BinaryOperatorTransformer.class.getSimpleName();
    }
}
