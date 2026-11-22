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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.PublicClassTesting;

import java.util.Collection;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiLineTextTest implements CanBinaryTesting,
    CanBeEmptyTesting,
    HasValueTesting,
    HasTextTesting,
    HashCodeEqualsDefinedTesting2<MultiLineText>,
    TextContextTesting,
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

    // fromCollection...................................................................................................

    private final static Function<Number, String> MAPPER = (Number number) -> null == number ?
        "*" :
        "@" + number;

    @Test
    public void testFromCollectionWithNullCollectionFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiLineText.fromCollection(
                null,
                MAPPER,
                TEXT_CONTEXT
            )
        );
    }

    @Test
    public void testFromCollectionWithNullMapperFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiLineText.fromCollection(
                Lists.empty(),
                null,
                TEXT_CONTEXT
            )
        );
    }

    @Test
    public void testFromCollectionWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> MultiLineText.fromCollection(
                Lists.empty(),
                MAPPER,
                null
            )
        );
    }

    @Test
    public void testFromCollectionWithEmpty() {
        this.fromCollectionAndCheck(
            Lists.empty(),
            MultiLineText.EMPTY
        );
    }

    @Test
    public void testFromCollectionWithOne() {
        this.fromCollectionAndCheck(
            Lists.of(11),
            MultiLineText.EMPTY.setText("@11")
        );
    }

    @Test
    public void testFromCollectionWithSeveral() {
        this.fromCollectionAndCheck(
            Lists.of(
                11,
                22,
                33
            ),
            MultiLineText.EMPTY.setText("@11\n@22\n@33")
        );
    }

    @Test
    public void testFromCollectionWithSeveral2() {
        this.fromCollectionAndCheck(
            Lists.of(
                11L,
                22L,
                33L
            ),
            (Long longValue) -> String.valueOf(longValue),
            DIFFERENT_TEXT_CONTEXT,
            MultiLineText.EMPTY.setText("11\r\n" +
                "22\r\n" +
                "33")
        );
    }

    @Test
    public void testFromCollectionIncludesNull() {
        this.fromCollectionAndCheck(
            Lists.of(
                11,
                null,
                33
            ),
            MultiLineText.EMPTY.setText("@11\n*\n@33")
        );
    }

    private void fromCollectionAndCheck(final Collection<Number> collection,
                                        final MultiLineText expected) {
        this.fromCollectionAndCheck(
            collection,
            MAPPER,
            TEXT_CONTEXT,
            expected
        );
    }

    private <T> void fromCollectionAndCheck(final Collection<T> collection,
                                            final Function<T, String> mapper,
                                            final TextContext context,
                                            final MultiLineText expected) {
        this.checkEquals(
            expected,
            MultiLineText.fromCollection(
                collection,
                mapper,
                context
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MultiLineText> type() {
        return MultiLineText.class;
    }
}
