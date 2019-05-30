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
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.FieldAttributes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TextPropertyNameTest extends TextNodeNameNameTestCase<TextPropertyName<?>>
        implements NameTesting2<TextPropertyName<?>, TextPropertyName<?>> {

    @Test
    public void testConstants() {
        assertEquals(Lists.empty(),
                Arrays.stream(TextPropertyName.class.getDeclaredFields())
                        .filter(FieldAttributes.STATIC::is)
                        .filter(f -> f.getType() == TextPropertyName.class)
                        .filter(TextPropertyNameTest::constantNotCached)
                        .collect(Collectors.toList()),
                "");
    }

    private static boolean constantNotCached(final Field field) {
        try {
            final TextPropertyName<?> name = Cast.to(field.get(null));
            return name != TextPropertyName.with(name.value());
        } catch (final Exception cause) {
            throw new AssertionError(cause.getMessage(), cause);
        }
    }

    @Override
    public TextPropertyName<?> createName(final String name) {
        return TextPropertyName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return "xyz";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "before";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "-.";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return 0 == position ?
                CONTROL + ASCII_DIGITS :
                CONTROL;
    }

    @Override
    public Class<TextPropertyName<?>> type() {
        return Cast.to(TextPropertyName.class);
    }
}
