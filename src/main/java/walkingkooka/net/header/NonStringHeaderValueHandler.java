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
 * Base handler that provides support for converting header text to values and back where T is not {@link String}.
 */
abstract class NonStringHeaderValueHandler<T> extends HeaderValueHandler<T> {
    /**
     * Package private to limit sub classing.
     */
    NonStringHeaderValueHandler() {
        super();
    }

    /**
     * Reports that the value is not a {@link String}.
     */
    @Override
    final HttpHeaderName<String> httpHeaderNameCast(final HttpHeaderName<?> headerName) {
        throw new HttpHeaderNameTypeParameterHeaderException(headerName.toString());
    }
}
