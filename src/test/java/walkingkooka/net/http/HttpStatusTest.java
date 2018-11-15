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
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

final public class HttpStatusTest extends PublicClassTestCase<HttpStatus> {

    // constants

    private final static int CODE = 200;
    private final static String MESSAGE = "message";

    // tests

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidCodeFails() {
        HttpStatus.with(-1, HttpStatusTest.MESSAGE);
    }

    @Test(expected = NullPointerException.class)
    public void testWithNullMessageFails() {
        HttpStatus.with(HttpStatusTest.CODE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyMessageFails() {
        HttpStatus.with(HttpStatusTest.CODE, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithWhitespaceOnlyMessageFails() {
        HttpStatus.with(HttpStatusTest.CODE, "   ");
    }

    @Test
    public void testWith() {
        final HttpStatus code = HttpStatus.with(HttpStatusTest.CODE, HttpStatusTest.MESSAGE);
        assertEquals("value", HttpStatusTest.CODE, code.value());
        assertSame("message", HttpStatusTest.MESSAGE, code.message());
    }

    @Test(expected = NullPointerException.class)
    public void testSetNullMessageFails() {
        HttpStatus.with(HttpStatusTest.CODE, HttpStatusTest.MESSAGE).setMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyMessageFails() {
        HttpStatus.with(HttpStatusTest.CODE, HttpStatusTest.MESSAGE).setMessage("");
    }

    @Test
    public void testSetSameMessage() {
        final HttpStatus code = HttpStatus.with(HttpStatusTest.CODE, HttpStatusTest.MESSAGE);
        assertSame(code, code.setMessage(HttpStatusTest.MESSAGE));
    }

    @Test
    public void testSetDifferentMessage() {
        final HttpStatus code = HttpStatus.with(HttpStatusTest.CODE, "different");
        final HttpStatus updated = code.setMessage(HttpStatusTest.MESSAGE);
        assertNotSame(code, updated);
        assertEquals("value", HttpStatusTest.CODE, updated.value());
        assertSame("message", HttpStatusTest.MESSAGE, updated.message());
    }

    @Test
    public void testUnknownCodeReturnsNull() {
        assertNull(HttpStatus.withCode(999));
    }

    @Test
    public void testKnownCode() throws Exception {
        for (final Field constant : HttpStatus.class.getDeclaredFields()) {
            if (false == constant.getType().equals(HttpStatus.class)) {
                continue;
            }
            assertTrue(constant + " is NOT public", MemberVisibility.PUBLIC.is(constant));
            assertTrue(constant + " is NOT static", FieldAttributes.STATIC.is(constant));

            final HttpStatus httpStatusCode = (HttpStatus) constant.get(null);
            final int code = httpStatusCode.value();
            if (code == 302) {
                continue;
            }
            assertSame(httpStatusCode, HttpStatus.withCode(httpStatusCode.value()));
        }
    }

    @Test
    public void testContinue() {
        final HttpStatus code = HttpStatus.CONTINUE;
        assertTrue(code + " should be a informational", code.isInformational());
        assertFalse(code + " should not be a successful", code.isSuccessful());
        assertFalse(code + " should not be a redirect", code.isRedirect());
        assertFalse(code + " should not be a client error", code.isClientError());
        assertFalse(code + " should not be server error", code.isServerError());
    }

    @Test
    public void testOk() {
        final HttpStatus code = HttpStatus.OK;
        assertFalse(code + " should not be informational", code.isInformational());
        assertTrue(code + " should be a successful", code.isSuccessful());
        assertFalse(code + " should not be redirect", code.isRedirect());
        assertFalse(code + " should not be a client error", code.isClientError());
        assertFalse(code + " should not be server error", code.isServerError());
    }

    /**
     * <pre>
     * In the HTTP protocol used by the World Wide Web, a redirect is a response with a status code beginning with 3 that causes a browser to display a different page. The different codes describe the reason for the redirect, which allows for the correct subsequent action (such as changing links in the case of code 301, a permanent change of address).
     * The HTTP standard defines several status codes for redirection:
     * 300 multiple choices (e.g. offer different languages)
     * 301 moved permanently
     * 302 found (originally temporary redirect, but now commonly used to specify redirection for unspecified reason)
     * 303 see other (e.g. for results of cgi-scripts)
     * 307 temporary redirect
     * All of these status codes require that the URL of the redirect target be given in the Location: header of the HTTP response. The 300 multiple choices will usually list all choices in the body of the message and show the default choice in the Location: header.
     * </pre>
     *
     * <a href="http://en.wikipedia.org/wiki/HTTP_redirect"></a>
     */
    @Test
    public void testMultipleChoicesIsRedirect() {
        this.checkRedirect(HttpStatus.MULTIPLE_CHOICES);
    }

    @Test
    public void testMovePermanentlyIsRedirect() {
        this.checkRedirect(HttpStatus.MOVED_PERMANENTLY);
    }

    @Test
    public void testFoundIsRedirect() {
        this.checkRedirect(HttpStatus.FOUND);
    }

    @Test
    public void testSeeOtherIsRedirect() {
        this.checkRedirect(HttpStatus.SEE_OTHER);
    }

    @Test
    public void testTemporaryRedirectIsRedirect() {
        this.checkRedirect(HttpStatus.TEMPORARY_REDIRECT);
    }

    private void checkRedirect(final HttpStatus status) {
        assertTrue(status + " should be a redirect", status.isRedirect());
    }

    /**
     * <pre>
     * (Status codes 304 not modified and 305 use proxy are not redirects).
     * </pre>
     *
     * <a href="http://en.wikipedia.org/wiki/HTTP_redirect"></a>}
     */
    @Test
    public void testNotModifiedNotRedirect() {
        this.checkNotRedirect(HttpStatus.NOT_MODIFIED);
    }

    @Test
    public void testUseProxyNotRedirect() {
        this.checkNotRedirect(HttpStatus.USE_PROXY);
    }

    private void checkNotRedirect(final HttpStatus status) {
        assertFalse(status + " should NOT be a redirect", status.isRedirect());
    }

    @Test
    public void testNotFound() {
        final HttpStatus code = HttpStatus.NOT_FOUND;
        assertFalse(code + " should not be a informational", code.isInformational());
        assertFalse(code + " should not be a successful", code.isSuccessful());
        assertFalse(code + " should not be a redirect", code.isRedirect());
        assertTrue(code + " should not be a client error", code.isClientError());
        assertFalse(code + " should not be a server error", code.isServerError());
    }

    @Test
    public void testInternalServerError() {
        final HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
        assertFalse(code + " should not be a informational", code.isInformational());
        assertFalse(code + " should not be a successful", code.isSuccessful());
        assertFalse(code + " should not be a redirect", code.isRedirect());
        assertFalse(code + " should not be a client error", code.isClientError());
        assertTrue(code + " should be a server error", code.isServerError());
    }

    @Test
    public void testToString() {
        assertEquals(HttpStatusTest.CODE + "=" + HttpStatusTest.MESSAGE,
                HttpStatus.with(HttpStatusTest.CODE, HttpStatusTest.MESSAGE).toString());
    }

    @Override
    protected Class<HttpStatus> type() {
        return HttpStatus.class;
    }
}
