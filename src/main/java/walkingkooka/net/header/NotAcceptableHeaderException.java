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

package walkingkooka.net.header;

/**
 * The exception thrown if response condition fails, such as the response content-type not matching an accept-charset list.
 * <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Accept-Charset"></a>
 * <pre>
 * The Accept-Charset request HTTP header advertises which character set the client is able to understand.
 * Using content negotiation, the server then selects one of the proposals, uses it and informs the client of its
 * choice within the Content-Type response header. Browsers usually don't set this header as the default value for
 * each content type is usually correct and transmitting it would allow easier fingerprinting.
 *
 * If the server cannot serve any matching character set, it can theoretically send back a 406 (Not Acceptable)
 * error code. But, for a better user experience, this is rarely done and the more common way is to ignore the
 * Accept-Charset header in this case.
 *
 * In early versions of HTTP/1.1, a default charset (ISO-8859-1) was defined. This is no more the case and now each
 * content type may have its own default
 * </pre>
 */
public class NotAcceptableHeaderException extends HeaderValueException {

    private final static long serialVersionUID = 1L;

    protected NotAcceptableHeaderException() {
        super();
    }

    public NotAcceptableHeaderException(final String message) {
        super(message);
    }

    public NotAcceptableHeaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
