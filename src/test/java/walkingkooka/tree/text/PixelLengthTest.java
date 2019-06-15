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
import walkingkooka.Cast;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.visit.Visiting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class PixelLengthTest extends LengthTestCase<PixelLength, Double> {

    @Test
    public void testParseMissingUnitFails() {
        this.parseFails("12", IllegalArgumentException.class);
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
        this.parseAndCheck("12px", PixelLength.with(12));
    }

    @Test
    public void testParse2() {
        this.parseAndCheck("12.5px", PixelLength.with(12.5));
    }

    @Test
    public void testParse3() {
        this.parseAndCheck("345.75px", PixelLength.with(345.75));
    }

    @Test
    public void testWith() {
        final double value = 12.5;
        final PixelLength pixels = PixelLength.with(value);
        assertEquals(value, pixels.value(), "value");
    }

    @Test
    public void testWithNegative() {
        final double value = -12.5;
        final PixelLength pixels = PixelLength.with(value);
        assertEquals(value, pixels.value(), "value");
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(PixelLength.with(99));
    }

    // LengthVisitor....................................................................................................

    @Test
    public void testVisitor() {
        final StringBuilder b = new StringBuilder();

        final PixelLength length = this.createLength();
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
            protected void visit(final PixelLength l) {
                assertSame(length, l, "length");
                b.append("3");
            }
        }.accept(length);

        assertEquals("132", b.toString());
    }

    // toString........................................................................................................


    @Test
    public void testToString() {
        this.toStringAndCheck(PixelLength.with(0), "0px");
    }

    @Test
    public void testToStringDecimal() {
        this.toStringAndCheck(PixelLength.with(10.5), "10.5px");
    }

    @Test
    public void testToString2() {
        this.toStringAndCheck(PixelLength.with(99), "99px");
    }

    @Override
    PixelLength createLength() {
        return PixelLength.with(123.5);
    }

    @Override
    Optional<LengthUnit<Double, Length<Double>>> unit() {
        return Cast.to(Optional.of(LengthUnit.PIXEL));
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PixelLength> type() {
        return PixelLength.class;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public PixelLength parse(String text) {
        return PixelLength.parsePixels(text);
    }
    // HasJsonNodeTesting...............................................................................................

    @Override
    public PixelLength fromJsonNode(final JsonNode from) {
        return PixelLength.fromJsonNodePixel(from);
    }

}
