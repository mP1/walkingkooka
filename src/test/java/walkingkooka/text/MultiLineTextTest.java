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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Binary;
import walkingkooka.CanBinaryTesting;
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiLineTextTest implements CanBinaryTesting,
    HasValueTesting,
    HasTextTesting,
    HashCodeEqualsDefinedTesting2<MultiLineText>,
    ToStringTesting<MultiLineText>,
    ClassTesting<MultiLineText> {

    @Test
    public void testWithNullStringFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiLineText.with(null)
        );
    }

    private final String VALUE = "Hello 123";

    @Test
    public void testWith() {
        final MultiLineText multiLineText = MultiLineText.with(VALUE);
        this.valueAndCheck(
            multiLineText,
            VALUE
        );

        this.textAndCheck(
            multiLineText,
            VALUE
        );
    }

    @Test
    public void testWithEmptyString() {
        final String value = "";

        final MultiLineText multiLineText = MultiLineText.with(value);
        this.valueAndCheck(
            multiLineText,
            value
        );

        this.textAndCheck(
            multiLineText,
            value
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
            MultiLineText.with("different")
        );
    }

    @Override
    public MultiLineText createObject() {
        return MultiLineText.with(VALUE);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            MultiLineText.with(VALUE),
            VALUE
        );
    }

    // CanBinary........................................................................................................

    @Test
    public void testCanBinary() {
        this.binaryAndCheck(
            MultiLineText.with(VALUE),
            CHARSET,
            Binary.with(
                VALUE.getBytes(CHARSET)
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MultiLineText> type() {
        return MultiLineText.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
