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

package walkingkooka.color;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.NameTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.type.FieldAttributes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WebColorNameTest implements NameTesting2<WebColorName, WebColorName> {

    @Test
    public void testConstants() {
        assertEquals(Lists.empty(),
                Arrays.stream(WebColorName.class.getDeclaredFields())
                        .filter(FieldAttributes.STATIC::is)
                        .filter(f -> f.getType() == WebColorName.class)
                        .filter(WebColorNameTest::constantNotCached)
                        .collect(Collectors.toList()),
                "");
    }

    private static boolean constantNotCached(final Field field) {
        try {
            final WebColorName name = Cast.to(field.get(null));
            return !Optional.of(name).equals(WebColorName.with(name.value()));
        } catch (final Exception cause) {
            throw new AssertionError(cause.getMessage(), cause);
        }
    }

    @Test
    public void testEmptyFails() {
        // nop
    }

    @Test
    public void testUnknownName() {
        this.colorAndCheck("unknown", null);
    }

    @Test
    public void testBlack() {
        this.colorAndCheck("black", Color.BLACK);
    }

    @Test
    public void testBlackUpperCase() {
        this.colorAndCheck("BLACK", Color.BLACK);
    }

    @Test
    public void testSilver() {
        this.colorAndCheck("silver", Color.fromRgb(0xc0c0c0));
    }

    @Test
    public void testTransparent() {
        this.colorAndCheck("transparent", Color.parseColor("#00000000"));
    }

    private void colorAndCheck(final String name, final Color color) {
        assertEquals(Optional.ofNullable(color),
                Optional.ofNullable(WebColorName.with(name).map(c -> c.color()).orElse(null)),
                () -> "name " + CharSequences.quoteAndEscape(name));
    }

    @Override
    public void testCompareDifferentCase() {
    }

    @Override
    public void testNameInvalidCharFails() {
    }

    @Override
    public void testNameValidChars() {
    }

    @Override
    public WebColorName createName(final String name) {
        return WebColorName.with(name).get();
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return "cyan";
    }

    @Override
    public String differentNameText() {
        return "red";
    }

    @Override
    public String nameTextLess() {
        return "aqua";
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
    public Class<WebColorName> type() {
        return Cast.to(WebColorName.class);
    }
}
