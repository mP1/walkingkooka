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

import walkingkooka.net.header.HeaderValue;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Optional;

/**
 * The range unit used in headers such as content-range.
 */
public enum HttpRangeUnit implements HeaderValue {

    NONE("none") {
        @Override
        void httpHeaderRangeCheck() {
            throw new IllegalArgumentException("Invalid range unit=" + this);
        }
    },

    BYTES("bytes") {
        @Override
        void httpHeaderRangeCheck() {
            // acceptable.
        }
    };

    HttpRangeUnit(final String headerText) {
        this.headerText = headerText;
    }

    abstract void httpHeaderRangeCheck();

    @Override
    public String toHeaderText() {
        return this.headerText;
    }

    private final String headerText;

    @Override
    public HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    /**
     * Finds a matching {@link HttpRangeUnit} for the given text or throw an {@link IllegalArgumentException}.
     */
    public static HttpRangeUnit fromHeaderText(final String text) {
        final Optional<HttpRangeUnit> found = Arrays.stream(values())
                .filter(u -> u.headerText.equalsIgnoreCase(text))
                .findFirst();
        if(!found.isPresent()){
            throw new IllegalArgumentException("Unknown range unit " + CharSequences.quote(text));
        }
        return found.get();
    }
}
