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

package walkingkooka.net.http.server.hateos;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Range;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.UrlParameterName;
import walkingkooka.net.header.AcceptCharset;
import walkingkooka.net.header.CharsetName;
import walkingkooka.net.header.HttpHeaderName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.header.MediaTypeParameterName;
import walkingkooka.net.http.HttpEntity;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.HttpProtocolVersion;
import walkingkooka.net.http.HttpStatus;
import walkingkooka.net.http.HttpStatusCode;
import walkingkooka.net.http.HttpTransport;
import walkingkooka.net.http.server.HttpRequest;
import walkingkooka.net.http.server.HttpRequestAttribute;
import walkingkooka.net.http.server.HttpRequestParameterName;
import walkingkooka.net.http.server.HttpResponse;
import walkingkooka.net.http.server.HttpResponses;
import walkingkooka.net.http.server.TestRecordingHttpResponse;
import walkingkooka.routing.RouterTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public final class HateosHandlerRouterTest implements ClassTesting2<HateosHandlerRouter<JsonNode>>,
        RouterTesting<HateosHandlerRouter<JsonNode>,
                HttpRequestAttribute<?>,
                BiConsumer<HttpRequest, HttpResponse>> {

    private final static String NO_JSON = null;

    final static BigInteger ID = BigInteger.valueOf(123);
    final static BigInteger NO_ID = null;
    final static Range<BigInteger> ALL = Range.all();
    final static Range<BigInteger> RANGE = Range.greaterThanEquals(BigInteger.valueOf(123)).and(Range.lessThanEquals(BigInteger.valueOf(456)));

    private final static HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> HATEOS_HANDLER = new FakeHateosHandler<>();
    private final static HateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2> HATEOS_COLLECTION_HANDLER = new FakeHateosCollectionHandler<>();
    private final static HateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2> HATEOS_MAPPING_HANDLER = new FakeHateosMappingHandler<>();

    // BAD REQUEST................................................................................................

    @Test
    public void testMethodNotSupported() {
        this.routeAndCheck(HttpMethod.HEAD,
                "/api/resource/*/self",
                HttpStatusCode.METHOD_NOT_ALLOWED.setMessage("HEAD"));
    }

    @Test
    public void testBadRequestMissingBase() {
        this.routeAndCheck("/missing/",
                null);
    }

    @Test
    public void testBadRequestMissingResourceName() {
        this.routeAndCheck("/api/",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestMissingResourceName2() {
        this.routeAndCheck("/api//",
                HttpStatusCode.BAD_REQUEST.setMessage("Missing resource name"));
    }

    @Test
    public void testBadRequestInvalidResourceName() {
        this.routeAndCheck("/api/999-invalid",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid resource name \"999-invalid\""));
    }

    @Test
    public void testNotFoundHandlerMissing() {
        this.routeAndCheck("/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissingWithRelation() {
        this.routeAndCheck("/api/unknown123/1/self",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testNotFoundHandlerMissing2() {
        this.routeAndCheck(this.builder().add(this.resourceName2(), this.relation1(), this.mapper()),
                "/api/unknown123",
                HttpStatusCode.NOT_FOUND.setMessage("resource: unknown123, link relation: self"));
    }

    @Test
    public void testBadRequestInvalidRelation() {
        assertThrows(IllegalArgumentException.class, () -> {
            LinkRelation.with("!!");
        });
        this.routeGetAndCheck("/api/resource1/1/!!",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid link relation \"!!\""));
    }

    @Test
    public void testBadRequestInvalidResourceId() {
        this.routeGetAndCheck("/api/resource1/@/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid id \"@\""));
    }

    @Test
    public void testBadRequestMissingRangeBeginResourceId() {
        this.routeGetAndCheck("/api/resource1/-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"-999\""));
    }

    @Test
    public void testBadRequestInvalidRangeBeginResourceId() {
        this.routeGetAndCheck("/api/resource1/!-999/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range begin \"!-999\""));
    }

    @Test
    public void testBadRequestMissingRangeEndResourceId() {
        this.routeGetAndCheck("/api/resource1/999-/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-\""));
    }

    @Test
    public void testBadRequestInvalidRangeEndResourceId() {
        this.routeGetAndCheck("/api/resource1/999-!/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid range end \"999-!\""));
    }

    @Test
    public void testBadRequestInvalidRange() {
        this.routeGetAndCheck("/api/resource1/1-2-3/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1-2-3\""));
    }

    @Test
    public void testBadRequestInvalidRange2() {
        this.routeGetAndCheck("/api/resource1/1\\-2-3-4/self",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid character within range \"1\\\\-2-3-4\""));
    }

    private void routeGetAndCheck(final String url,
                                  final HttpStatus status,
                                  final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(HATEOS_HANDLER)),
                url,
                status,
                entities);
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(HATEOS_COLLECTION_HANDLER)),
                url,
                status,
                entities);
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(HATEOS_MAPPING_HANDLER)),
                url,
                status,
                entities);
    }

    // HateosHandler..................................................................................................

    @Test
    public void testHateosHandlerBadRequestIdAndInvalidJson() {
        this.routeHateosHandlerPostAndCheck("/api/resource1/123/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosHandlerBadRequestMissingIdAndInvalidJson() {
        this.routeHateosHandlerPostAndCheck("/api/resource1//self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosHandlerBadRequestWildcardAndInvalidJson() {
        this.routeHateosHandlerPostAndCheck("/api/resource1/*/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosHandlerBadRequestRangeAndInvalidJson() {
        this.routeHateosHandlerPostAndCheck("/api/resource1/1-2/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    private void routeHateosHandlerPostAndCheck(final String url,
                                                final String requestBody,
                                                final HttpStatus status,
                                                final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().post(HATEOS_HANDLER)),
                HttpMethod.POST,
                url,
                this.contentType(),
                requestBody,
                status,
                entities);
    }

    // HateosCollectionHandler..................................................................................................

    @Test
    public void testHateosCollectionHandlerBadRequestIdAndInvalidJson() {
        this.routeHateosCollectionHandlerPostAndCheck("/api/resource1/123/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosCollectionHandlerBadRequestMissingIdAndInvalidJson() {
        this.routeHateosCollectionHandlerPostAndCheck("/api/resource1//self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage(HateosHandlerMapperHateosCollectionHandlerMapping.COLLECTION_NOT_SUPPORTED_MESSAGE));
    }

    @Test
    public void testHateosCollectionHandlerBadRequestWildcardAndInvalidJson() {
        this.routeHateosCollectionHandlerPostAndCheck("/api/resource1/*/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage(HateosHandlerMapperHateosCollectionHandlerMapping.COLLECTION_NOT_SUPPORTED_MESSAGE));
    }

    @Test
    public void testHateosCollectionHandlerBadRequestRangeAndInvalidJson() {
        this.routeHateosCollectionHandlerPostAndCheck("/api/resource1/1-2/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage(HateosHandlerMapperHateosCollectionHandlerMapping.COLLECTION_NOT_SUPPORTED_MESSAGE));
    }

    private void routeHateosCollectionHandlerPostAndCheck(final String url,
                                                          final String requestBody,
                                                          final HttpStatus status,
                                                          final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().post(HATEOS_COLLECTION_HANDLER)),
                HttpMethod.POST,
                url,
                this.contentType(),
                requestBody,
                status,
                entities);
    }

    // HateosMappingHandler..................................................................................................

    @Test
    public void testHateosMappingHandlerBadRequestIdAndInvalidJson() {
        this.routeHateosMappingHandlerPostAndCheck("/api/resource1/123/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosMappingHandlerBadRequestMissingIdAndInvalidJson() {
        this.routeHateosMappingHandlerPostAndCheck("/api/resource1//self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosMappingHandlerBadRequestWildcardAndInvalidJson() {
        this.routeHateosMappingHandlerPostAndCheck("/api/resource1/*/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    @Test
    public void testHateosMappingHandlerBadRequestRangeAndInvalidJson() {
        this.routeHateosMappingHandlerPostAndCheck("/api/resource1/1-2/self",
                "!invalid json",
                HttpStatusCode.BAD_REQUEST.setMessage("Invalid JSON: Unrecognized character '!' at (1,1) \"!invalid json\" expected NULL | BOOLEAN | STRING | NUMBER | ARRAY | OBJECT"));
    }

    private void routeHateosMappingHandlerPostAndCheck(final String url,
                                                       final String requestBody,
                                                       final HttpStatus status,
                                                       final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().post(HATEOS_MAPPING_HANDLER)),
                HttpMethod.POST,
                url,
                this.contentType(),
                requestBody,
                status,
                entities);
    }

    private void routeAndCheck(final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(HttpMethod.GET,
                url,
                status,
                entities);
    }

    private void routeAndCheck(final HttpMethod method,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(this.builder(),
                method,
                url,
                status,
                entities);
    }

    // HateosHandler.handle...................................................................................

    @Test
    public void testHateosHandlerNotImplementedResource() {
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public Optional<TestHateosResource2> handle(final BigInteger id,
                                                                                        final Optional<TestHateosResource> resource,
                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new UnsupportedOperationException();
                                            }
                                        },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosHandlerNotImplementedResource2() {
        final String message = "abc123";
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public Optional<TestHateosResource2> handle(final BigInteger id,
                                                                                        final Optional<TestHateosResource> resource,
                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new UnsupportedOperationException(message);
                                            }
                                        },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosHandlerInternalServerErrorResource() {
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public Optional<TestHateosResource2> handle(final BigInteger id,
                                                                                        final Optional<TestHateosResource> resource,
                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new IllegalArgumentException();
                                            }
                                        },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosHandlerInternalServerErrorResource2() {
        final String message = "abc123";
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public Optional<TestHateosResource2> handle(final BigInteger id,
                                                                                        final Optional<TestHateosResource> resource,
                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new IllegalArgumentException(message);
                                            }
                                        },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }


    @Test
    public void testHateosHandlerNotImplementedResourceCollection() {
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new UnsupportedOperationException();
                                            }
                                        },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosHandlerNotImplementedResourceCollection2() {
        final String message = "abc123";
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new UnsupportedOperationException(message);
                                            }
                                        },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosHandlerInternalServerErrorResourceCollection() {
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new IllegalArgumentException();
                                            }
                                        },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosHandlerInternalServerErrorResourceCollection2() {
        final String message = "abc123";
        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                throw new IllegalArgumentException(message);
                                            }
                                        },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }

    private void routeHateosHandlerAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                            final String url,
                                            final HttpStatus status,
                                            final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(handler)),
                HttpMethod.GET,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    // HateosCollectionHandler.handle...................................................................................

    @Test
    public void testHateosCollectionHandlerNotImplementedResource() {
        this.routeHateosCollectionHandlerAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handle(final BigInteger id,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          throw new UnsupportedOperationException();
                                                      }
                                                  },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosCollectionHandlerNotImplementedResource2() {
        final String message = "abc123";
        this.routeHateosCollectionHandlerAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handle(final BigInteger id,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          throw new UnsupportedOperationException(message);
                                                      }
                                                  },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosCollectionHandlerInternalServerErrorResource() {
        this.routeHateosCollectionHandlerAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handle(final BigInteger id,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          throw new IllegalArgumentException();
                                                      }
                                                  },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosCollectionHandlerInternalServerErrorResource2() {
        final String message = "abc123";
        this.routeHateosCollectionHandlerAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handle(final BigInteger id,
                                                                                              final List<TestHateosResource> resources,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          throw new IllegalArgumentException(message);
                                                      }
                                                  },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }

    private void routeHateosCollectionHandlerAndCheck(final HateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                      final String url,
                                                      final HttpStatus status,
                                                      final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(handler)),
                HttpMethod.GET,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    // HateosMappingHandler.handle...................................................................................

    @Test
    public void testHateosMappingHandlerNotImplementedResource() {
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public Optional<Object> handle(final BigInteger id,
                                                                                  final Optional<TestHateosResource> resource,
                                                                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new UnsupportedOperationException();
                                                   }
                                               },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosMappingHandlerNotImplementedResource2() {
        final String message = "abc123";
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public Optional<Object> handle(final BigInteger id,
                                                                                  final Optional<TestHateosResource> resource,
                                                                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new UnsupportedOperationException(message);
                                                   }
                                               },
                "/api/resource1/1/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosMappingHandlerInternalServerErrorResource() {
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public Optional<Object> handle(final BigInteger id,
                                                                                  final Optional<TestHateosResource> resource,
                                                                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new IllegalArgumentException();
                                                   }
                                               },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosMappingHandlerInternalServerErrorResource2() {
        final String message = "abc123";
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public Optional<Object> handle(final BigInteger id,
                                                                                  final Optional<TestHateosResource> resource,
                                                                                  final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new IllegalArgumentException(message);
                                                   }
                                               },
                "/api/resource1/1/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }


    @Test
    public void testHateosMappingHandlerNotImplementedResourceCollection() {
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                                     final List<TestHateosResource> resources,
                                                                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new UnsupportedOperationException();
                                                   }
                                               },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.status());
    }

    @Test
    public void testHateosMappingHandlerNotImplementedResourceCollection2() {
        final String message = "abc123";
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                                     final List<TestHateosResource> resources,
                                                                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new UnsupportedOperationException(message);
                                                   }
                                               },
                "/api/resource1/*/self",
                HttpStatusCode.NOT_IMPLEMENTED.setMessage(message));
    }

    @Test
    public void testHateosMappingHandlerInternalServerErrorResourceCollection() {
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                                     final List<TestHateosResource> resources,
                                                                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new IllegalArgumentException();
                                                   }
                                               },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.status());
    }

    @Test
    public void testHateosMappingHandlerInternalServerErrorResourceCollection2() {
        final String message = "abc123";
        this.routeHateosMappingHandlerAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                   @Override
                                                   public List<TestHateosResource2> handleCollection(final Range<BigInteger> ids,
                                                                                                     final List<TestHateosResource> resources,
                                                                                                     final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                       throw new IllegalArgumentException(message);
                                                   }
                                               },
                "/api/resource1/*/self",
                HttpStatusCode.BAD_REQUEST.setMessage(message));
    }

    private void routeHateosMappingHandlerAndCheck(final HateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                   final String url,
                                                   final HttpStatus status,
                                                   final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), this.mapper().get(handler)),
                HttpMethod.GET,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    // helpers.........................................................

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(builder,
                HttpMethod.GET,
                url,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final HttpMethod method,
                               final String url,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(builder,
                method,
                url,
                this.contentType(),
                NO_JSON,
                status,
                entities);
    }

    // HATEOSHANDLER GET RESOURCE ................................................................................................

    @Test
    public void testHateosHandlerIdWithoutRequestResource() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource2 responseResource = this.resource2(234);

        this.routeHateosHandlerGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                               @Override
                                               public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                                           final Optional<TestHateosResource> r,
                                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                   assertEquals(id, i);
                                                   assertEquals(Optional.empty(), r);
                                                   return Optional.of(responseResource);
                                               }
                                           },
                "/api/resource1/123/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndResponseResource() {
        this.hateosHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentType());
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndResponseResourceDifferentCharset() {
        this.hateosHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentTypeUtf16());
    }

    private void hateosHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(final MediaType contentType) {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final TestHateosResource2 responseResource = TestHateosResource2.with(BigInteger.valueOf(234));

        this.routeHateosHandlerGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                               @Override
                                               public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                                           final Optional<TestHateosResource> r,
                                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                   assertEquals(id, i);
                                                   assertEquals(Optional.of(requestResource), r);
                                                   return Optional.of(responseResource);
                                               }
                                           },
                "/api/resource1/123/self",
                contentType,
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}", contentType));
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndWithoutResponseResourceGet() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeHateosHandlerAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                            @Override
                                            public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                                        final Optional<TestHateosResource> r,
                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                assertEquals(id, i);
                                                assertEquals(Optional.of(requestResource), r);
                                                return Optional.empty();
                                            }
                                        },
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndWithoutResponseResourcePost() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().post(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                final Optional<TestHateosResource> r,
                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.POST,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("POST resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndWithoutResponseResourcePut() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().put(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                final Optional<TestHateosResource> r,
                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.PUT,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("PUT resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdWithRequestResourceAndWithoutResponseResourceDelete() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().delete(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                final Optional<TestHateosResource> r,
                                                                final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return Optional.empty();
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("DELETE resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdEscaped() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeHateosHandlerGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                               @Override
                                               public Optional<TestHateosResource2> handle(final BigInteger i,
                                                                                           final Optional<TestHateosResource> r,
                                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                   assertEquals(id, i);
                                                   assertEquals(Optional.of(requestResource), r);
                                                   return Optional.empty();
                                               }
                                           },
                "/api/resource1/\\1\\2\\3/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.NO_CONTENT.setMessage("GET resource successful"),
                this.httpEntity("", this.contentType()));
    }

    @Test
    public void testHateosHandlerIdDifferentTypeFromHateosResourceIdType() {
        final Long id = 123L;
        final TestHateosResource requestResource = TestHateosResource.with(BigInteger.valueOf(99));
        final TestHateosResource2 responseResource = TestHateosResource2.with(BigInteger.valueOf(id));

        final HateosHandlerBuilder<JsonNode> builder = this.builder()
                .add(this.resourceName1(), this.relation1(), HateosHandlerMapper.with(Long::parseLong, TestHateosResource.class, TestHateosResource2.class)
                        .get(new FakeHateosHandler<Long, TestHateosResource, TestHateosResource2>() {
                            @Override
                            public Optional<TestHateosResource2> handle(final Long i,
                                                                        final Optional<TestHateosResource> resource,
                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                assertEquals(id, i, "id");
                                return Optional.of(responseResource);
                            }
                        }));
        this.routeAndCheck(builder,
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("{\n" +
                                "  \"id\": \"123\",\n" +
                                "  \"_links\": [{\n" +
                                "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                                "    \"method\": \"GET\",\n" +
                                "    \"rel\": \"self\",\n" +
                                "    \"type\": \"application/hal+json\"\n" +
                                "  }]\n" +
                                "}",
                        this.contentType()));
    }

    private void routeHateosHandlerGetAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                               final String url,
                                               final MediaType contentType,
                                               final String body,
                                               final HttpStatus status,
                                               final HttpEntity... entities) {
        this.routeHateosHandlerAndCheck(handler,
                HttpMethod.GET,
                url,
                contentType,
                body,
                status,
                entities);
    }

    // HateosCollectionHandler GET RESOURCE ................................................................................................

    @Test
    public void testHateosCollectionHandlerIdWithoutRequestResource() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource2 responseResource = this.resource2(234);

        this.routeHateosCollectionHandlerGetAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                         @Override
                                                         public List<TestHateosResource2> handle(final BigInteger i,
                                                                                                 final List<TestHateosResource> r,
                                                                                                 final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                             assertEquals(id, i);
                                                             assertEquals(Lists.empty(), r);
                                                             return Lists.of(responseResource);
                                                         }
                                                     },
                "/api/resource1/123/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resources successful"),
                this.httpEntity("[{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndResponseResource() {
        this.HateosCollectionHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentType());
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndResponseResourceDifferentCharset() {
        this.HateosCollectionHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentTypeUtf16());
    }

    private void HateosCollectionHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(final MediaType contentType) {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final TestHateosResource2 responseResource = TestHateosResource2.with(BigInteger.valueOf(234));

        this.routeHateosCollectionHandlerGetAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                         @Override
                                                         public List<TestHateosResource2> handle(final BigInteger i,
                                                                                                 final List<TestHateosResource> r,
                                                                                                 final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                             assertEquals(id, i);
                                                             assertEquals(Lists.of(requestResource), r);
                                                             return Lists.of(responseResource);
                                                         }
                                                     },
                "/api/resource1/123/self",
                contentType,
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("GET resources successful"),
                this.httpEntity("[{\n" +
                        "  \"id\": \"234\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/234\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]", contentType));
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndWithoutResponseResourceGet() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeHateosCollectionHandlerAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handle(final BigInteger i,
                                                                                              final List<TestHateosResource> r,
                                                                                              final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          assertEquals(id, i);
                                                          assertEquals(Lists.of(requestResource), r);
                                                          return Lists.empty();
                                                      }
                                                  },
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("GET resources successful"),
                this.httpEntity("[]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndWithoutResponseResourcePost() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().post(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public List<TestHateosResource2> handle(final BigInteger i,
                                                            final List<TestHateosResource> r,
                                                            final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Lists.of(requestResource), r);
                        return Lists.empty();
                    }
                }),
                HttpMethod.POST,
                "/api/resource1/123/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("POST resources successful"),
                this.httpEntity("[]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndWithoutResponseResourcePut() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().put(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public List<TestHateosResource2> handle(final BigInteger i,
                                                            final List<TestHateosResource> r,
                                                            final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Lists.of(requestResource), r);
                        return Lists.empty();
                    }
                }),
                HttpMethod.PUT,
                "/api/resource1/123/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("PUT resources successful"),
                this.httpEntity("[]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdWithRequestResourceAndWithoutResponseResourceDelete() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeAndCheck(this.mapper().delete(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public List<TestHateosResource2> handle(final BigInteger i,
                                                            final List<TestHateosResource> r,
                                                            final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Lists.of(requestResource), r);
                        return Lists.empty();
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/123/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("DELETE resources successful"),
                this.httpEntity("[]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdEscaped() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);

        this.routeHateosCollectionHandlerGetAndCheck(new FakeHateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                         @Override
                                                         public List<TestHateosResource2> handle(final BigInteger i,
                                                                                                 final List<TestHateosResource> r,
                                                                                                 final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                             assertEquals(id, i);
                                                             assertEquals(Lists.of(requestResource), r);
                                                             return Lists.empty();
                                                         }
                                                     },
                "/api/resource1/\\1\\2\\3/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("GET resources successful"),
                this.httpEntity("[]", this.contentType()));
    }

    @Test
    public void testHateosCollectionHandlerIdDifferentTypeFromHateosResourceIdType() {
        final Long id = 123L;
        final TestHateosResource requestResource = TestHateosResource.with(BigInteger.valueOf(99));
        final TestHateosResource2 responseResource = TestHateosResource2.with(BigInteger.valueOf(id));

        final HateosHandlerBuilder<JsonNode> builder = this.builder()
                .add(this.resourceName1(), this.relation1(), HateosHandlerMapper.with(Long::parseLong, TestHateosResource.class, TestHateosResource2.class)
                        .get(new FakeHateosCollectionHandler<Long, TestHateosResource, TestHateosResource2>() {
                            @Override
                            public List<TestHateosResource2> handle(final Long i,
                                                                    final List<TestHateosResource> resources,
                                                                    final Map<HttpRequestAttribute<?>, Object> parameters) {
                                assertEquals(id, i, "id");
                                return Lists.of(responseResource);
                            }
                        }));
        this.routeAndCheck(builder,
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                this.toJsonArray(requestResource),
                HttpStatusCode.OK.setMessage("GET resources successful"),
                this.httpEntity("[{\n" +
                                "  \"id\": \"123\",\n" +
                                "  \"_links\": [{\n" +
                                "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                                "    \"method\": \"GET\",\n" +
                                "    \"rel\": \"self\",\n" +
                                "    \"type\": \"application/hal+json\"\n" +
                                "  }]\n" +
                                "}]",
                        this.contentType()));
    }

    private void routeHateosCollectionHandlerGetAndCheck(final HateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                         final String url,
                                                         final MediaType contentType,
                                                         final String body,
                                                         final HttpStatus status,
                                                         final HttpEntity... entities) {
        this.routeHateosCollectionHandlerAndCheck(handler,
                HttpMethod.GET,
                url,
                contentType,
                body,
                status,
                entities);
    }

    // HATEOSMAPPINGHANDLER GET RESOURCE ................................................................................................

    @Test
    public void testHateosMappingHandlerIdWithoutRequestResource() {
        final BigInteger id = BigInteger.valueOf(123);
        final Object response = 123;

        this.routeHateosMappingHandlerGetAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public Object handle(final BigInteger i,
                                                                           final Optional<TestHateosResource> r,
                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          assertEquals(id, i);
                                                          assertEquals(Optional.empty(), r);
                                                          return response;
                                                      }
                                                  },
                "/api/resource1/123/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("" + response, this.contentType()));
    }

    @Test
    public void testHateosMappingHandlerIdWithRequestResourceAndResponseResource() {
        this.HateosMappingHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentType());
    }

    @Test
    public void testHateosMappingHandlerIdWithRequestResourceAndResponseResourceDifferentCharset() {
        this.HateosMappingHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(this.contentTypeUtf16());
    }

    private void HateosMappingHandlerIdWithRequestResourceAndResponseResourceDifferentCharset(final MediaType contentType) {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final Object response = 234;

        this.routeHateosMappingHandlerGetAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public Object handle(final BigInteger i,
                                                                           final Optional<TestHateosResource> r,
                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          assertEquals(id, i);
                                                          assertEquals(Optional.of(requestResource), r);
                                                          return response;
                                                      }
                                                  },
                "/api/resource1/123/self",
                contentType,
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("" + response, contentType));
    }

    @Test
    public void testHateosMappingHandlerIdWithRequestResourcePost() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final Object response = 234;

        this.routeAndCheck(this.mapper().post(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Object handle(final BigInteger i,
                                         final Optional<TestHateosResource> r,
                                         final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return response;
                    }
                }),
                HttpMethod.POST,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("POST resource successful"),
                this.httpEntity("" + response, this.contentType()));
    }

    @Test
    public void testHateosMappingHandlerIdWithRequestResourcePut() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final Object response = 234;

        this.routeAndCheck(this.mapper().put(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Object handle(final BigInteger i,
                                         final Optional<TestHateosResource> r,
                                         final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return response;
                    }
                }),
                HttpMethod.PUT,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("PUT resource successful"),
                this.httpEntity("" + response, this.contentType()));
    }

    @Test
    public void testHateosMappingHandlerIdWithRequestResourceDelete() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final Object response = 234;

        this.routeAndCheck(this.mapper().delete(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                    @Override
                    public Object handle(final BigInteger i,
                                         final Optional<TestHateosResource> r,
                                         final Map<HttpRequestAttribute<?>, Object> parameters) {
                        assertEquals(id, i);
                        assertEquals(Optional.of(requestResource), r);
                        return response;
                    }
                }),
                HttpMethod.DELETE,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("DELETE resource successful"),
                this.httpEntity("" + response, this.contentType()));
    }

    @Test
    public void testHateosMappingHandlerIdEscaped() {
        final BigInteger id = BigInteger.valueOf(123);
        final TestHateosResource requestResource = TestHateosResource.with(id);
        final Object response = 234;

        this.routeHateosMappingHandlerGetAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public Object handle(final BigInteger i,
                                                                           final Optional<TestHateosResource> r,
                                                                           final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          assertEquals(id, i);
                                                          assertEquals(Optional.of(requestResource), r);
                                                          return response;
                                                      }
                                                  },
                "/api/resource1/\\1\\2\\3/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("" + response, this.contentType()));
    }

    @Test
    public void testHateosMappingHandlerIdDifferentTypeFromHateosResourceIdType() {
        final Long id = 123L;
        final TestHateosResource requestResource = TestHateosResource.with(BigInteger.valueOf(99));
        final Object response = 234;

        final HateosHandlerBuilder<JsonNode> builder = this.builder()
                .add(this.resourceName1(), this.relation1(), HateosHandlerMapper.with(Long::parseLong, TestHateosResource.class, TestHateosResource2.class)
                        .get(new FakeHateosMappingHandler<Long, TestHateosResource, TestHateosResource2>() {
                            @Override
                            public Object handle(final Long i,
                                                 final Optional<TestHateosResource> resource,
                                                 final Map<HttpRequestAttribute<?>, Object> parameters) {
                                assertEquals(id, i, "id");
                                return response;
                            }
                        }));
        this.routeAndCheck(builder,
                HttpMethod.GET,
                "/api/resource1/123/self",
                this.contentType(),
                requestResource.toJsonNode().toString(),
                HttpStatusCode.OK.setMessage("GET resource successful"),
                this.httpEntity("" + response,
                        this.contentType()));
    }

    private void routeHateosMappingHandlerGetAndCheck(final HateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                      final String url,
                                                      final MediaType contentType,
                                                      final String body,
                                                      final HttpStatus status,
                                                      final HttpEntity... entities) {
        this.routeHateosMappingHandlerAndCheck(handler,
                HttpMethod.GET,
                url,
                contentType,
                body,
                status,
                entities);
    }

    // HATEOSHANDLER GET COLLECTION  ................................................................................................

    @Test
    public void testHateosHandlerAllWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosHandlerAllWithoutRequestResourcesWithResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerAllWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1//self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerWildcardWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosHandlerWildcardWithoutRequestResourcesWithResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerWildcardWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosHandlerGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/*/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerRangeWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosHandlerRangeIdEscaped() {
        this.routeHateosHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/\\1-\\2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosHandlerRangeWithoutRequestResourcesWithResponseResources() {
        this.routeHateosHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerRangeWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosHandlerRangeWithRequestResourcesWithResponseResourcesDifferentContentType() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentTypeUtf16(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    private void routeHateosHandlerGetCollectionAndCheck(final Range<BigInteger> range,
                                                         final List<TestHateosResource> requestResources,
                                                         final List<TestHateosResource2> responseResources,
                                                         final String url,
                                                         final MediaType contentType,
                                                         final HttpStatus status,
                                                         final String responseBody) {
        this.routeHateosHandlerGetCollectionAndCheck(range,
                requestResources,
                responseResources,
                url,
                contentType,
                HasJsonNode.toJsonNodeList(requestResources).toString(),
                status,
                responseBody);
    }

    private void routeHateosHandlerGetCollectionAndCheck(final Range<BigInteger> range,
                                                         final List<TestHateosResource> requestResources,
                                                         final List<TestHateosResource2> responseResources,
                                                         final String url,
                                                         final MediaType contentType,
                                                         final String requestBody,
                                                         final HttpStatus status,
                                                         final String responseBody) {
        this.routeHateosHandlerGetAndCheck(new FakeHateosHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                               @Override
                                               public List<TestHateosResource2> handleCollection(final Range<BigInteger> i,
                                                                                                 final List<TestHateosResource> r,
                                                                                                 final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                   assertEquals(range, i);
                                                   assertEquals(requestResources, r);
                                                   return responseResources;
                                               }
                                           },
                url,
                contentType,
                requestBody,
                status,
                this.httpEntity(responseBody, contentType));
    }

    private void routeHateosHandlerAndCheck(final HateosHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                            final HttpMethod method,
                                            final String url,
                                            final MediaType contentType,
                                            final String body,
                                            final HttpStatus status,
                                            final HttpEntity... entities) {
        final HateosHandlerMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper = this.mapper();
        do {
            if (method.equals(HttpMethod.GET)) {
                mapper.get(handler);
                break;
            }
            if (method.equals(HttpMethod.POST)) {
                mapper.post(handler);
                break;
            }
            if (method.equals(HttpMethod.PUT)) {
                mapper.put(handler);
                break;
            }
            if (method.equals(HttpMethod.DELETE)) {
                mapper.delete(handler);
                break;
            }
            fail("Unknown method=" + method);
        } while (false);

        this.routeAndCheck(mapper,
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    private void routeHateosCollectionHandlerAndCheck(final HateosCollectionHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                      final HttpMethod method,
                                                      final String url,
                                                      final MediaType contentType,
                                                      final String body,
                                                      final HttpStatus status,
                                                      final HttpEntity... entities) {
        final HateosHandlerMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper = this.mapper();
        do {
            if (method.equals(HttpMethod.GET)) {
                mapper.get(handler);
                break;
            }
            if (method.equals(HttpMethod.POST)) {
                mapper.post(handler);
                break;
            }
            if (method.equals(HttpMethod.PUT)) {
                mapper.put(handler);
                break;
            }
            if (method.equals(HttpMethod.DELETE)) {
                mapper.delete(handler);
                break;
            }
            fail("Unknown method=" + method);
        } while (false);

        this.routeAndCheck(mapper,
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    // HateosMappingHandler GET COLLECTION  ................................................................................................

    @Test
    public void testHateosMappingHandlerAllWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosMappingHandlerAllWithoutRequestResourcesWithResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1//self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerAllWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1//self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerWildcardWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosMappingHandlerWildcardWithoutRequestResourcesWithResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1/*/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerWildcardWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosMappingHandlerGetCollectionAndCheck(Range.all(),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/*/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerRangeWithoutRequestResourcesWithoutResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosMappingHandlerRangeIdEscaped() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.empty(),
                "/api/resource1/\\1-\\2/self",
                this.contentType(),
                "",
                HttpStatusCode.NO_CONTENT.setMessage("GET resource collection successful"),
                "");
    }

    @Test
    public void testHateosMappingHandlerRangeWithoutRequestResourcesWithResponseResources() {
        this.routeHateosMappingHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                Lists.empty(),
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(123))),
                "/api/resource1/1-2/self",
                this.contentType(),
                "",
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"123\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/123\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerRangeWithRequestResourcesWithResponseResources() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosMappingHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentType(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    @Test
    public void testHateosMappingHandlerRangeWithRequestResourcesWithResponseResourcesDifferentContentType() {
        final List<TestHateosResource> request = Lists.of(TestHateosResource.with(BigInteger.valueOf(111)),
                TestHateosResource.with(BigInteger.valueOf(222)));

        this.routeHateosMappingHandlerGetCollectionAndCheck(this.rangeBetween(1, 2),
                request,
                Lists.of(TestHateosResource2.with(BigInteger.valueOf(999))),
                "/api/resource1/1-2/self",
                this.contentTypeUtf16(),
                HttpStatusCode.OK.setMessage("GET resource collection successful"),
                "[{\n" +
                        "  \"id\": \"999\",\n" +
                        "  \"_links\": [{\n" +
                        "    \"href\": \"http://www.example.com/api/resource1/999\",\n" +
                        "    \"method\": \"GET\",\n" +
                        "    \"rel\": \"self\",\n" +
                        "    \"type\": \"application/hal+json\"\n" +
                        "  }]\n" +
                        "}]");
    }

    private void routeHateosMappingHandlerGetCollectionAndCheck(final Range<BigInteger> range,
                                                                final List<TestHateosResource> requestResources,
                                                                final List<TestHateosResource2> responseResources,
                                                                final String url,
                                                                final MediaType contentType,
                                                                final HttpStatus status,
                                                                final String responseBody) {
        this.routeHateosMappingHandlerGetCollectionAndCheck(range,
                requestResources,
                responseResources,
                url,
                contentType,
                HasJsonNode.toJsonNodeList(requestResources).toString(),
                status,
                responseBody);
    }

    private void routeHateosMappingHandlerGetCollectionAndCheck(final Range<BigInteger> range,
                                                                final List<TestHateosResource> requestResources,
                                                                final List<TestHateosResource2> responseResources,
                                                                final String url,
                                                                final MediaType contentType,
                                                                final String requestBody,
                                                                final HttpStatus status,
                                                                final String responseBody) {
        this.routeHateosMappingHandlerGetAndCheck(new FakeHateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2>() {
                                                      @Override
                                                      public List<TestHateosResource2> handleCollection(final Range<BigInteger> i,
                                                                                                        final List<TestHateosResource> r,
                                                                                                        final Map<HttpRequestAttribute<?>, Object> parameters) {
                                                          assertEquals(range, i);
                                                          assertEquals(requestResources, r);
                                                          return responseResources;
                                                      }
                                                  },
                url,
                contentType,
                requestBody,
                status,
                this.httpEntity(responseBody, contentType));
    }

    private void routeHateosMappingHandlerAndCheck(final HateosMappingHandler<BigInteger, TestHateosResource, TestHateosResource2> handler,
                                                   final HttpMethod method,
                                                   final String url,
                                                   final MediaType contentType,
                                                   final String body,
                                                   final HttpStatus status,
                                                   final HttpEntity... entities) {
        final HateosHandlerMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper = this.mapper();
        do {
            if (method.equals(HttpMethod.GET)) {
                mapper.get(handler);
                break;
            }
            if (method.equals(HttpMethod.POST)) {
                mapper.post(handler);
                break;
            }
            if (method.equals(HttpMethod.PUT)) {
                mapper.put(handler);
                break;
            }
            if (method.equals(HttpMethod.DELETE)) {
                mapper.delete(handler);
                break;
            }
            fail("Unknown method=" + method);
        } while (false);

        this.routeAndCheck(mapper,
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    // HELPERS ............................................................................................

    @Override
    public HateosHandlerRouter<JsonNode> createRouter() {
        final HateosHandlerBuilder<JsonNode> builder = this.builder();
        //builder.add(this.resourceName1(), this.relation1(), new FakeHateosGetHandler<BigInteger, JsonNode>());
        return Cast.to(builder.build()); // builder returns interface which is HHBR class.
    }

    private Range<BigInteger> rangeBetween(final long begin, final long end) {
        return Range.greaterThanEquals(BigInteger.valueOf(begin))
                .and(Range.lessThanEquals(BigInteger.valueOf(end)));
    }

    private String toJsonArray(final HasJsonNode resource) {
        return JsonNode.array()
                .appendChild(resource.toJsonNode())
                .toString();
    }

    private HateosHandlerBuilder<JsonNode> builder() {
        return HateosHandlerBuilder.with(
                this.base(),
                HateosContentType.json());
    }

    private AbsoluteUrl base() {
        return Url.parseAbsolute("http://www.example.com/api");
    }

    private HateosResourceName resourceName1() {
        return HateosResourceName.with("resource1");
    }

    private HateosResourceName resourceName2() {
        return HateosResourceName.with("resource2");
    }

    private LinkRelation<?> relation1() {
        return LinkRelation.SELF;
    }

    private LinkRelation<?> relation2() {
        return LinkRelation.NEXT;
    }

    private MediaType contentType() {
        return HateosContentType.json().contentType();
    }

    private MediaType contentTypeUtf16() {
        return this.contentType().setCharset(CharsetName.UTF_16);
    }

    private TestHateosResource resource(final long id) {
        return TestHateosResource.with(BigInteger.valueOf(id));
    }

    private TestHateosResource2 resource2(final long id) {
        return TestHateosResource2.with(BigInteger.valueOf(id));
    }

    private HttpEntity[] httpEntity(final String body,
                                    final MediaType contentType) {
        HttpEntity[] entities = new HttpEntity[0];

        if (!CharSequences.isNullOrEmpty(body)) {
            final CharsetName charsetName = MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8);
            final byte[] bytes = this.bytes(body, contentType);

            final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();
            headers.put(HttpHeaderName.CONTENT_TYPE, HateosContentType.json().contentType().setCharset(charsetName));
            headers.put(HttpHeaderName.CONTENT_LENGTH, Long.valueOf(bytes(body, contentType).length));

            entities = new HttpEntity[]{HttpEntity.with(headers, bytes)};
        }
        return entities;
    }

    private void routeAndCheck(final HateosHandlerMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        this.routeAndCheck(this.builder().add(this.resourceName1(), this.relation1(), mapper),
                method,
                url,
                contentType,
                body,
                status,
                entities);
    }

    private void routeAndCheck(final HateosHandlerBuilder<JsonNode> builder,
                               final HttpMethod method,
                               final String url,
                               final MediaType contentType,
                               final String body,
                               final HttpStatus status,
                               final HttpEntity... entities) {
        final TestRecordingHttpResponse response = HttpResponses.testRecording();

        final HttpRequest request = this.request(method,
                url,
                contentType,
                bytes(body, contentType));
        final Optional<BiConsumer<HttpRequest, HttpResponse>> handle = builder.build()
                .route(request.routingParameters());
        if (handle.isPresent()) {
            handle.get()
                    .accept(request, response);
        }

        response.check(request, status, entities);
    }

    private byte[] bytes(final String body, final MediaType contentType) {
        return null != body ?
                body.getBytes(MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8).charset().get()) :
                null;
    }

    private HateosHandlerMapper<BigInteger, TestHateosResource, TestHateosResource2> mapper() {
        return HateosHandlerMapper.with(BigInteger::new,
                TestHateosResource.class,
                TestHateosResource2.class);
    }

    private HttpRequest request(final HttpMethod method,
                                final String url,
                                final MediaType contentType,
                                final byte[] body) {
        return new HttpRequest() {

            @Override
            public HttpTransport transport() {
                return HttpTransport.UNSECURED;
            }

            @Override
            public HttpProtocolVersion protocolVersion() {
                return HttpProtocolVersion.VERSION_1_0;
            }

            @Override
            public HttpMethod method() {
                return method;
            }

            @Override
            public RelativeUrl url() {
                return RelativeUrl.parse(url);
            }

            @Override
            public Map<HttpHeaderName<?>, Object> headers() {
                final Map<HttpHeaderName<?>, Object> headers = Maps.sorted();
                headers.put(HttpHeaderName.ACCEPT_CHARSET, AcceptCharset.parse(MediaTypeParameterName.CHARSET.parameterValue(contentType).orElse(CharsetName.UTF_8).toHeaderText()));
                headers.put(HttpHeaderName.CONTENT_TYPE, contentType);
                return headers;
            }

            @Override
            public byte[] body() {
                return null != body ?
                        body.clone() :
                        body;
            }

            @Override
            public Map<HttpRequestParameterName, List<String>> parameters() {
                final Map<HttpRequestParameterName, List<String>> parameters = Maps.sorted();

                this.url()
                        .query()
                        .parameters()
                        .entrySet()
                        .forEach(e -> parameters.put(HttpRequestParameterName.with(e.getKey().value()), e.getValue()));

                return parameters;
            }

            @Override
            public List<String> parameterValues(final HttpRequestParameterName parameterName) {
                final List<String> values = this.parameters().get(parameterName);
                return null == values ?
                        Lists.empty() :
                        values;
            }

            @Override
            public String toString() {
                return this.method() + " " + this.url() + " " + parameters();
            }
        };
    }

    private void checkParameters(Map<HttpRequestAttribute<?>, Object> parameters) {
        assertEquals(Lists.of("value1"),
                parameters.get(UrlParameterName.with("param1")),
                "parameters");
    }

    @Override
    public Class<HateosHandlerRouter<JsonNode>> type() {
        return Cast.to(HateosHandlerRouter.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
