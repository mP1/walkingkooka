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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public final class HttpStatusCodeTest extends ClassTestCase<HttpStatusCode> implements ConstantsTesting<HttpStatusCode> {

    @Test
    public void testStatusDefaultMessageUnique() {
        final Map<String, HttpStatusCode> intToCode = Maps.ordered();
        for (HttpStatusCode code : HttpStatusCode.values()) {
            assertNull("Default message is not unique " + code.message + "=" + code, intToCode.put(code.message, code));
        }
    }

    @Test
    public void testStatus() {
        for (HttpStatusCode code : HttpStatusCode.values()) {
            final HttpStatus status = code.status();
            assertSame("status not cached", status, code.status());
            assertSame("code", code, status.value());
            assertNotEquals("message", "", status.message());
        }
    }

    // category ....................................................................

    @Test
    public void testCategory1xx() {
        this.categoryAndCheck(HttpStatusCode.CONTINUE, HttpStatusCodeCategory.INFORMATION);
    }

    @Test
    public void testCategory2xx() {
        this.categoryAndCheck(HttpStatusCode.OK, HttpStatusCodeCategory.SUCCESSFUL);
    }

    @Test
    public void testCategory3xx() {
        this.categoryAndCheck(HttpStatusCode.MOVED_TEMPORARILY, HttpStatusCodeCategory.REDIRECTION);
    }

    @Test
    public void testCategory4xx() {
        this.categoryAndCheck(HttpStatusCode.BAD_REQUEST, HttpStatusCodeCategory.CLIENT_ERROR);
    }

    @Test
    public void testCategory5xx() {
        this.categoryAndCheck(HttpStatusCode.INTERNAL_SERVER_ERROR, HttpStatusCodeCategory.SERVER_ERROR);
    }

    private void categoryAndCheck(final HttpStatusCode code, final HttpStatusCodeCategory category) {
        assertSame("category for status code: " + code, category, code.category());
    }

    // setMessage..............................................................

    @Test(expected = NullPointerException.class)
    public void testSetMessageNullFails() {
        HttpStatusCode.OK.setMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMessageEmptyFails() {
        HttpStatusCode.OK.setMessage("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMessageWhitespaceFails() {
        HttpStatusCode.OK.setMessage("  ");
    }

    @Test
    public void testSetMessageDefault() {
        assertSame("set message with default should return constant",
                HttpStatusCode.OK.status(),
                HttpStatusCode.OK.setMessage(HttpStatusCode.OK.message));
    }

    @Test
    public void testSetMessage() {
        final String message = "Message something something2";
        final HttpStatusCode code = HttpStatusCode.MOVED_TEMPORARILY;
        final HttpStatus status = code.setMessage(message);
        assertEquals("code", code, status.value());
        assertEquals("message", message, status.message());
    }

    @Override
    public Class<HttpStatusCode> type() {
        return HttpStatusCode.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Set<HttpStatusCode> intentionalDuplicateConstants() {
        return Sets.empty();
    }
}
