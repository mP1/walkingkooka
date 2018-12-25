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
import walkingkooka.test.ClassTestCase;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public abstract class HeaderValueTestCase<V extends HeaderValue> extends ClassTestCase<V> {

    @Test
    public final void testIsMultipart() {
        assertEquals(this.isMultipart(), this.createHeaderValue().isMultipart());
    }

    @Test
    public final void testIsRequest() {
        assertEquals(this.isRequest(), this.createHeaderValue().isRequest());
    }

    @Test
    public final void testIsResponse() {
        assertEquals(this.isResponse(), this.createHeaderValue().isResponse());
    }

    @Test
    public final void testIsWildcardHeaderText() {
        final V header = this.createHeaderValue();
        this.isWildcardAndCheck(header, String.valueOf(HeaderValue.WILDCARD).equals(header.toHeaderText()));
    }
    
    abstract protected boolean isMultipart();

    abstract protected boolean isRequest();

    abstract protected boolean isResponse();

    abstract protected V createHeaderValue();

    protected void toHeaderTextAndCheck(final String expected) {
        this.toHeaderTextAndCheck(this.createHeaderValue(), expected);
    }

    protected void toHeaderTextAndCheck(final HeaderValue headerValue, final String expected) {
        assertEquals("headerText of " + headerValue, expected, headerValue.toHeaderText());
        this.isWildcardAndCheck0(headerValue, String.valueOf(HeaderValue.WILDCARD).equals(expected));
    }

    protected void toHeaderTextListAndCheck(final String toString,
                                            final HeaderValue... headerValues) {
        assertEquals("toHeaderTextList returned wrong toString " + Arrays.toString(headerValues),
                toString,
                HeaderValue.toHeaderTextList(Lists.of(headerValues)));
    }

    protected void isWildcardAndCheck(final boolean expected) {
        this.isWildcardAndCheck(this.createHeaderValue(), expected);
    }

    protected void isWildcardAndCheck(final HeaderValue headerValue, final boolean expected) {
        this.isWildcardAndCheck0(headerValue, expected);

        final String text = headerValue.toHeaderText();
        this.isWildcardAndCheck0(headerValue, String.valueOf(HeaderValue.WILDCARD).equals(text) || "*/*".equals(text));
    }

    private void isWildcardAndCheck0(final HeaderValue headerValue, final boolean expected) {
        assertEquals("header " + headerValue, expected, headerValue.isWildcard());
    }
}
