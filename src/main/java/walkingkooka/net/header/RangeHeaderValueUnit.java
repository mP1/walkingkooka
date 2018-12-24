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

package walkingkooka.net.header;

import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.Optional;

/**
 * The range unit used in headers such as content-range.
 */
public enum RangeHeaderValueUnit implements HeaderValue {

    NONE("none") {
        @Override
        RangeHeaderValueUnit rangeCheck() {
            throw new IllegalArgumentException("Invalid range unit=" + this);
        }
    },

    BYTES("bytes") {
        @Override
        RangeHeaderValueUnit rangeCheck() {
            return this;
        }
    };

    RangeHeaderValueUnit(final String headerText) {
        this.headerText = headerText;
    }

    abstract RangeHeaderValueUnit rangeCheck();

    @Override
    public final String toHeaderText() {
        return this.headerText;
    }

    private final String headerText;

    @Override
    public final boolean isWildcard() {
        return false;
    }

    // HasHeaderScope....................................................................
    @Override
    public final boolean isMultipart() {
        return RangeHeaderValue.IS_MULTIPART;
    }

    @Override
    public final boolean isRequest() {
        return RangeHeaderValue.IS_REQUEST;
    }

    @Override
    public final boolean isResponse() {
        return RangeHeaderValue.IS_RESPONSE;
    }
    /**
     * Finds a matching {@link RangeHeaderValueUnit} for the given text or throw an {@link IllegalArgumentException}.
     */
    public static RangeHeaderValueUnit parse(final String text) {
        final Optional<RangeHeaderValueUnit> found = Arrays.stream(values())
                .filter(u -> u.headerText.equalsIgnoreCase(text))
                .findFirst();
        if(!found.isPresent()){
            throw new IllegalArgumentException("Unknown range unit " + CharSequences.quote(text));
        }
        return found.get();
    }
}
