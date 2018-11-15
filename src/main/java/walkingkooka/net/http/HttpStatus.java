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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Whitespace;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Holds a HTTP response and message.
 */
public final class HttpStatus implements HashCodeEqualsDefined {

    /**
     * Factory that creates a {@link HttpStatus}. This factory is called exclusively by all constants in this class.
     */
    private static HttpStatus registerConstant(final int value, final String message) {
        final HttpStatus statusCode = new HttpStatus(value, message);
        CONSTANTS.put(value, statusCode);
        return statusCode;
    }

    /**
     * Holds all constants keyed by their code. This must appear before the constants so the map is initialized and not null.
     */
    private static Map<Integer, HttpStatus> CONSTANTS = Maps.hash();

    /**
     * Continue={@link HttpServletResponse#SC_CONTINUE}
     */
    public final static HttpStatus CONTINUE = registerConstant(HttpServletResponse.SC_CONTINUE, "Continue");

    /**
     * Switching protocols={@link HttpServletResponse#SC_SWITCHING_PROTOCOLS}
     */
    public final static HttpStatus SWITCHING_PROTOCOLS = registerConstant(
            HttpServletResponse.SC_SWITCHING_PROTOCOLS,
            "Switching protocols");

    // Success

    /**
     * OK={@link HttpServletResponse#SC_OK}
     */
    public final static HttpStatus OK = registerConstant(HttpServletResponse.SC_OK, "OK");

    /**
     * Created={@link HttpServletResponse#SC_CREATED}
     */
    public final static HttpStatus CREATED = registerConstant(HttpServletResponse.SC_CREATED, "Created");

    /**
     * Accepted={@link HttpServletResponse#SC_ACCEPTED}
     */
    public final static HttpStatus ACCEPTED = registerConstant(HttpServletResponse.SC_ACCEPTED, "Accepted");

    /**
     * Non Authoritative information={@link HttpServletResponse#SC_NON_AUTHORITATIVE_INFORMATION}
     */
    public final static HttpStatus NON_AUTHORITATIVE_INFORMATION = registerConstant(
            HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION,
            "Non authoritative information");

    /**
     * No content={@link HttpServletResponse#SC_NO_CONTENT}
     */
    public final static HttpStatus NO_CONTENT = registerConstant(HttpServletResponse.SC_NO_CONTENT,
            "No content");

    /**
     * Reset content={@link HttpServletResponse#SC_RESET_CONTENT}
     */
    public final static HttpStatus RESET_CONTENT = registerConstant(HttpServletResponse.SC_RESET_CONTENT,
            "Reset content");

    /**
     * Partial content={@link HttpServletResponse#SC_PARTIAL_CONTENT}
     */
    public final static HttpStatus PARTIAL_CONTENT = registerConstant(HttpServletResponse.SC_PARTIAL_CONTENT,
            "Partial content");

    // Redirect

    /**
     * Multiple choices={@link HttpServletResponse#SC_MULTIPLE_CHOICES}
     */
    public final static HttpStatus MULTIPLE_CHOICES = registerConstant(
            HttpServletResponse.SC_MULTIPLE_CHOICES,
            "Multiple choices");

    /**
     * Moved permanently={@link HttpServletResponse#SC_MOVED_PERMANENTLY}
     */
    public final static HttpStatus MOVED_PERMANENTLY = registerConstant(
            HttpServletResponse.SC_MOVED_PERMANENTLY,
            "Moved permanently");

    /**
     * Found={@link HttpServletResponse#SC_MOVED_TEMPORARILY}
     */
    public final static HttpStatus MOVED_TEMPORARILY = registerConstant(
            HttpServletResponse.SC_MOVED_TEMPORARILY,
            "Moved temporarily");

    /**
     * Found={@link HttpServletResponse#SC_FOUND}
     */
    public final static HttpStatus FOUND = registerConstant(HttpServletResponse.SC_FOUND, "Found");

    /**
     * See other={@link HttpServletResponse#SC_SEE_OTHER}
     */
    public final static HttpStatus SEE_OTHER = registerConstant(HttpServletResponse.SC_SEE_OTHER,
            "See other");

    /**
     * Not Modified={@link HttpServletResponse#SC_NOT_MODIFIED}
     */
    public final static HttpStatus NOT_MODIFIED = registerConstant(HttpServletResponse.SC_NOT_MODIFIED,
            "Not modified");

    /**
     * User Proxy={@link HttpServletResponse#SC_USE_PROXY}
     */
    public final static HttpStatus USE_PROXY = registerConstant(HttpServletResponse.SC_USE_PROXY,
            "User proxy");

    /**
     * Temporary redirect={@link HttpServletResponse#SC_TEMPORARY_REDIRECT}
     */
    public final static HttpStatus TEMPORARY_REDIRECT = registerConstant(
            HttpServletResponse.SC_TEMPORARY_REDIRECT,
            "Temporary redirect");

    // Client Error

    /**
     * Bad request={@link HttpServletResponse#SC_BAD_REQUEST}
     */
    public final static HttpStatus BAD_REQUEST = registerConstant(HttpServletResponse.SC_BAD_REQUEST,
            "Bad request");

    /**
     * Unauthorized={@link HttpServletResponse#SC_UNAUTHORIZED}
     */
    public final static HttpStatus UNAUTHORIZED = registerConstant(HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized");

    /**
     * Payment required={@link HttpServletResponse#SC_PAYMENT_REQUIRED}
     */
    public final static HttpStatus PAYMENT_REQUIRED = registerConstant(
            HttpServletResponse.SC_PAYMENT_REQUIRED,
            "Payment required");

    /**
     * Forbidden={@link HttpServletResponse#SC_FORBIDDEN}
     */
    public final static HttpStatus FORBIDDEN = registerConstant(HttpServletResponse.SC_FORBIDDEN,
            "Forbidden");

    /**
     * Not found={@link HttpServletResponse#SC_NOT_FOUND}
     */
    public final static HttpStatus NOT_FOUND = registerConstant(HttpServletResponse.SC_NOT_FOUND,
            "Not found");

    /**
     * Method not allowed={@link HttpServletResponse#SC_METHOD_NOT_ALLOWED}
     */
    public final static HttpStatus METHOD_NOT_ALLOWED = registerConstant(
            HttpServletResponse.SC_METHOD_NOT_ALLOWED,
            "Method not allowed");

    /**
     * Not acceptable={@link HttpServletResponse#SC_NOT_ACCEPTABLE}
     */
    public final static HttpStatus NOT_ACCEPTABLE = registerConstant(HttpServletResponse.SC_NOT_ACCEPTABLE,
            "Not acceptable");

    /**
     * Request timeout={@link HttpServletResponse#SC_REQUEST_TIMEOUT}
     */
    public final static HttpStatus REQUEST_TIMEOUT = registerConstant(HttpServletResponse.SC_REQUEST_TIMEOUT,
            "Request timeout");

    /**
     * Conflict={@link HttpServletResponse#SC_CONFLICT}
     */
    public final static HttpStatus CONFLICT = registerConstant(HttpServletResponse.SC_CONFLICT, "Conflict");

    /**
     * Gone={@link HttpServletResponse#SC_GONE}
     */
    public final static HttpStatus GONE = registerConstant(HttpServletResponse.SC_GONE, "Gone");

    /**
     * Length required={@link HttpServletResponse#SC_LENGTH_REQUIRED}
     */
    public final static HttpStatus LENGTH_REQUIRED = registerConstant(HttpServletResponse.SC_LENGTH_REQUIRED,
            "Length required");

    /**
     * Precondition failed={@link HttpServletResponse#SC_PRECONDITION_FAILED}
     */
    public final static HttpStatus PRECONDITION_FAILED = registerConstant(
            HttpServletResponse.SC_PRECONDITION_FAILED,
            "Precondition failed");

    /**
     * Proxy Authentication required={@link HttpServletResponse#SC_PROXY_AUTHENTICATION_REQUIRED}
     */
    public final static HttpStatus PROXY_AUTHENTICATION_REQUIRED = registerConstant(
            HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED,
            "Proxy Authentication required");

    /**
     * Request Entity Too Large={@link HttpServletResponse#SC_REQUEST_ENTITY_TOO_LARGE}
     */
    public final static HttpStatus REQUEST_ENTITY_TOO_LARGE = registerConstant(
            HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
            "Request entity too large");

    /**
     * Request-URI Too Long={@link HttpServletResponse#SC_REQUEST_URI_TOO_LONG}
     */
    public final static HttpStatus REQUEST_URI_TOO_LONG = registerConstant(
            HttpServletResponse.SC_REQUEST_URI_TOO_LONG,
            "Request-URI Too Long");

    /**
     * Unsupported Media Type={@link HttpServletResponse#SC_UNSUPPORTED_MEDIA_TYPE}
     */
    public final static HttpStatus UNSUPPORTED_MEDIA_TYPE = registerConstant(
            HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
            "Unsupported Media Type");

    /**
     * Requested Range Not Satisfiable={@link HttpServletResponse#SC_REQUESTED_RANGE_NOT_SATISFIABLE}
     */
    public final static HttpStatus REQUESTED_RANGE_NOT_SATISFIABLE = registerConstant(
            HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable");

    /**
     * Expectation Failed={@link HttpServletResponse#SC_EXPECTATION_FAILED}
     */
    public final static HttpStatus EXPECTATION_FAILED = registerConstant(
            HttpServletResponse.SC_EXPECTATION_FAILED,
            "Expectation Failed");

    // Server Error

    /**
     * Internal Server Error={@link HttpServletResponse#SC_INTERNAL_SERVER_ERROR}
     */
    public final static HttpStatus INTERNAL_SERVER_ERROR = registerConstant(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "Internal Server Error");

    /**
     * Not Implemented={@link HttpServletResponse#SC_NOT_IMPLEMENTED}
     */
    public final static HttpStatus NOT_IMPLEMENTED = registerConstant(HttpServletResponse.SC_NOT_IMPLEMENTED,
            "Not implemented");

    /**
     * Bad Gateway={@link HttpServletResponse#SC_BAD_GATEWAY}
     */
    public final static HttpStatus BAD_GATEWAY = registerConstant(HttpServletResponse.SC_BAD_GATEWAY,
            "Bad Gateway");

    /**
     * Service Unavailable={@link HttpServletResponse#SC_SERVICE_UNAVAILABLE}
     */
    public final static HttpStatus SERVICE_UNAVAILABLE = registerConstant(
            HttpServletResponse.SC_SERVICE_UNAVAILABLE,
            "Service Unavailable");

    /**
     * Gateway Timeout={@link HttpServletResponse#SC_GATEWAY_TIMEOUT}
     */
    public final static HttpStatus GATEWAY_TIMEOUT = registerConstant(HttpServletResponse.SC_GATEWAY_TIMEOUT,
            "Gateway Timeout");

    /**
     * HTTP Version Not Supported={@link HttpServletResponse#SC_HTTP_VERSION_NOT_SUPPORTED}
     */
    public final static HttpStatus HTTP_VERSION_NOT_SUPPORTED = registerConstant(
            HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED,
            "HTTP Version Not Supported");

    /**
     * Lookup that retrieves the {@link HttpStatus} which includes a standard default message with the given code, returning null if unknown.
     */
    public static HttpStatus withCode(final int code) {
        return CONSTANTS.get(code);
    }

    /**
     * Factory that creates a {@link HttpStatus}
     */
    public static HttpStatus with(final int value, final String message) {
        checkCode(value);
        checkMessage(message);

        return new HttpStatus(value, message);
    }

    private static void checkCode(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Code " + value + " must be >= 0");
        }
    }

    private static void checkMessage(final String message) {
        Whitespace.failIfNullOrWhitespace(message, "message");
    }

    /**
     * Private constructor use static factory.
     */
    private HttpStatus(final int value, final String message) {
        super();
        this.value = value;
        this.message = message;
    }

    /**
     * Tests if code is informational (100-199)
     */
    public boolean isInformational() {
        return this.isRange(100, 199);
    }

    /**
     * Tests if code is successful (200-299)
     */
    public boolean isSuccessful() {
        return this.isRange(200, 299);
    }

    /**
     * Tests if code is a redirect
     *
     * <pre>
     * HTTP status codes 3xx
     * In the HTTP protocol used by the World Wide Web, a redirect is a response with a status code beginning with 3 that causes a browser to display a different page. The different codes describe the reason for the redirect, which allows for the correct subsequent action (such as changing links in the case of code 301, a permanent change of address).
     * The HTTP standard defines several status codes for redirection:
     * 300 multiple choices (e.g. offer different languages)
     * 301 moved permanently
     * 302 found (originally temporary redirect, but now commonly used to specify redirection for unspecified reason)
     * 303 see other (e.g. for results of cgi-scripts)
     * 307 temporary redirect
     * </pre>
     * <p>
     * <a href="http://en.wikipedia.org/wiki/HTTP_redirect"></a>}
     */
    public boolean isRedirect() {
        final int value = this.value;
        return value == MULTIPLE_CHOICES.value || //
                value == MOVED_PERMANENTLY.value || //
                value == FOUND.value || //
                value == SEE_OTHER.value || //
                value == TEMPORARY_REDIRECT.value;
    }

    /**
     * Tests if code is a client error (400-499)
     */
    public boolean isClientError() {
        return this.isRange(400, 499);
    }

    /**
     * Tests if code is a server error (500-599)
     */
    public boolean isServerError() {
        return this.isRange(500, 599);
    }

    /**
     * Helper that tests if the actual value is between the lower and upper range(inclusive).
     */
    private boolean isRange(final int lower, final int upper) {
        final int value = this.value;
        return value >= lower && value <= upper;
    }

    /**
     * Getter that returns the code
     */
    public int value() {
        return this.value;
    }

    private final int value;

    /**
     * Getter that returns the message accompanying the code
     */
    public String message() {
        return this.message;
    }

    /**
     * Would be setter that returns a {@link HttpStatus} with the new message. Together with this method any of the constants may be used to set a
     * message inheriting the status code.
     */
    public HttpStatus setMessage(final String message) {
        checkMessage(message);

        return this.message.equals(message) ?
                this :
                new HttpStatus(this.value, message);
    }

    private final String message;

    // Object........................................................................

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HttpStatus && this.equals0(Cast.to(other));
    }

    /**
     * Note only the value and not the message is included in equality tests.
     */
    private boolean equals0(final HttpStatus other) {
        return this.value == other.value;
    }

    /**
     * Dumps the status message followed by the code
     */
    @Override
    public String toString() {
        return this.value + "=" + this.message;
    }
}
