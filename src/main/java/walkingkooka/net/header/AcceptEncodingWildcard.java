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
 * An accept-encoding wildcard.
 */
final class AcceptEncodingWildcard extends AcceptEncoding {

    /**
     * Singleton without parameters/
     */
    final static AcceptEncodingWildcard INSTANCE = new AcceptEncodingWildcard(NO_PARAMETERS);

    static AcceptEncodingWildcard with(final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return new AcceptEncodingWildcard(parameters);
    }

    private AcceptEncodingWildcard(final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        super("*", parameters);
    }

    @Override
    AcceptEncoding replace(final Map<AcceptEncodingParameterName<?>, Object> parameters) {
        return new AcceptEncodingWildcard(parameters);
    }

    @Override
    public boolean isWildcard() {
        return true;
    }

    // Predicate........................................................................................................

    @Override
    boolean test0(final ContentEncoding contentEncoding) {
        return true;
    }

    // Object..........................................................................................................

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof AcceptEncodingWildcard;
    }
}
