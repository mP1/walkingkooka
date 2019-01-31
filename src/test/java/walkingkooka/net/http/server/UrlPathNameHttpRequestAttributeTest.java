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

package walkingkooka.net.http.server;

import org.junit.jupiter.api.Test;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPathName;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.type.MemberVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class UrlPathNameHttpRequestAttributeTest extends ClassTestCase<UrlPathNameHttpRequestAttribute>
        implements HashCodeEqualsDefinedTesting<UrlPathNameHttpRequestAttribute> {

    @Test
    public void testInvalidIndexFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            UrlPathNameHttpRequestAttribute.with(-1);
        });
    }

    @Test
    public void testCache() {
        assertSame(UrlPathNameHttpRequestAttribute.with(0), UrlPathNameHttpRequestAttribute.with(0));
    }

    @Test
    public void testUncached() {
        final int index = UrlPathNameHttpRequestAttribute.CONSTANT_COUNT;
        assertNotSame(UrlPathNameHttpRequestAttribute.with(index), UrlPathNameHttpRequestAttribute.with(index));
    }

    @Test
    public void testParameterValue() {
        final UrlPathNameHttpRequestAttribute name = UrlPathNameHttpRequestAttribute.with(2);

        assertEquals(Optional.of(UrlPathName.with("path2")),
                name.parameterValue(new FakeHttpRequest() {
                    @Override
                    public RelativeUrl url() {
                        return Url.parseRelative("/path1/path2/path3");
                    }
                }));
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(UrlPathNameHttpRequestAttribute.with(1));
    }

    @Test
    public void testSameUncached() {
        final int index = UrlPathNameHttpRequestAttribute.CONSTANT_COUNT + 1;
        this.checkEquals(
                UrlPathNameHttpRequestAttribute.with(index),
                UrlPathNameHttpRequestAttribute.with(index));
    }

    @Test
    public void testToString() {
        assertEquals("path-0", UrlPathNameHttpRequestAttribute.with(0).toString());
    }

    @Override
    protected Class<UrlPathNameHttpRequestAttribute> type() {
        return UrlPathNameHttpRequestAttribute.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public UrlPathNameHttpRequestAttribute createObject() {
        return UrlPathNameHttpRequestAttribute.with(0);
    }
}
