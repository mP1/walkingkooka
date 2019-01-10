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

import org.junit.Test;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class RouterHttpRequestParametersMapEntryEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<RouterHttpRequestParametersMapEntry> {

    @Test
    public void testDifferentKey() {
        this.checkNotEquals(RouterHttpRequestParametersMapEntry.with(HttpRequestAttributes.TRANSPORT, value()));
    }

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(RouterHttpRequestParametersMapEntry.with(this.key(), "different"));
    }

    @Override
    protected RouterHttpRequestParametersMapEntry createObject() {
        return RouterHttpRequestParametersMapEntry.with(key(), value());
    }

    private HttpRequestAttribute key() {
        return HttpRequestAttributes.METHOD;
    }

    private Object value() {
        return HttpMethod.GET;
    }
}
