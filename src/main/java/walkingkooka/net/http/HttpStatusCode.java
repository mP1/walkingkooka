/*
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"),
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

import javax.servlet.http.HttpServletResponse;

/**
 * Holds all possible http status codes.<br>
 * <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes"></a>
 */
public enum HttpStatusCode {

    /**
     * Continue={@link HttpServletResponse#SC_CONTINUE}
     */
    CONTINUE(HttpServletResponse.SC_CONTINUE, "Continue"),

    /**
     * Switching protocols={@link HttpServletResponse#SC_SWITCHING_PROTOCOLS}
     */
    SWITCHING_PROTOCOLS(HttpServletResponse.SC_SWITCHING_PROTOCOLS, "Switching protocols"),

    // Success

    /**
     * OK={@link HttpServletResponse#SC_OK}
     */
    OK(HttpServletResponse.SC_OK, "OK"),

    /**
     * Created={@link HttpServletResponse#SC_CREATED}
     */
    CREATED(HttpServletResponse.SC_CREATED, "Created"),

    /**
     * Accepted={@link HttpServletResponse#SC_ACCEPTED}
     */
    ACCEPTED(HttpServletResponse.SC_ACCEPTED, "Accepted"),

    /**
     * Non Authoritative information={@link HttpServletResponse#SC_NON_AUTHORITATIVE_INFORMATION}
     */
    NON_AUTHORITATIVE_INFORMATION(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION, "Non authoritative information"),

    /**
     * No content={@link HttpServletResponse#SC_NO_CONTENT}
     */
    NO_CONTENT(HttpServletResponse.SC_NO_CONTENT, "No content"),

    /**
     * Reset content={@link HttpServletResponse#SC_RESET_CONTENT}
     */
    RESET_CONTENT(HttpServletResponse.SC_RESET_CONTENT, "Reset content"),

    /**
     * Partial content={@link HttpServletResponse#SC_PARTIAL_CONTENT}
     */
    PARTIAL_CONTENT(HttpServletResponse.SC_PARTIAL_CONTENT, "Partial content"),

    // Redirect

    /**
     * Multiple choices={@link HttpServletResponse#SC_MULTIPLE_CHOICES}
     */
    MULTIPLE_CHOICES(HttpServletResponse.SC_MULTIPLE_CHOICES, "Multiple choices"),

    /**
     * Moved permanently={@link HttpServletResponse#SC_MOVED_PERMANENTLY}
     */
    MOVED_PERMANENTLY(HttpServletResponse.SC_MOVED_PERMANENTLY, "Moved permanently"),

    /**
     * Found={@link HttpServletResponse#SC_MOVED_TEMPORARILY}
     */
    MOVED_TEMPORARILY(HttpServletResponse.SC_MOVED_TEMPORARILY, "Moved temporarily"),

    /**
     * Found={@link HttpServletResponse#SC_FOUND}
     */
    FOUND(HttpServletResponse.SC_FOUND, "Found"),

    /**
     * See other={@link HttpServletResponse#SC_SEE_OTHER}
     */
    SEE_OTHER(HttpServletResponse.SC_SEE_OTHER, "See other"),

    /**
     * Not Modified={@link HttpServletResponse#SC_NOT_MODIFIED}
     */
    NOT_MODIFIED(HttpServletResponse.SC_NOT_MODIFIED, "Not modified"),

    /**
     * User Proxy={@link HttpServletResponse#SC_USE_PROXY}
     */
    USE_PROXY(HttpServletResponse.SC_USE_PROXY, "User proxy"),

    /**
     * Temporary redirect={@link HttpServletResponse#SC_TEMPORARY_REDIRECT}
     */
    TEMPORARY_REDIRECT(HttpServletResponse.SC_TEMPORARY_REDIRECT, "Temporary redirect"),

    // Client Error

    /**
     * Bad request={@link HttpServletResponse#SC_BAD_REQUEST}
     */
    BAD_REQUEST(HttpServletResponse.SC_BAD_REQUEST, "Bad request"),

    /**
     * Unauthorized={@link HttpServletResponse#SC_UNAUTHORIZED}
     */
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"),

    /**
     * Payment required={@link HttpServletResponse#SC_PAYMENT_REQUIRED}
     */
    PAYMENT_REQUIRED(HttpServletResponse.SC_PAYMENT_REQUIRED, "Payment required"),

    /**
     * Forbidden={@link HttpServletResponse#SC_FORBIDDEN}
     */
    FORBIDDEN(HttpServletResponse.SC_FORBIDDEN, "Forbidden"),

    /**
     * Not found={@link HttpServletResponse#SC_NOT_FOUND}
     */
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "Not found"),

    /**
     * Method not allowed={@link HttpServletResponse#SC_METHOD_NOT_ALLOWED}
     */
    METHOD_NOT_ALLOWED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed"),

    /**
     * Not acceptable={@link HttpServletResponse#SC_NOT_ACCEPTABLE}
     */
    NOT_ACCEPTABLE(HttpServletResponse.SC_NOT_ACCEPTABLE, "Not acceptable"),

    /**
     * Request timeout={@link HttpServletResponse#SC_REQUEST_TIMEOUT}
     */
    REQUEST_TIMEOUT(HttpServletResponse.SC_REQUEST_TIMEOUT, "Request timeout"),

    /**
     * Conflict={@link HttpServletResponse#SC_CONFLICT}
     */
    CONFLICT(HttpServletResponse.SC_CONFLICT, "Conflict"),

    /**
     * Gone={@link HttpServletResponse#SC_GONE}
     */
    GONE(HttpServletResponse.SC_GONE, "Gone"),

    /**
     * Length required={@link HttpServletResponse#SC_LENGTH_REQUIRED}
     */
    LENGTH_REQUIRED(HttpServletResponse.SC_LENGTH_REQUIRED, "Length required"),

    /**
     * Precondition failed={@link HttpServletResponse#SC_PRECONDITION_FAILED}
     */
    PRECONDITION_FAILED(HttpServletResponse.SC_PRECONDITION_FAILED, "Precondition failed"),

    /**
     * Proxy Authentication required={@link HttpServletResponse#SC_PROXY_AUTHENTICATION_REQUIRED}
     */
    PROXY_AUTHENTICATION_REQUIRED(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED, "Proxy Authentication required"),

    /**
     * Request Entity Too Large={@link HttpServletResponse#SC_REQUEST_ENTITY_TOO_LARGE}
     */
    REQUEST_ENTITY_TOO_LARGE(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "Request entity too large"),

    /**
     * Request-URI Too Long={@link HttpServletResponse#SC_REQUEST_URI_TOO_LONG}
     */
    REQUEST_URI_TOO_LONG(HttpServletResponse.SC_REQUEST_URI_TOO_LONG, "Request-URI Too Long"),

    /**
     * Unsupported Media Type={@link HttpServletResponse#SC_UNSUPPORTED_MEDIA_TYPE}
     */
    UNSUPPORTED_MEDIA_TYPE(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type"),

    /**
     * Requested Range Not Satisfiable={@link HttpServletResponse#SC_REQUESTED_RANGE_NOT_SATISFIABLE}
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE, "Requested Range Not Satisfiable"),

    /**
     * Expectation Failed={@link HttpServletResponse#SC_EXPECTATION_FAILED}
     */
    EXPECTATION_FAILED(HttpServletResponse.SC_EXPECTATION_FAILED, "Expectation Failed"),

    // Server Error

    /**
     * Internal Server Error={@link HttpServletResponse#SC_INTERNAL_SERVER_ERROR}
     */
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),

    /**
     * Not Implemented={@link HttpServletResponse#SC_NOT_IMPLEMENTED}
     */
    NOT_IMPLEMENTED(HttpServletResponse.SC_NOT_IMPLEMENTED, "Not implemented"),

    /**
     * Bad Gateway={@link HttpServletResponse#SC_BAD_GATEWAY}
     */
    BAD_GATEWAY(HttpServletResponse.SC_BAD_GATEWAY, "Bad Gateway"),

    /**
     * Service Unavailable={@link HttpServletResponse#SC_SERVICE_UNAVAILABLE}
     */
    SERVICE_UNAVAILABLE(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "Service Unavailable"),

    /**
     * Gateway Timeout={@link HttpServletResponse#SC_GATEWAY_TIMEOUT}
     */
    GATEWAY_TIMEOUT(HttpServletResponse.SC_GATEWAY_TIMEOUT, "Gateway Timeout"),

    /**
     * HTTP Version Not Supported={@link HttpServletResponse#SC_HTTP_VERSION_NOT_SUPPORTED}
     */
    HTTP_VERSION_NOT_SUPPORTED(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported");

    HttpStatusCode(final int code, final String message) {
        this.code = code;
        this.message = message;
        this.category = HttpStatusCodeCategory.category(code);
    }

    /**
     * Returns a {@link HttpStatus} with this code and a default message.
     */
    public HttpStatus status() {
        return HttpStatus.with(this, this.message);
    }

    /**
     * The numeric value of this code.
     */
    public int code() {
        return this.code;
    }

    private final int code;

    /**
     * Returns the category for this code.
     */
    public HttpStatusCodeCategory category() {
        return this.category;
    }

    private HttpStatusCodeCategory category;

    /**
     * The default message for this code.
     */
    String message;
}