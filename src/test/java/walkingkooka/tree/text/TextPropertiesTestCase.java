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
import walkingkooka.Cast;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class TextPropertiesTestCase<T extends TextProperties> implements ClassTesting2<TextProperties>,
        HashCodeEqualsDefinedTesting<TextProperties>,
        HasJsonNodeTesting<TextProperties>,
        ToStringTesting<TextProperties> {

    TextPropertiesTestCase() {
        super();
    }

    // get..............................................................................................................

    @Test
    public final void testGetNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createObject().get(null);
        });
    }

    @Test
    public final void testGetUnknown() {
        this.getAndCheck(this.createObject(),
                TextPropertyName.HYPHENS,
                null);
    }

    final <T> void getAndCheck(final TextProperties properties,
                               final TextPropertyName<T> propertyName,
                               final T value) {
        assertEquals(Optional.ofNullable(value),
                properties.get(propertyName),
                () -> properties + " get " + propertyName);
    }

    // set..............................................................................................................

    @Test
    public final void testSetNullPropertyNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createObject().set(null, "value");
        });
    }

    @Test
    public final void testSetNullPropertyValueFails() {
        assertThrows(TextPropertyValueException.class, () -> {
            this.createObject().set(TextPropertyName.FONT_FAMILY_NAME, null);
        });
    }

    @Test
    public final void testSetInvalidPropertyValueFails() {
        assertThrows(TextPropertyValueException.class, () -> {
            final TextPropertyName<?> propertyName = TextPropertyName.FONT_FAMILY_NAME;
            this.createObject().set(propertyName, Cast.to("invalid"));
        });
    }

    final <T> TextProperties setAndCheck(final TextProperties properties,
                                         final TextPropertyName<T> propertyName,
                                         final T value,
                                         final TextProperties expected) {
        final TextProperties set = properties.set(propertyName, value);
        assertEquals(expected,
                set,
                () -> properties + " set " + propertyName + " and " + CharSequences.quoteIfChars(value));
        return set;
    }

    // remove...........................................................................................................

    @Test
    public final void testRemoveNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createObject().remove(null);
        });
    }

    @Test
    public final void testRemoveUnknown() {
        final TextProperties properties = this.createObject();
        assertSame(properties, properties.remove(TextPropertyName.HYPHENS));
    }

    final <T> TextProperties removeAndCheck(final TextProperties properties,
                                            final TextPropertyName<T> propertyName,
                                            final TextProperties expected) {
        final TextProperties removed = properties.remove(propertyName);
        assertEquals(expected,
                removed,
                () -> properties + " remove " + propertyName);
        return removed;
    }

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextProperties> type() {
        return Cast.to(this.textPropertiesType());
    }

    abstract Class<T> textPropertiesType();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // HasJsonNodeTesting................................................................................................

    @Override
    public final TextProperties fromJsonNode(final JsonNode from) {
        return TextProperties.fromJsonNode(from);
    }

    @Override
    public final TextProperties createHasJsonNode() {
        return this.createObject();
    }
}
