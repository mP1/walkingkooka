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
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextPropertyValueConverterTestCase<P extends TextPropertyValueConverter<T>, T> extends TextNodeTestCase<P>
        implements ToStringTesting<P>,
        TypeNameTesting<P> {

    TextPropertyValueConverterTestCase() {
        super();
    }

    @Test
    public final void testCheckNullValueFails() {
        this.checkFails(null, "Property " + this.propertyName().inQuotes() + " missing value");
    }

    @Test
    public final void testCheck() {
        this.check(this.propertyValue());
    }

    @Test
    public final void testCheckWrongValueTypeFails() {
        this.checkFails(this, "Property " + this.propertyName().inQuotes() + " value " + this + " is not a " + this.propertyValueType());
    }

    @Test
    public final void testRoundtripJson() {
        final T value = this.propertyValue();
        final P converter = this.converter();

        final JsonNode json = converter.toJsonNode(value);

        assertEquals(value,
                converter.fromJsonNode(json),
                () -> "value " + CharSequences.quoteIfChars(value) + " to json " + json);
    }

    final void check(final Object value) {
        this.converter().check(value, this.propertyName());
    }

    final void checkFails(final Object value, final String message) {
        final TextPropertyValueException thrown = assertThrows(TextPropertyValueException.class, () -> {
            this.check(value);
        });
        assertEquals(message, thrown.getMessage(), "message");
    }

    final void fromJsonNodeAndCheck(final JsonNode node, final T value) {
        assertEquals(value,
                this.converter().fromJsonNode(node),
                () -> "from JsonNode " + node);
    }

    final void toJsonNodeAndCheck(final T value, final JsonNode node) {
        assertEquals(node,
                this.converter().toJsonNode(value),
                () -> "toJsonNode " + CharSequences.quoteIfChars(value));
    }

    // helper...........................................................................................................

    abstract P converter();

    abstract TextPropertyName<T> propertyName();

    abstract T propertyValue();

    abstract String propertyValueType();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public final String typeNameSuffix() {
        return TextPropertyValueConverter.class.getSimpleName();
    }
}
