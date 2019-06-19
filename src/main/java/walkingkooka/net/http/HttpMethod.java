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

package walkingkooka.net.http;

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.net.header.HeaderValue;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Whitespace;

/**
 * A {@link Value} including an enumeration of standards methods that contains the HTTP request methods.
 */
public final class HttpMethod implements Value<String>,
        HeaderValue,
        Comparable<HttpMethod>,
        HashCodeEqualsDefined {

    private final static String HEAD_STRING = "HEAD";

    /**
     * The <code>HEAD</code> method
     */
    public final static HttpMethod HEAD = createConstant(HEAD_STRING);

    private final static String GET_STRING = "GET";

    /**
     * The <code>GET</code> method
     */
    public final static HttpMethod GET = createConstant(GET_STRING);

    private final static String POST_STRING = "POST";

    /**
     * The <code>POST</code> method
     */
    public final static HttpMethod POST = createConstant(POST_STRING);

    private final static String PUT_STRING = "PUT";

    /**
     * The <code>PUT</code> method
     */
    public final static HttpMethod PUT = createConstant(PUT_STRING);

    private final static String DELETE_STRING = "DELETE";

    /**
     * The <code>DELETE</code> method
     */
    public final static HttpMethod DELETE = createConstant(DELETE_STRING);

    private final static String TRACE_STRING = "TRACE";

    /**
     * The <code>TRACE</code> method
     */
    public final static HttpMethod TRACE = createConstant(TRACE_STRING);

    private final static String OPTIONS_STRING = "OPTIONS";

    /**
     * The <code>OPTIONS</code> method
     */
    public final static HttpMethod OPTIONS = createConstant(OPTIONS_STRING); //

    private final static String CONNECT_STRING = "CONNECT";

    /**
     * The <code>CONNECT</code> method
     */
    public final static HttpMethod CONNECT = createConstant(CONNECT_STRING);

    private final static String PATCH_STRING = "PATCH";

    /**
     * The <code>PATCH</code> method
     */
    public final static HttpMethod PATCH = createConstant(PATCH_STRING);

    /**
     * Creates and adds a new {@link HttpMethod} to the cache being built.
     */
    private static HttpMethod createConstant(final String header) {
        return new HttpMethod(header, header.toUpperCase());
    }

    /**
     * Factory that creates a {@link HttpMethod} with the {@link String method name}.
     */
    public static HttpMethod with(final String method) {
        Whitespace.failIfNullOrEmptyOrWhitespace(method, "method");

        final String headerText = method.toUpperCase();

        HttpMethod httpMethod;
        switch(headerText) {
            case HEAD_STRING:
                httpMethod = HEAD;
                break;
            case GET_STRING:
                httpMethod = GET;
                break;
            case POST_STRING:
                httpMethod = POST;
                break;
            case PUT_STRING:
                httpMethod = PUT;
                break;
            case DELETE_STRING:
                httpMethod = DELETE;
                break;
            case TRACE_STRING:
                httpMethod = TRACE;
                break;
            case OPTIONS_STRING:
                httpMethod = OPTIONS;
                break;
            case CONNECT_STRING:
                httpMethod = CONNECT;
                break;
            case PATCH_STRING:
                httpMethod = PATCH;
                break;
            default:
                // verify method name
                final int length = method.length();
                for (int i = 0; i < length; i++) {
                    final char c = method.charAt(i);
                    if (false == Character.isLetter(c)) {
                        throw new IllegalArgumentException(
                                "Method includes invalid character " + CharSequences.quoteAndEscape(c) + "'="
                                        + CharSequences.quoteAndEscape(method));
                    }
                }

                httpMethod = new HttpMethod(method, headerText);
        }

        return httpMethod;
    }

    /**
     * Private constructor use static factory.
     */
    private HttpMethod(final String value,
                       final String headerText) {
        super();
        this.value = value;
        this.headerText = headerText;
    }

    /**
     * Getter that returns the method as a {@link String}.
     */
    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    /**
     * Tests if the method is a GET or HEAD a commonly performed test.
     */
    public boolean isGetOrHead() {
        return this == GET || this == HEAD;
    }

    // HeaderValue...........................................................................................................

    @Override
    public String toHeaderText() {
        return this.headerText;
    }

    private final String headerText;

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // HttpMethodVisitor................................................................................................

    void accept(final HttpMethodVisitor visitor) {
        switch(this.headerText) {
            case HEAD_STRING:
                visitor.visitHead();
                break;
            case GET_STRING:
                visitor.visitGet();
                break;
            case POST_STRING:
                visitor.visitPost();
                break;
            case PUT_STRING:
                visitor.visitPut();
                break;
            case DELETE_STRING:
                visitor.visitDelete();
                break;
            case TRACE_STRING:
                visitor.visitTrace();
                break;
            case OPTIONS_STRING:
                visitor.visitOptions();
                break;
            case CONNECT_STRING:
                visitor.visitConnect();
                break;
            case PATCH_STRING:
                visitor.visitPatch();
                break;
            default:
                visitor.visitUnknown(this);
        }
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpMethod &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final HttpMethod other) {
        return 0 == this.compareTo(other);
    }

    /**
     * Dumps the method name.
     */
    @Override
    public String toString() {
        return this.value;
    }

    // Comparable..........................................................................................................

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    @Override
    public int compareTo(final HttpMethod other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }
}
