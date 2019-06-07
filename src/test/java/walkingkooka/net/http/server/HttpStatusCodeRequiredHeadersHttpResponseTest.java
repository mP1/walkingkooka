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
import walkingkooka.Binary;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.Url;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.RangeHeaderValue;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpStatusCodeCategory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HttpStatusCodeRequiredHeadersHttpResponseTest extends BufferingHttpResponseTestCase<HttpStatusCodeRequiredHeadersHttpResponse> {

    // test.........................................................................................

    @Test
    public void testWithRequestNullFails() {
        assertThrows(NullPointerException.class, () -> {
            LastModifiedAwareHttpResponse.with(null,
                    HttpResponses.fake());
        });
    }

    @Test
    public void testWithHttpStatusCodeRequiredHeadersHttpResponse() {
        final HttpStatusCodeRequiredHeadersHttpResponse response = this.createResponse();
        assertSame(response, HttpStatusCodeRequiredHeadersHttpResponse.with(response));
    }

    @Test
    public void testSuccessMultiPart() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.OK.status(),
                HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.parse("multipart/text")), this.binary("part-1a")),
                HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.parse("multipart/text")), this.binary("part-2b")));
    }

    @Test
    public void testPartialContentWithRanges() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.PARTIAL_CONTENT.status(),
                HttpEntity.with(Maps.of(HttpHeaderName.RANGE, RangeHeaderValue.parse("bytes=1-2")), this.binary("a1")));
    }

    @Test
    public void testPartialContentMissingRangesFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.PARTIAL_CONTENT, Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN));
    }

    @Test
    public void testMultipleChoicesMissingLocation() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.MULTIPLE_CHOICES.status(),
                this.httpEntityWithServerHeader());
    }

    @Test
    public void testMultipleChoices() {
        this.setStatusAddEntityAndCheck(HttpStatusCode.MULTIPLE_CHOICES.status(),
                this.httpEntityWithServerHeader());
    }

    @Test
    public void testMovedPermanentlyMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.MOVED_PERMANENTLY);
    }

    @Test
    public void testMovedPermanentlyWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.MOVED_PERMANENTLY);
    }

    @Test
    public void testMovedTemporarilyMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.MOVED_TEMPORARILY);
    }

    @Test
    public void testMovedTemporarilyWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.MOVED_TEMPORARILY);
    }

    @Test
    public void testFoundMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.FOUND);
    }

    @Test
    public void testFoundWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.FOUND);
    }

    @Test
    public void testSeeOtherMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.SEE_OTHER);
    }

    @Test
    public void testSeeOtherWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.SEE_OTHER);
    }

    @Test
    public void testNotModifiedMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.NOT_MODIFIED);
    }

    @Test
    public void testNotModifiedWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.NOT_MODIFIED);
    }

    @Test
    public void testUseProxyMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.USE_PROXY);
    }

    @Test
    public void testUseProxyWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.USE_PROXY);
    }

    @Test
    public void testTemporaryRedirectMissingLocationFails() {
        this.missingRequiredHeaderFails(HttpStatusCode.TEMPORARY_REDIRECT);
    }

    @Test
    public void testTemporaryRedirectWithLocation() {
        this.setStatusWithoutLocationHeader(HttpStatusCode.TEMPORARY_REDIRECT);
    }

    @Test
    public void testInformation() {
        this.setStatusWithServerHeader(HttpStatusCodeCategory.INFORMATION);
    }

    @Test
    public void testSuccessExceptPartialContent() {
        Arrays.stream(HttpStatusCode.values())
                .filter(c -> c.category() == HttpStatusCodeCategory.CLIENT_ERROR)
                .filter(c -> c != HttpStatusCode.PARTIAL_CONTENT)
                .forEach(this::setStatusWithServerHeader);
    }

    @Test
    public void testClientError() {
        this.setStatusWithServerHeader(HttpStatusCodeCategory.CLIENT_ERROR);
    }

    @Test
    public void testServerError() {
        this.setStatusWithServerHeader(HttpStatusCodeCategory.SERVER_ERROR);
    }

    private void setStatusWithServerHeader(final HttpStatusCodeCategory codeCategory) {
        Arrays.stream(HttpStatusCode.values())
                .filter(c -> c.category() == codeCategory)
                .forEach(this::setStatusWithServerHeader);
    }

    private void setStatusWithServerHeader(final HttpStatusCode code) {
        this.setStatusAddEntityAndCheck(code.status(), this.httpEntityWithServerHeader());
    }

    private void setStatusWithoutLocationHeader(final HttpStatusCode code) {
        this.setStatusAddEntityAndCheck(code.status(),
                HttpEntity.with(Maps.of(HttpHeaderName.LOCATION, Url.parse("http://example.com")), this.binary("a1")));
    }

    private HttpEntity httpEntityWithServerHeader() {
        return HttpEntity.with(Maps.of(HttpHeaderName.SERVER, "Server123"), this.binary("a1"));
    }

    private Binary binary(final String content) {
        return Binary.with(content.getBytes(Charset.defaultCharset()));
    }

    private void missingRequiredHeaderFails(final HttpStatusCode code) {
        this.missingRequiredHeaderFails(code, this.httpEntityWithServerHeader().headers());
    }

    private void missingRequiredHeaderFails(final HttpStatusCode code,
                                            final Map<HttpHeaderName<?>, Object> headers) {
        final HttpStatus status = code.status();
        final HttpEntity entity = HttpEntity.with(headers, Binary.with(new byte[]{'a', 'b', 'c', '1', '2', '3'}));

        this.setStatusAddEntityAndCheck(status,
                Lists.of(entity, HttpEntity.with(Maps.of(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN), Binary.EMPTY)),
                HttpStatusCode.INTERNAL_SERVER_ERROR.status(),
                HttpEntity.with(HttpEntity.NO_HEADERS, Binary.EMPTY));
    }

    @Override
    HttpStatusCodeRequiredHeadersHttpResponse createResponse(final HttpRequest request,
                                                             final HttpResponse response) {
        return HttpStatusCodeRequiredHeadersHttpResponse.with(response);
    }

    @Override
    HttpRequest createRequest() {
        return HttpRequests.fake();
    }

    @Override
    public Class<HttpStatusCodeRequiredHeadersHttpResponse> type() {
        return HttpStatusCodeRequiredHeadersHttpResponse.class;
    }
}
