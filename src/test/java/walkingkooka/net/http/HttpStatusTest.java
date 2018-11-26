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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.test.PublicClassTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

final public class HttpStatusTest extends PublicClassTestCase<HttpStatus> {

    // constants

    private final static HttpStatusCode CODE = HttpStatusCode.OK;
    private final static String MESSAGE = "OK";

    @Test
    public void testWith() {
        this.check(this.status(), CODE, MESSAGE);
    }

    // setCode ................................................................

    @Test(expected = NullPointerException.class)
    public void testSetCodeNullFails() {
        status().setCode(null);
    }

    @Test
    public void testSetCodeSame() {
        final HttpStatus status = this.status();
        assertSame(status, status.setCode(CODE));
    }

    @Test
    public void testSetCodeDifferent() {
        final HttpStatus status = this.status();
        final HttpStatusCode code = HttpStatusCode.BAD_REQUEST;
        final HttpStatus different = status.setCode(code);
        assertNotSame(status, different);
        this.check(different, code, MESSAGE);
    }

    // setMessage ................................................................

    @Test(expected = NullPointerException.class)
    public void testSetMessageNullFails() {
        status().setMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMessageEmptyFails() {
        status().setMessage("");
    }

    @Test
    public void testSetMessageSame() {
        final HttpStatus status = this.status();
        assertSame(status, status.setMessage(MESSAGE));
    }

    @Test
    public void testSetMessageDifferent() {
        final HttpStatus status = this.status();
        final String message = "different";
        final HttpStatus different = status.setMessage(message);
        assertNotSame(status, different);
        this.check(different, CODE, message);
    }

    // helpers ..................................

    private HttpStatus status() {
        return HttpStatus.with(CODE, MESSAGE);
    }

    private void check(final HttpStatus status, final HttpStatusCode code, final String message) {
        assertEquals("value", code, status.value());
        assertEquals("message", message, status.message());
    }

    @Test
    public void testToString() {
        assertEquals("200 OK", this.status().toString());
    }

    @Override
    protected Class<HttpStatus> type() {
        return HttpStatus.class;
    }
}
