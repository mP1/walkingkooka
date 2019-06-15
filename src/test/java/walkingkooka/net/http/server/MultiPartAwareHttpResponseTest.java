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
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MultiPartAwareHttpResponseTest extends BufferingHttpResponseTestCase<MultiPartAwareHttpResponse> {

    // test.........................................................................................

    @Test
    public void testWithRequestNullFails() {
        assertThrows(NullPointerException.class, () -> {
            MultiPartAwareHttpResponse.with(null);
        });
    }

    @Test
    public void testNonMultiPart() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN, "part"));
    }

    @Test
    public void testNonMultiPartIgnoresAdditionalParts() {
        final HttpStatus status = HttpStatusCode.OK.status();
        final HttpEntity part1 = this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN, "part-1a");
        final HttpEntity part2 = this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN, "part-2b");

        this.setStatusAddEntityAndCheck(status,
                Lists.of(part1, part2),
                status,
                part1);
    }

    @Test
    public void testNonMultiPartIgnoresAdditionalPartsWithoutContentType() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entity(HttpHeaderName.CONTENT_LENGTH, 6L, "abc123"));
    }

    @Test
    public void testMultipartOnePart() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA, "part1"));
    }

    @Test
    public void testMultipartSeveralPart2() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA, "part1"),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA, "part2"));
    }

    @Test
    public void testMultipartSeveralPart3() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA, "part1"),
                this.entity(HttpHeaderName.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA, "part2"));
    }

    private <TT> HttpEntity entity(final HttpHeaderName<TT> header, final TT value, final String content) {
        return HttpEntity.with(Maps.of(header, value), Binary.with(content.getBytes(Charset.defaultCharset())));
    }

    @Override
    public Class<MultiPartAwareHttpResponse> type() {
        return MultiPartAwareHttpResponse.class;
    }

    @Override
    HttpRequest createRequest() {
        return HttpRequests.fake();
    }

    @Override
    MultiPartAwareHttpResponse createResponse(final HttpRequest request, final HttpResponse response) {
        return MultiPartAwareHttpResponse.with(response);
    }
}
