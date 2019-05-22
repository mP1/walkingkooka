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

import java.util.Map;

/**
 * An accept-encoding with optional parameters.
 */
final class AcceptEncodingNonWildcard extends AcceptEncoding {

    static AcceptEncodingNonWildcard with(final String value, final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return parameters.isEmpty() ?
                maybeConstant(value) :
                new AcceptEncodingNonWildcard(value, parameters);
    }

    private static AcceptEncodingNonWildcard maybeConstant(final String value) {
        final AcceptEncodingNonWildcard acceptEncodings = CONSTANTS.get(value);
        return null != acceptEncodings ?
                acceptEncodings :
                new AcceptEncodingNonWildcard(value, NO_PARAMETERS);
    }

    private AcceptEncodingNonWildcard(final String value, final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    AcceptEncodingNonWildcard replace(final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return new AcceptEncodingNonWildcard(this.value, parameters);
    }

    // Predicate........................................................................................................

    @Override
    boolean test0(final ContentEncoding contentEncoding) {
        return CASE_SENSITIVITY.equals(this.value(), contentEncoding.value());
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof AcceptEncodingNonWildcard;
    }
}
