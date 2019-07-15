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

final class HasJsonNodeLongMapper extends HasJsonNodeTypedMapper<Long> {

    static HasJsonNodeLongMapper instance() {
        return new HasJsonNodeLongMapper();
    }

    private HasJsonNodeLongMapper() {
        super();
    }

    @Override
    Class<Long> type() {
        return Long.class;
    }

    @Override
    Long fromJsonNodeNull() {
        return null;
    }

    @Override
    Long fromJsonNodeNonNull(final JsonNode node) {
        return node.isNumber() ?
                this.fromJsonNodeNumber(node.numberValueOrFail()) :
                this.fromJsonNodeString(node);
    }

    private Long fromJsonNodeNumber(final Number number) {
        final double doubleValue = number.doubleValue();
        final long longValue = number.longValue();
        if (doubleValue != longValue) {
            throw new NumericLossJsonNodeException("Unable to convert " + number + " to Long");
        }
        return Long.valueOf(longValue);
    }

    private Long fromJsonNodeString(final JsonNode node) {
        final String text = node.stringValueOrFail();
        return Long.parseLong(text);
    }

    @Override
    JsonStringNode typeName() {
        return TYPE_NAME;
    }

    private final JsonStringNode TYPE_NAME = JsonStringNode.with("long");

    @Override
    JsonNode toJsonNodeNonNull(final Long value) {
        return JsonNode.string(value.toString());
    }
}
