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
import walkingkooka.test.PackagePrivateClassTestCase;

public abstract class HttpResponseTestCase<R extends HttpResponse> extends PackagePrivateClassTestCase<R> {

    @Test(expected = NullPointerException.class)
    public void testSetStatusNullFails() {
        this.createResponse().setStatus(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddHeaderNullNameFails() {
        this.createResponse().addHeader(null, "header-value");
    }

    @Test(expected = NullPointerException.class)
    public void testAddHeaderNullValueFails() {
        this.createResponse().addHeader(HttpHeaderName.CONTENT_LENGTH, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetBodyNullFails() {
        this.createResponse().setBody(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetBodyTextNullFails() {
        this.createResponse().setBodyText(null);
    }

    protected abstract R createResponse();
}
