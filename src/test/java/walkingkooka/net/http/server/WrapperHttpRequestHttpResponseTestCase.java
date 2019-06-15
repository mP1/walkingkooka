/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.http.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class WrapperHttpRequestHttpResponseTestCase<R extends WrapperHttpRequestHttpResponse>
        extends WrapperHttpResponseTestCase<R> {

    WrapperHttpRequestHttpResponseTestCase() {
        super();
    }

    @Test
    public final void testWithNullRequestFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createResponse(null, HttpResponses.fake());
        });
    }

    // helpers..................................................................................................

    final R createResponse(final HttpResponse response) {
        return this.createResponse(this.createRequest(), response);
    }

    abstract R createResponse(final HttpRequest request,
                              final HttpResponse response);

    abstract HttpRequest createRequest();
}
