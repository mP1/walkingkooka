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

package walkingkooka.tree.json;

abstract class HasJsonNodeMapper3<T extends Number> extends HasJsonNodeMapper2<T> {

    HasJsonNodeMapper3() {
        super();
    }

    @Override
    final T fromJsonNode0(final JsonNode node) {
        return this.fromJsonNode1(node.numberValueOrFail());
    }

    @Override
    final T fromJsonNodeNull() {
        return null;
    }

    /**
     * Ensures that no precision is lost.
     */
    private T fromJsonNode1(final Number number) {
        final T number2 = this.fromJsonNode2(number);
        if(number2.doubleValue() != number.doubleValue()) {
            throw new NumericLossJsonNodeException(number.getClass().getName() + "=" + number);
        }
        return number2;
    }

    abstract T fromJsonNode2(final Number number);

    @Override
    final JsonNode toJsonNode0(final T value) {
        return JsonNode.number(value.doubleValue());
    }
}
