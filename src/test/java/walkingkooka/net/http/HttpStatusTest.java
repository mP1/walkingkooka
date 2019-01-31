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

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class HttpStatusTest extends ClassTestCase<HttpStatus>
        implements HashCodeEqualsDefinedTesting<HttpStatus> {

    // constants

    private final static HttpStatusCode CODE = HttpStatusCode.OK;
    private final static String MESSAGE = "OK";

    @Test
    public void testWith() {
        this.check(this.status(), CODE, MESSAGE);
    }

    // setCode ................................................................

    @Test
    public void testSetCodeNullFails() {
        assertThrows(NullPointerException.class, () -> {
            status().setCode(null);
        });
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

    @Test
    public void testSetMessageNullFails() {
        assertThrows(NullPointerException.class, () -> {
            status().setMessage(null);
        });
    }

    @Test
    public void testSetMessageEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            status().setMessage("");
        });
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

    @Test
    public void testEqualsDifferentCode() {
        this.checkNotEquals(HttpStatus.with(HttpStatusCode.BAD_GATEWAY, MESSAGE));
    }

    @Test
    public void testEqualsDifferentMessage() {
        this.checkNotEquals(HttpStatus.with(CODE, "different"));
    }

    // helpers ..................................

    private HttpStatus status() {
        return HttpStatus.with(CODE, MESSAGE);
    }

    private void check(final HttpStatus status, final HttpStatusCode code, final String message) {
        assertEquals(code, status.value(), "value");
        assertEquals(message, status.message(), "message");
    }

    @Test
    public void testToString() {
        assertEquals("200 OK", this.status().toString());
    }

    @Override
    protected Class<HttpStatus> type() {
        return HttpStatus.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public HttpStatus createObject() {
        return this.status();
    }
}
