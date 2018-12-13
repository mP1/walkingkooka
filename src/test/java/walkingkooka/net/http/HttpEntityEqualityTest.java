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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Map;

public final class HttpEntityEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<HttpEntity> {

    private final static Map<HttpHeaderName<?>, Object> HEADERS = Maps.one(HttpHeaderName.CONTENT_LENGTH, 123L);
    private final static byte[] BODY = new byte[123];

    @Test
    public void testDifferentHeaders() {
        this.checkNotEquals(HttpEntity.with(Maps.one(HttpHeaderName.CONTENT_LENGTH, 456L), BODY));
    }

    @Test
    public void testDifferentBody() {
        this.checkNotEquals(HttpEntity.with(HEADERS, new byte[456]));
    }

    @Override
    protected HttpEntity createObject() {
        return HttpEntity.with(HEADERS, BODY);
    }
}
