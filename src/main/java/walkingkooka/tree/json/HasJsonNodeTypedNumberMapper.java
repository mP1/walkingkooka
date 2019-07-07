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

package walkingkooka.tree.json;

/**
 * Base for a {@link HasJsonNodeMapper} that holds a number value wrapped inside a JSON object with the type.
 */
abstract class HasJsonNodeTypedNumberMapper<T extends Number> extends HasJsonNodeTypedMapper<T> {

    HasJsonNodeTypedNumberMapper() {
        super();
    }

    @Override
    final T fromJsonNodeNonNull(final JsonNode node) {
        return this.numberWithoutPrecisionLoss(node.numberValueOrFail());
    }

    @Override
    final T fromJsonNodeNull() {
        return null;
    }

    /**
     * Ensures that no precision is lost.
     */
    private T numberWithoutPrecisionLoss(final Number number) {
        final T number2 = this.number(number);
        if(number2.doubleValue() != number.doubleValue()) {
            throw new NumericLossJsonNodeException(number.getClass().getName() + "=" + number);
        }
        return number2;
    }

    abstract T number(final Number number);

    @Override
    final JsonNode toJsonNodeNonNull(final T value) {
        return JsonNode.number(value.doubleValue());
    }
}
