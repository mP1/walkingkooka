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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NumberLengthTest extends LengthTestCase<NumberLength, Long> {

    @Test
    public void testParseInvalidNumberFails() {
        this.parseFails("A", IllegalArgumentException.class);
    }

    @Test
    public void testParseIncorrectUnitFails() {
        this.parseFails("12EM", IllegalArgumentException.class);
    }

    @Test
    public void testParseIncorrectUnitCaseFails() {
        this.parseFails("12PX", IllegalArgumentException.class);
    }

    @Test
    public void testParse() {
        this.parseAndCheck("12", NumberLength.with(12L));
    }

    @Test
    public void testWith() {
        final Long value = 12L;
        final NumberLength number = NumberLength.with(value);
        assertEquals(value, number.value(), "value");
    }

    @Test
    public void testWithNegativeFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            NumberLength.with(-1L);
        });
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(NumberLength.with(99L));
    }

    // LengthVisitor....................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        final NumberLength length = this.createLength();
        new FakeLengthVisitor() {
            @Override
            protected Visiting startVisit(final Length<?> l) {
                assertSame(length, l, "length");
                b.append("1");
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final Length<?> l) {
                assertSame(length, l, "length");
                b.append("2");
            }

            @Override
            protected void visit(final NumberLength l) {
                assertSame(length, l, "length");
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    // toString........................................................................................................


    @Test
    public void testToString() {
        this.toStringAndCheck(NumberLength.with(0L), "0");
    }

    @Test
    public void testToStringDecimal() {
        this.toStringAndCheck(NumberLength.with(10L), "10");
    }

    @Override
    NumberLength createLength() {
        return NumberLength.with(123L);
    }

    @Override
    Optional<LengthUnit<Long, Length<Long>>> unit() {
        return Optional.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NumberLength> type() {
        return NumberLength.class;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public NumberLength parse(final String text) {
        return NumberLength.parseNumber(text);
    }
    // HasJsonNodeTesting...............................................................................................

    @Override
    public NumberLength fromJsonNode(final JsonNode from) {
        return NumberLength.fromJsonNodeNumber(from);
    }

}
