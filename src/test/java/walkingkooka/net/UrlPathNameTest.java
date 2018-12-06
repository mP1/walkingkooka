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

package walkingkooka.net;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CharSequences;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class UrlPathNameTest extends NameTestCase<UrlPathName> {

    @Test
    @Ignore
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testEmpty() {
        assertSame(UrlPathName.ROOT, UrlPathName.with(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncludesSeparatorFails() {
        UrlPathName.with("abc" + UrlPath.SEPARATOR.character() + "xyz");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTooLongFails() {
        final char[] chars = new char[UrlPathName.MAXIMUM_LENGTH + 1];
        Arrays.fill(chars, 'x');
        UrlPathName.with(new String(chars));
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc");
    }

    @Test
    public void testWithPercentage() {
        this.createNameAndCheck("abc%20def");
    }

    @Test
    public void testToStringAddQuotesBecauseOfWhitespace() {
        final String string = "name with whitespace";
        final UrlPathName name = UrlPathName.with(string);
        assertEquals(CharSequences.quote(string).toString(), name.toString());
    }

    @Override
    protected UrlPathName createName(final String name) {
        return UrlPathName.with(name);
    }

    @Override
    protected Class<UrlPathName> type() {
        return UrlPathName.class;
    }
}
