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

/**
 * Defines a HTTP response.
 */
public interface HttpResponse {

    /**
     * Sets the response status
     */
    void setStatus(final HttpStatus status);

    /**
     * Adds a new header and its value.
     */
    <T> void addHeader(final HttpHeaderName<T> name, final T value);

    /**
     * Sets the body of the response.
     */
    void setBody(final byte[] body);

    /**
     * Sets text that will be encoded and set upon the body.
     */
    void setBodyText(final String text);
}
