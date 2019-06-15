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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import walkingkooka.Binary;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlPath;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class DirectoryHttpRequestHttpResponseBiConsumerTest implements ClassTesting2<DirectoryHttpRequestHttpResponseBiConsumer>,
        ToStringTesting<DirectoryHttpRequestHttpResponseBiConsumer> {

    private final static String FILE1 = "file1.txt";
    private final static String FILE2 = "dir/file2";

    private final static byte[] CONTENT1 = "a1b2".getBytes(Charset.defaultCharset());
    private final static byte[] CONTENT2 = "c3d4e5".getBytes(Charset.defaultCharset());

    private final Function<FileResponse, MediaType> CONTENT_TYPE_IDENTIFIER = (f) -> {
        return f.filename().endsWith("txt") ?
                MediaType.TEXT_PLAIN :
                MediaType.BINARY;
    };

    @TempDir
    static Path FILE_BASE_DIR;

    @BeforeAll
    public static void beforeEach() throws IOException {
        Files.write(Paths.get(FILE_BASE_DIR.toString(), FILE1), CONTENT1);

        final Path file2 = Paths.get(FILE_BASE_DIR.toString(), FILE2);
        file2.getParent().toFile().mkdirs();
        Files.write(file2, CONTENT2);
    }

    @Test
    public void testWithUrlNullUrlPathBaseFails() {
        assertThrows(NullPointerException.class, () -> {
                    DirectoryHttpRequestHttpResponseBiConsumer.with(null, FILE_BASE_DIR, CONTENT_TYPE_IDENTIFIER);
                }
        );
    }

    @Test
    public void testWithNullFilePathBaseFails2() {
        assertThrows(NullPointerException.class, () -> {
                    DirectoryHttpRequestHttpResponseBiConsumer.with(this.urlPathBase(), null, CONTENT_TYPE_IDENTIFIER);
                }
        );
    }

    @Test
    public void testWithNullContentTypeIdentifierFails2() {
        assertThrows(NullPointerException.class, () -> {
                    DirectoryHttpRequestHttpResponseBiConsumer.with(this.urlPathBase(), null, CONTENT_TYPE_IDENTIFIER);
                }
        );
    }

    @Test
    public void testWithPathBaseNotExistingFails() {
        assertThrows(IllegalArgumentException.class, () -> {
                    DirectoryHttpRequestHttpResponseBiConsumer.with(this.urlPathBase(),
                            Paths.get(FILE_BASE_DIR.toString(), "doesnt-exist"),
                            CONTENT_TYPE_IDENTIFIER);
                }
        );
    }

    @Test
    public void testFileNotFound() {
        final HttpRequest request = this.request("/files/server/file-not-found", null);
        final RecordingHttpResponse response = HttpResponses.recording();

        this.createBiConsumer()
                .accept(request, response);

        final RecordingHttpResponse expected = HttpResponses.recording();
        expected.setStatus(HttpStatusCode.NOT_FOUND.status());

        assertEquals(expected, response, "request: " + request);
    }

    @Test
    public void testFileWithoutIfNotModified() throws IOException {
        final HttpRequest request = this.request("/files/server/" + FILE1, null);
        final RecordingHttpResponse response = HttpResponses.recording();

        this.createBiConsumer()
                .accept(request, response);

        final RecordingHttpResponse expected = HttpResponses.recording();
        expected.setStatus(HttpStatusCode.OK.status());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.LAST_MODIFIED, this.fileLastModified1());
        headers.put(HttpHeaderName.CONTENT_LENGTH, this.contentLength1());
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);

        expected.addEntity(HttpEntity.with(headers, this.content1()));

        assertEquals(expected, response, "request: " + request);
    }

    @Test
    public void testFile2WithoutIfNotModified() throws IOException {
        final HttpRequest request = this.request("/files/server/" + FILE2, null);
        final RecordingHttpResponse response = HttpResponses.recording();

        this.createBiConsumer()
                .accept(request, response);

        final RecordingHttpResponse expected = HttpResponses.recording();
        expected.setStatus(HttpStatusCode.OK.status());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.LAST_MODIFIED, this.fileLastModified2());
        headers.put(HttpHeaderName.CONTENT_LENGTH, this.contentLength2());
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.BINARY);

        expected.addEntity(HttpEntity.with(headers, this.content2()));

        assertEquals(expected, response, "request: " + request);
    }

    @Test
    public void testFileIfNotModifiedInvalid() throws IOException {
        final HttpRequest request = this.request("/files/server/" + FILE1, this.lastModified(this.file1()).minusSeconds(10));
        final RecordingHttpResponse response = HttpResponses.recording();

        this.createBiConsumer()
                .accept(request, response);

        final RecordingHttpResponse expected = HttpResponses.recording();
        expected.setStatus(HttpStatusCode.OK.status());

        final Map<HttpHeaderName<?>, Object> headers = Maps.ordered();
        headers.put(HttpHeaderName.LAST_MODIFIED, this.fileLastModified1());
        headers.put(HttpHeaderName.CONTENT_LENGTH, this.contentLength1());
        headers.put(HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN);

        expected.addEntity(HttpEntity.with(headers, this.content1()));

        assertEquals(expected, response, "request: " + request);
    }

    @Test
    public void testFileIfNotModified() throws IOException {
        final HttpRequest request = this.request("/files/server/" + FILE1, this.fileLastModified1());
        final RecordingHttpResponse response = HttpResponses.recording();
        this.createBiConsumer()
                .accept(request, response);

        final RecordingHttpResponse expected = HttpResponses.recording();
        expected.setStatus(HttpStatusCode.NOT_MODIFIED.status());

        expected.addEntity(HttpEntity.with(Maps.of(HttpHeaderName.LAST_MODIFIED, this.fileLastModified1(),
                HttpHeaderName.CONTENT_TYPE, MediaType.TEXT_PLAIN),
                HttpEntity.NO_BODY));

        assertEquals(expected, response, "request: " + request);
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createBiConsumer(), FILE_BASE_DIR.toString());
    }

    private DirectoryHttpRequestHttpResponseBiConsumer createBiConsumer() {
        return DirectoryHttpRequestHttpResponseBiConsumer.with(this.urlPathBase(), FILE_BASE_DIR, CONTENT_TYPE_IDENTIFIER);
    }

    private UrlPath urlPathBase() {
        return UrlPath.parse("/files/server/");
    }

    private LocalDateTime fileLastModified1() throws IOException {
        return this.lastModified(this.file1());
    }

    private LocalDateTime fileLastModified2() throws IOException {
        return this.lastModified(this.file2());
    }

    private File file1() {
        return this.file(FILE1);
    }

    private File file2() {
        return this.file(FILE2);
    }

    private File file(final String path) {
        return Paths.get(FILE_BASE_DIR.toString(), path).toFile();
    }

    private LocalDateTime lastModified(final File file) throws IOException {
        return LocalDateTime.ofInstant(Files.getLastModifiedTime(file.toPath()).toInstant(), ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
    }

    private Binary content1() {
        return Binary.with(CONTENT1);
    }

    private Binary content2() {
        return Binary.with(CONTENT2);
    }

    private long contentLength1() {
        return this.content1().size();
    }

    private long contentLength2() {
        return this.content2().size();
    }

    private HttpRequest request(final String url, final LocalDateTime ifNotModified) {
        return new FakeHttpRequest() {

            @Override
            public RelativeUrl url() {
                return Url.parseRelative(url);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                return null != ifNotModified ?
                        Maps.of(HttpHeaderName.IF_MODIFIED_SINCE, ifNotModified) :
                        Maps.empty();
            }

            @Override
            public String toString() {
                return this.url() + " " + this.headers();
            }
        };
    }

    @Override
    public Class<DirectoryHttpRequestHttpResponseBiConsumer> type() {
        return DirectoryHttpRequestHttpResponseBiConsumer.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}