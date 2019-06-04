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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.tree.json.JsonNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NormalLengthTest extends LengthTestCase<NormalLength, Void> {

    @Test
    public void testParseInvalidTextFails() {
        this.parseFails("12px", IllegalArgumentException.class);
    }

    @Test
    public void testParse() {
        this.parseAndCheck("normal", NormalLength.INSTANCE);
    }

    @Test
    public void testWith() {
        assertThrows(UnsupportedOperationException.class, () -> {
            NormalLength.INSTANCE.value();
        });
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(NormalLength.INSTANCE, "normal");
    }

    @Override
    NormalLength createLength() {
        return NormalLength.INSTANCE;
    }

    @Override
    Optional<LengthUnit<Void, Length<Void>>> unit() {
        return Optional.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<NormalLength> type() {
        return NormalLength.class;
    }

    // ParseStringTesting...............................................................................................

    @Override
    public NormalLength parse(final String text) {
        return NormalLength.parseNormal(text);
    }
    // HasJsonNodeTesting...............................................................................................

    @Override
    public NormalLength fromJsonNode(final JsonNode from) {
        return NormalLength.fromJsonNode(from);
    }

}
