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
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;

public abstract class HeaderValueTestCase<V extends HeaderValue> extends PublicClassTestCase<V> {

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
    
    abstract protected boolean isMultipart();

    abstract protected boolean isRequest();

    abstract protected boolean isResponse();

    abstract protected V createHeaderValue();

    protected void toHeaderTextAndCheck(final String expected) {
        this.toHeaderTextAndCheck(this.createHeaderValue(), expected);
    }

    protected void toHeaderTextAndCheck(final HeaderValue headerValue, final String expected) {
        assertEquals("headerText of " + headerValue, expected, headerValue.toHeaderText());
    }
}
