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
import walkingkooka.Cast;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.type.MemberVisibility;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class IfRangeTest extends HeaderValueTestCase<IfRange<?>> implements ParseStringTesting<IfRange<?>> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            IfRange.with(null);
        });
    }

    @Test
    public void testWithEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IfRange.with("");
        });
    }

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            IfRange.parse(null);
        });
    }

    @Test
    public void testParseEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            IfRange.parse("");
        });
    }

    @Test
    public void testParseInvalidFails() {
        assertThrows(HeaderValueException.class, () -> {
            IfRange.parse("\"1234567890abcdef");
        });
    }

    @Test
    public void testParseETag() {
        final ETag etag = ETagValidator.WEAK.setValue("abc");
        this.parseAndCheck(etag.toHeaderText(),
                IfRange.with(etag));
    }

    @Test
    public void testParseLastModified() {
        final LocalDateTime lastModified = LocalDateTime.of(2000, 12, 31, 6, 28, 29);
        this.parseAndCheck(HttpHeaderName.LAST_MODIFIED.headerText(lastModified),
                IfRange.with(lastModified));
    }

    // ParseStringTesting ........................................................................................

    @Override
    public IfRange<?> parse(final String text) {
        return IfRange.parse(text);
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
        return false;
    }

    @Override
    protected IfRange<?> createHeaderValue() {
        return IfRange.with(LocalDateTime.of(2000, 12, 31, 6, 28, 29));
    }

    @Override
    protected Class<IfRange<?>> type() {
        return Cast.to(IfRange.class);
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
