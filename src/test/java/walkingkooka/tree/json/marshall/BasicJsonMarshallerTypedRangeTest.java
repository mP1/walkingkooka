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

package walkingkooka.tree.json.marshall;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.compare.Range;
import walkingkooka.tree.json.JsonNode;


public final class BasicJsonMarshallerTypedRangeTest extends BasicJsonMarshallerTypedTestCase2<BasicJsonMarshallerTypedRange, Range<?>> {

    @Test
    public void testRangeAllFromJson() {
        this.unmarshallAndCheck(JsonNode.object(), Range.all());
    }

    @Test
    public void testRangeAllMarshall() {
        this.marshallAndCheck(Range.all(), JsonNode.object());
    }

    @Test
    public void testRangeGreaterThanFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1())),
                Range.greaterThan(1));
    }

    @Test
    public void testRangeGreaterThanMarshall() {
        this.marshallAndCheck(Range.greaterThan(1),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1())));
    }

    @Test
    public void testRangeGreaterThanEqualsFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1())),
                Range.greaterThanEquals(1));
    }

    @Test
    public void testRangeGreaterThanEqualsMarshall() {
        this.marshallAndCheck(Range.greaterThanEquals(1),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1())));
    }

    @Test
    public void testRangeLessThanFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1())),
                Range.lessThan(1));
    }

    @Test
    public void testRangeLessThanMarshall() {
        this.marshallAndCheck(Range.lessThan(1),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1())));
    }

    @Test
    public void testRangeLessThanEqualsFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1())),
                Range.lessThanEquals(1));
    }

    @Test
    public void testRangeLessThanEqualsMarshall() {
        this.marshallAndCheck(Range.lessThanEquals(1),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1())));
    }

    @Test
    public void testRangeLowerInclusiveUpperExclusiveFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1()))
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value2())),
                Range.greaterThanEquals(1).and(Range.lessThan(2)));
    }

    @Test
    public void testRangeLowerInclusiveUpperExclusiveMarshall() {
        this.marshallAndCheck(Range.greaterThanEquals(1).and(Range.lessThan(2)),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value1()))
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value2())));
    }

    @Test
    public void testRangeLowerExclusiveUpperInclusiveFromJson() {
        this.unmarshallAndCheck(JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1()))
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value2())),
                Range.greaterThan(1).and(Range.lessThanEquals(2)));
    }

    @Test
    public void testRangeLowerExclusiveUpperInclusiveMarshall() {
        this.marshallAndCheck(Range.greaterThan(1).and(Range.lessThanEquals(2)),
                JsonNode.object()
                        .set(BasicJsonMarshallerTypedRange.LOWER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.EXCLUSIVE_PROPERTY, this.value1()))
                        .set(BasicJsonMarshallerTypedRange.UPPER_BOUND_PROPERTY,
                                JsonNode.object().set(BasicJsonMarshallerTypedRange.INCLUSIVE_PROPERTY, this.value2())));
    }

    private JsonNode value1() {
        return valueWithType(1);
    }

    private JsonNode value2() {
        return valueWithType(2);
    }

    private JsonNode valueWithType(final Integer value) {
        return this.marshallContext()
                .marshallWithType(value);
    }

    // helpers..........................................................................................................

    @Override
    BasicJsonMarshallerTypedRange marshaller() {
        return BasicJsonMarshallerTypedRange.instance();
    }

    @Override
    Range<Integer> value() {
        return Range.all();
    }

    @Override
    JsonNode node() {
        return JsonNode.object();
    }

    @Override
    Range<Integer> jsonNullNode() {
        return null;
    }

    @Override
    String typeName() {
        return "range";
    }

    @Override
    Class<Range<?>> marshallerType() {
        return Cast.to(Range.class);
    }

    @Override
    public Class<BasicJsonMarshallerTypedRange> type() {
        return BasicJsonMarshallerTypedRange.class;
    }
}
