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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharSequences;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class ETagTest extends HeaderValueTestCase<ETag> {

    private final static String VALUE = "123";
    private final static ETagValidator WEAK = ETagValidator.WEAK;

    // with .....................................................................................


    @Test(expected = NullPointerException.class)
    public void testWithNullValueFails() {
        ETag.with(null, ETagValidator.STRONG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidValueCharacterFails() {
        ETag.with("abc def", ETagValidator.STRONG);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullWeaknessIndicatorFails() {
        ETag.with(VALUE, null);
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
        assertEquals("\"abc123\"", ETag.with("abc123", ETagValidator.STRONG).toString());
    }

    @Test
    public void testToStringWeak() {
        assertEquals("W/\"abc123\"", ETag.with("abc123", WEAK).toString());
    }

    // toHeaderTextList.......................................................................................

    @Test(expected = NullPointerException.class)
    public void testToHeaderTextListListNullFails() {
        ETag.toHeaderTextList(null);
    }

    @Test
    public void testToHeaderTextListListOfOne() {
        this.toHeaderTextListAndCheck("\"abc123\"",
                ETag.with("abc123", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfOne2() {
        this.toHeaderTextListAndCheck("W/\"abc123\"",
                ETag.with("abc123", ETagValidator.WEAK));
    }

    @Test
    public void testToHeaderTextListListOfOneWildcard() {
        this.toHeaderTextListAndCheck("*",
                ETag.wildcard());
    }

    @Test
    public void testToHeaderTextListListOfMany() {
        this.toHeaderTextListAndCheck("\"1\", \"2\"",
                ETag.with("1", ETagValidator.STRONG),
                ETag.with("2", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfMany2() {
        this.toHeaderTextListAndCheck("\"11\", \"22\"",
                ETag.with("11", ETagValidator.STRONG),
                ETag.with("22", ETagValidator.STRONG));
    }

    @Test
    public void testToHeaderTextListListOfMany3() {
        this.toHeaderTextListAndCheck("W/\"11\", \"22\"",
                ETag.with("11", ETagValidator.WEAK),
                ETag.with("22", ETagValidator.STRONG));
    }

    private void toHeaderTextListAndCheck(final String toString, final ETag... tags) {
        assertEquals("ETag.toString(List) failed =" + CharSequences.quote(toString),
                toString,
                ETag.toHeaderTextList(Lists.of(tags)));
    }

    @Override
    protected ETag createHeaderValue() {
        return ETag.with("A", ETagValidator.WEAK);
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<ETag> type() {
        return ETag.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
