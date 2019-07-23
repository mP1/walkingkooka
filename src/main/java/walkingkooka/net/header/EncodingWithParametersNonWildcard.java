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

import java.util.Map;

/**
 * An accept-encoding encoding with optional parameters.
 */
final class EncodingWithParametersNonWildcard extends EncodingWithParameters {

    static EncodingWithParametersNonWildcard with(final String value, final Map<EncodingParameterName<?>, Object> parameters) {
        return parameters.isEmpty() ?
                maybeConstant(value) :
                new EncodingWithParametersNonWildcard(value, parameters);
    }

    private static EncodingWithParametersNonWildcard maybeConstant(final String value) {
        final EncodingWithParametersNonWildcard acceptEncodings = CONSTANTS.get(value);
        return null != acceptEncodings ?
                acceptEncodings :
                new EncodingWithParametersNonWildcard(value, NO_PARAMETERS);
    }

    private EncodingWithParametersNonWildcard(final String value, final Map<EncodingParameterName<?>, Object> parameters) {
        super(value, parameters);
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    @Override
    EncodingWithParametersNonWildcard replace(final Map<EncodingParameterName<?>, Object> parameters) {
        return new EncodingWithParametersNonWildcard(this.value, parameters);
    }

    // Predicate........................................................................................................

    @Override
    boolean test0(final ContentEncoding contentEncoding) {
        return CASE_SENSITIVITY.equals(this.value(), contentEncoding.value());
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof EncodingWithParametersNonWildcard;
    }
}
