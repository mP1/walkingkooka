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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ConstantsTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpStatusCodeTest implements ClassTesting2<HttpStatusCode>,
        ConstantsTesting<HttpStatusCode>,
        ToStringTesting<HttpStatusCode>,
        TypeNameTesting<HttpStatusCode> {

    @Test
    public void testStatusDefaultMessageUnique() {
        final Map<String, HttpStatusCode> intToCode = Maps.ordered();
        for (HttpStatusCode code : HttpStatusCode.values()) {
            assertNull(intToCode.put(code.message, code), "Default message is not unique " + code.message + "=" + code);
        }
    }

    @Test
    public void testStatus() {
        for (HttpStatusCode code : HttpStatusCode.values()) {
            final HttpStatus status = code.status();
            assertSame(status, code.status(), "status not cached");
            assertSame(code, status.value(), "code");
            assertNotEquals( "", status.message(), "message");
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
        assertSame(category, code.category(), "category for status code: " + code);
    }

    // setMessage..............................................................

    @Test
    public void testSetMessageNullFails() {
        assertThrows(NullPointerException.class, () -> {
            HttpStatusCode.OK.setMessage(null);
        });
    }

    @Test
    public void testSetMessageEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HttpStatusCode.OK.setMessage("");
        });
    }

    @Test
    public void testSetMessageWhitespaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            HttpStatusCode.OK.setMessage("  ");
        });
    }

    @Test
    public void testSetMessageDefault() {
        assertSame(HttpStatusCode.OK.status(),
                HttpStatusCode.OK.setMessage(HttpStatusCode.OK.message),
                "set message with default should return constant");
    }

    @Test
    public void testSetMessage() {
        final String message = "Message something something2";
        final HttpStatusCode code = HttpStatusCode.MOVED_TEMPORARILY;
        final HttpStatus status = code.setMessage(message);
        assertEquals(code, status.value(), "code");
        assertEquals(message, status.message(), "message");
    }

    // setMessageOrDefault..............................................................

    @Test
    public void testSetMessageOrDefaultNullDefaults() {
        this.setMessageOrDefaultAndCheck(HttpStatusCode.OK,
                null,
                HttpStatusCode.OK.status());
    }

    @Test
    public void testSetMessageOrDefaultEmptyDefaults() {
        this.setMessageOrDefaultAndCheck(HttpStatusCode.OK,
                "",
                HttpStatusCode.OK.status());
    }

    @Test
    public void testSetMessageOrDefaultWhitespaceDefaults() {
        this.setMessageOrDefaultAndCheck(HttpStatusCode.OK,
                "   ",
                HttpStatusCode.OK.status());
    }

    @Test
    public void testSetMessageOrDefault() {
        final String message = "message 123";
        this.setMessageOrDefaultAndCheck(HttpStatusCode.OK,
                message,
                HttpStatus.with(HttpStatusCode.OK, message));
    }

    @Test
    public void testSetMessageOrDefaultDefault() {
        assertSame(HttpStatusCode.OK.status(),
                HttpStatusCode.OK.setMessage(HttpStatusCode.OK.message),
                "set message with default should return constant");
    }

    private void setMessageOrDefaultAndCheck(final HttpStatusCode code,
                                             final String message,
                                             final HttpStatus status) {
        assertEquals(status, code.setMessageOrDefault(message));
    }

    @Override
    public Class<HttpStatusCode> type() {
        return HttpStatusCode.class;
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public Set<HttpStatusCode> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return HttpStatus.class.getSimpleName();
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
