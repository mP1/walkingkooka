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

package walkingkooka.net.http.server.hateos;

import walkingkooka.net.header.LinkRelation;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.Objects;

/**
 * Used as a key within a map holding all mappers.
 */
final class HateosHandlerRouterKey implements HashCodeEqualsDefined, Comparable<HateosHandlerRouterKey> {

    static HateosHandlerRouterKey with(final HateosResourceName resourceName,
                                       final LinkRelation<?> linkRelation) {
        return new HateosHandlerRouterKey(resourceName, linkRelation);
    }

    private HateosHandlerRouterKey(final HateosResourceName resourceName,
                                   final LinkRelation<?> linkRelation) {
        this.resourceName = resourceName;
        this.linkRelation = linkRelation;
    }

    final HateosResourceName resourceName;

    final LinkRelation<?> linkRelation;

    // HashCodeEqualsDefined ...........................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.resourceName, this.linkRelation);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof HateosHandlerRouterKey && this.equals0((HateosHandlerRouterKey) other);
    }

    private boolean equals0(final HateosHandlerRouterKey other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Dumps the resource name and link relation
     */
    @Override
    public String toString() {
        return this.resourceName + " " + this.linkRelation;
    }

    // Comparable........................................................................................................

    @Override
    public int compareTo(final HateosHandlerRouterKey other) {
        int result = this.resourceName.compareTo(other.resourceName);
        if (0 == result) {
            result = this.linkRelation.compareTo(other.linkRelation);
        }
        return result;
    }
}
