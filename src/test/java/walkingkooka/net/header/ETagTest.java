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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ETagTest extends HeaderValueTestCase<ETag> {

    private final static String VALUE = "123";
    private final static ETagValidator WEAK = ETagValidator.WEAK;

    // with .....................................................................................

    @Test
    public void testWithNullValueFails() {
        assertThrows(NullPointerException.class, () -> {
            ETag.with(null, ETagValidator.STRONG);
        });
    }

    @Test
    public void testWithInvalidValueCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ETag.with("abc def", ETagValidator.STRONG);
        });
    }

    @Test
    public void testWithNullWeaknessIndicatorFails() {
        assertThrows(NullPointerException.class, () -> {
            ETag.with(VALUE, null);
        });
    }

    // toHeaderText...................................................................

    @Test
    public void testToHeaderTextString() {
        this.toHeaderTextAndCheck(ETag.with("abc123", ETagValidator.STRONG),
                "\"abc123\"");
    }

    @Test
    public void testToHeaderTextWeak() {
        this.toHeaderTextAndCheck(ETag.with("abc123", ETagValidator.WEAK),
                "W/\"abc123\"");
    }

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // toString ...........................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(ETag.with("abc123", ETagValidator.STRONG), "\"abc123\"");
    }

    @Test
    public void testToStringWeak() {
        this.toStringAndCheck(ETag.with("abc123", WEAK), "W/\"abc123\"");
    }

    @Override
    public void testCheckToStringOverridden() {
        throw new UnsupportedOperationException();
    }

    // toHeaderTextList.......................................................................................

    @Test
    public void testToHeaderTextListOne() {
        this.toHeaderTextListAndCheck("\"abc123\"",
                ETag.with("abc123", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListOne2() {
        this.toHeaderTextListAndCheck("W/\"abc123\"",
                ETag.with("abc123", ETagValidator.WEAK));
    }

    @Test
    public void testToHeaderTextListOneWildcard() {
        this.toHeaderTextListAndCheck("*",
                ETag.wildcard());
    }

    @Test
    public void testToHeaderTextListSeveral() {
        this.toHeaderTextListAndCheck("\"1\", \"2\"",
                ETag.with("1", ETagValidator.STRONG),
                ETag.with("2", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListSeveral2() {
        this.toHeaderTextListAndCheck("\"11\", \"22\"",
                ETag.with("11", ETagValidator.STRONG),
                ETag.with("22", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListSeveral3() {
        this.toHeaderTextListAndCheck("W/\"11\", \"22\"",
                ETag.with("11", ETagValidator.WEAK),
                ETag.with("22", ETagValidator.STRONG));
    }

    private void toHeaderTextListAndCheck(final String toString, final ETag... tags) {
        assertEquals(toString,
                HeaderValue.toHeaderTextList(Lists.of(tags), HeaderValue.SEPARATOR.string().concat(" ")),
                "ETag.toString(List) failed =" + CharSequences.quote(toString));
    }

    @Override
    public ETag createHeaderValue() {
        return ETag.with("A", ETagValidator.WEAK);
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<ETag> type() {
        return ETag.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
