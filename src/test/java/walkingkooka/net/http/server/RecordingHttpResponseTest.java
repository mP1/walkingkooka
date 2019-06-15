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
import walkingkooka.Binary;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RecordingHttpResponseTest implements ClassTesting2<RecordingHttpResponse>,
        HttpResponseTesting<RecordingHttpResponse> {

    @Test
    public void testBuild() {
        final RecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        response.setStatus(status);
        response.addEntity(entity);

        assertEquals(Optional.ofNullable(status), response.status(), "status");
        assertEquals(Lists.of(entity), response.entities(), "entities");
    }

    @Test
    public void testCheck() {
        final RecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        response.setStatus(status);
        response.addEntity(entity);
        this.checkResponse(response, HttpRequests.fake(), status, entity);
    }

    @Test
    public void testCheckMultipleEntities() {
        final RecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        final HttpEntity entity2 = HttpEntity.with(Maps.of(HttpHeaderName.SERVER, "part 2"), Binary.with(new byte[123]));
        response.setStatus(status);
        response.addEntity(entity);
        response.addEntity(entity2);

        this.checkResponse(response, HttpRequests.fake(), status, entity, entity2);
    }

    @Test
    public void testCheckIncorrectStatusFails() {
        final RecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = this.entity();
        response.setStatus(status);
        response.addEntity(entity);

        assertThrows(AssertionError.class, () -> {
            this.checkResponse(response, HttpRequests.fake(),
                    HttpStatusCode.OK.status(),
                    entity);
        });
    }

    @Test
    public void testCheckDifferentEntityFails() {
        final RecordingHttpResponse response = this.createResponse();
        final HttpStatus status = this.status();
        final HttpEntity entity = HttpEntity.with(Maps.of(HttpHeaderName.SERVER, "Server 123"), Binary.with(new byte[123]));
        response.setStatus(status);
        response.addEntity(entity);

        assertThrows(AssertionError.class, () -> {
            this.checkResponse(response, HttpRequests.fake(),
                    status,
                    HttpEntity.with(Maps.of(HttpHeaderName.SERVER, "Server 456"), Binary.with(new byte[456])));
        });
    }

    @Test
    public void testToString() {
        final RecordingHttpResponse response = this.createResponse();
        response.setStatus(this.status());
        response.addEntity(this.entity());

        this.toStringAndCheck(response.toString().replace("\r\n", "\n").replace('\r', '\n'),
                "503 Problem x y z\n" +
                        "Server: Server 123\n" +
                        "\n" +
                        "00000000 41 42 43                                        ABC             \n");
    }

    @Override
    public RecordingHttpResponse createResponse() {
        return RecordingHttpResponse.with();
    }

    private HttpStatus status() {
        return HttpStatusCode.SERVICE_UNAVAILABLE.setMessage("Problem x y z");
    }

    private HttpEntity entity() {
        return HttpEntity.with(Maps.of(HttpHeaderName.SERVER, "Server 123"), Binary.with(new byte[]{65, 66, 67}));
    }

    @Override
    public Class<RecordingHttpResponse> type() {
        return RecordingHttpResponse.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
