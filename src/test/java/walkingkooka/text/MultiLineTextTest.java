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
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.CanBinaryTesting;
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.PublicClassTesting;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiLineTextTest implements CanBinaryTesting,
    CanBeEmptyTesting,
    HasValueTesting,
    HasTextTesting,
    HashCodeEqualsDefinedTesting2<MultiLineText>,
    ToStringTesting,
    PublicClassTesting<MultiLineText> {

    private final String VALUE = "Hello 123";

    @Test
    public void testEmpty() {
        this.valueAndCheck(
            MultiLineText.EMPTY,
            ""
        );
    }

    // setText..........................................................................................................

    @Test
    public void testSetTextNullStringFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiLineText.EMPTY.setText(null)
        );
    }

    @Test
    public void testSetTextWithSame() {
        this.setTextAndCheck(
            new MultiLineText(VALUE),
            VALUE,
            new MultiLineText(VALUE)
        );
    }

    @Test
    public void testSetTextWithEmptyString() {
        final String value = "";

        this.setTextAndCheck(
            MultiLineText.EMPTY,
            value,
            MultiLineText.EMPTY
        );
    }

    @Test
    public void testSetTextWithEmptyString2() {
        this.setTextAndCheck(
            new MultiLineText(VALUE),
            "",
            MultiLineText.EMPTY
        );
    }

    @Test
    public void testSetTextWithDifferent() {
        this.setTextAndCheck(
            new MultiLineText("first"),
            VALUE,
            new MultiLineText(VALUE)
        );
    }

    private void setTextAndCheck(final MultiLineText multiLineText,
                                 final String value,
                                 final MultiLineText expected) {
        final MultiLineText actual = multiLineText.setText(value);

        if (multiLineText.equals(expected)) {
            assertSame(
                multiLineText,
                actual
            );
        } else {
            assertNotSame(
                actual,
                multiLineText
            );
        }

        this.valueAndCheck(
            actual,
            value
        );

        this.textAndCheck(
            actual,
            value
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
            new MultiLineText("different")
        );
    }

    @Override
    public MultiLineText createObject() {
        return new MultiLineText(VALUE);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            VALUE
        );
    }

    // CanBinary........................................................................................................

    @Test
    public void testCanBinary() {
        this.binaryAndCheck(
            this.createObject(),
            CHARSET,
            Binary.with(
                VALUE.getBytes(CHARSET)
            )
        );
    }

    // CanBeEmpty......................................................................................................

    @Test
    public void testIsEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            MultiLineText.EMPTY,
            true
        );
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
            this.createObject(),
            false
        );
    }

    // class............................................................................................................

    @Override
    public Class<MultiLineText> type() {
        return MultiLineText.class;
    }
}
