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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.JavaVisibility;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpacityTest implements ClassTesting2<Opacity>,
        ComparableTesting<Opacity>,
        ConstantsTesting<Opacity>,
        SerializationTesting<Opacity>,
        HasJsonNodeTesting<Opacity>,
        ToStringTesting<Opacity> {

    private final static double VALUE = 0.25;

    @Test
    public void testWithNegativeValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Opacity.with(-0.1);
        });
    }

    @Test
    public void testWithGreaterValueFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            Opacity.with(1.01);
        });
    }

    @Test
    public void testWith() {
        final double value = 0.5;
        final Opacity size = Opacity.with(value);
        assertEquals(value, size.value(), "value");
    }

    @Test
    public void testTransparent() {
        assertSame(Opacity.TRANSPARENT, Opacity.with(Opacity.TRANSPARENT.value()));
    }

    @Test
    public void testOpaque() {
        assertSame(Opacity.OPAQUE, Opacity.with(Opacity.OPAQUE.value()));
    }

    // HasJsonNode......................................................................................

    @Test
    public void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public void testFromJsonNodeNullFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public void testFromJsonNodeInvalidStringFails() {
        this.fromJsonNodeFails(JsonNode.string("not transparent or opaque"));
    }

    @Test
    public void testFromJsonNodeArrayFails() {
        this.fromJsonNodeFails(JsonNode.array());
    }

    @Test
    public void testFromJsonNodeObjectFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }

    @Test
    public void testFromJsonNodeNumberInvalidFails() {
        this.fromJsonNodeFails(JsonNode.number(-1));
    }

    @Test
    public void testFromJsonTransparent() {
        this.fromJsonNodeAndCheck(JsonNode.string("transparent"),
                Opacity.TRANSPARENT);
    }

    @Test
    public void testFromJsonOpaque() {
        this.fromJsonNodeAndCheck(JsonNode.string("opaque"),
                Opacity.OPAQUE);
    }

    @Test
    public void testFromJsonOpaque2() {
        this.fromJsonNodeAndCheck(JsonNode.array()
                        .appendChild(JsonNode.string("opaque"))
                        .get(0),
                Opacity.OPAQUE);
    }

    @Test
    public void testFromJsonNumber() {
        final double value = 0.25;
        this.fromJsonNodeAndCheck(JsonNode.number(value),
                Opacity.with(value));
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck(this.createComparable(), JsonNode.number(VALUE));
    }

    @Test
    public void testToJsonNodeRoundtripTwice() {
        this.toJsonNodeRoundTripTwiceAndCheck(this.createObject());
    }

    @Test
    public void testToJsonNodeRoundtripTransparent() {
        this.toJsonNodeRoundTripTwiceAndCheck(Opacity.TRANSPARENT);
    }

    @Test
    public void testToJsonNodeRoundtripOpaque() {
        this.toJsonNodeRoundTripTwiceAndCheck(Opacity.OPAQUE);
    }

    // Serializable.....................................................................................................

    @Test
    public void testSerializeTransparent() throws Exception {
        this.serializeSingletonAndCheck(Opacity.TRANSPARENT);
    }

    @Test
    public void testSerializeOpaque() throws Exception {
        this.serializeSingletonAndCheck(Opacity.OPAQUE);
    }

    // Object...........................................................................................................

    @Test
    public void testToStringOpaque() {
        this.toStringAndCheck(Opacity.OPAQUE, "opaque");
    }

    @Test
    public void testToStringTransparent() {
        this.toStringAndCheck(Opacity.TRANSPARENT, "transparent");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Opacity.with(0.25), "25%");
    }

    @Override
    public Set<Opacity> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    @Override
    public Opacity createComparable() {
        return Opacity.with(VALUE);
    }

    @Override
    public Opacity serializableInstance() {
        return this.createComparable();
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public Class<Opacity> type() {
        return Opacity.class;
    }

    // HasJsonNodeTesting.....................................................................

    @Override
    public Opacity createHasJsonNode() {
        return this.createObject();
    }

    @Override
    public Opacity fromJsonNode(final JsonNode jsonNode) {
        return Opacity.fromJsonNode(jsonNode);
    }
}
