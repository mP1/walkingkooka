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
import walkingkooka.collect.list.Lists;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.Latch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class WrapperHttpResponseTestCase<R extends WrapperHttpResponse> extends HttpResponseTestCase2<R> {

    WrapperHttpResponseTestCase() {
        super();
    }

    @Test
    public final void testWithNullResponseFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createResponse(null);
        });
    }

    @Test
    public void testSetStatus() {
        final Latch set = Latch.create();
        final HttpStatus status = HttpStatusCode.OK.status();
        this.createResponse(new FakeHttpResponse() {
            @Test
            public void setStatus(final HttpStatus s) {
                assertSame(status, s);
                set.set("Status set to " + s);
            }
        }).setStatus(status);
        assertTrue(set.value(), "wrapped response setStatus not called");
    }

    @Test
    public final void testToString() {
        final String toString = "Wrapped Http Response";
        this.toStringAndCheck(this.createResponse(new FakeHttpResponse() {

                    @Override
                    public String toString() {
                        return toString;
                    }
                }),
                toString);
    }

    // helpers..................................................................................................

    @Override
    public final R createResponse() {
        return this.createResponse(this.wrappedHttpResponse());
    }

    HttpResponse wrappedHttpResponse() {
        return HttpResponses.fake();
    }

    R createResponse(final HttpResponse response) {
        return this.createResponse(this.createRequest(), response);
    }

    abstract HttpRequest createRequest();

    abstract R createResponse(final HttpRequest request, final HttpResponse response);

    final void setStatusAddEntityAndCheck(final HttpStatus status,
                                          final HttpEntity entity) {
        this.setStatusAddEntityAndCheck(status,
                entity,
                status,
                entity);
    }

    final void setStatusAddEntityAndCheck(final HttpStatus status,
                                          final HttpEntity... entities) {
        this.setStatusAddEntityAndCheck(status,
                Lists.of(entities),
                status,
                entities);
    }

    final void setStatusAddEntityAndCheck(final HttpStatus status,
                                          final HttpEntity entity,
                                          final HttpStatus expectedStatus,
                                          final HttpEntity... expectedEntities) {
        this.setStatusAddEntityAndCheck(status,
                Lists.of(entity),
                expectedStatus,
                expectedEntities);
    }

    final void setStatusAddEntityAndCheck(final HttpStatus status,
                                          final List<HttpEntity> entities,
                                          final HttpStatus expectedStatus,
                                          final HttpEntity... expectedEntities) {
        this.setStatusAddEntityAndCheck(this.createRequest(),
                status,
                entities,
                expectedStatus,
                expectedEntities);
    }

    final void setStatusAddEntityAndCheck(final HttpRequest request,
                                          final HttpStatus status,
                                          final HttpEntity... entities) {
        this.setStatusAddEntityAndCheck(request,
                status,
                Lists.of(entities),
                status,
                entities);
    }

    final void setStatusAddEntityAndCheck(final HttpRequest request,
                                          final HttpStatus status,
                                          final HttpEntity entity,
                                          final HttpStatus expectedStatus,
                                          final HttpEntity... expectedEntities) {
        this.setStatusAddEntityAndCheck(request,
                status,
                Lists.of(entity),
                expectedStatus,
                expectedEntities);
    }

    final void setStatusAddEntityAndCheck(final HttpRequest request,
                                          final HttpStatus status,
                                          final List<HttpEntity> entities,
                                          final HttpStatus expectedStatus,
                                          final HttpEntity... expectedEntities) {
        final RecordingHttpResponse wrapped = RecordingHttpResponse.with();

        final R response = this.createResponse(request, wrapped);
        response.setStatus(status);
        entities.forEach(response::addEntity);

        this.checkResponse(wrapped, request, expectedStatus, expectedEntities);
    }
}
