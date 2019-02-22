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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TestRecordingHttpResponseTest implements ClassTesting2<TestRecordingHttpResponse>,
        HttpResponseTesting<TestRecordingHttpResponse> {

    @Test
    public void testCheck() {
        final TestRecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        response.setStatus(status);
        response.addEntity(entity);
        response.check(HttpRequests.fake(), status, entity);
    }

    @Test
    public void testCheckMultipleEntities() {
        final TestRecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        final HttpEntity entity2 = HttpEntity.with(Maps.one(HttpHeaderName.SERVER, "part 2"), new byte[123]);
        response.setStatus(status);
        response.addEntity(entity);
        response.addEntity(entity2);

        response.check(HttpRequests.fake(), status, entity, entity2);
    }

    @Test
    public void testCheckIncorrectStatusFails() {
        final TestRecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        response.setStatus(status);
        response.addEntity(entity);

        assertThrows(AssertionError.class, () -> {
            response.check(HttpRequests.fake(),
                    HttpStatusCode.OK.status(),
                    entity);
        });
    }

    @Test
    public void testCheckDifferentEntityFails() {
        final TestRecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = HttpEntity.with(Maps.one(HttpHeaderName.SERVER, "Server 123"), new byte[123]);
        response.setStatus(status);
        response.addEntity(entity);

        assertThrows(AssertionError.class, () -> {
            response.check(HttpRequests.fake(),
                    status,
                    HttpEntity.with(Maps.one(HttpHeaderName.SERVER, "Server 456"), new byte[456]));
        });
    }

    @Test
    public void testToString() {
        final TestRecordingHttpResponse response = this.createResponse();
        response.setStatus(this.status());
        response.addEntity(this.entity());

        this.toStringAndCheck(response.toString().replace("\r\n", "\n").replace('\r', '\n'),
                "503 Problem x y z\n" +
                        "Server: Server 123\n" +
                        "\n" +
                        "00000000 41 42 43                                        ABC             \n");
    }

    @Override public TestRecordingHttpResponse createResponse() {
        return TestRecordingHttpResponse.with();
    }

    private HttpStatus status() {
        return HttpStatusCode.SERVICE_UNAVAILABLE.setMessage("Problem x y z");
    }

    private HttpEntity entity() {
        return HttpEntity.with(Maps.one(HttpHeaderName.SERVER, "Server 123"), new byte[]{65, 66, 67});
    }

    @Override
    public Class<TestRecordingHttpResponse> type() {
        return TestRecordingHttpResponse.class;
    }

    @Override public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
