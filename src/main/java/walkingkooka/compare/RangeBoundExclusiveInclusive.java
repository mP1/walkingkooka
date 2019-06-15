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

package walkingkooka.compare;

import walkingkooka.Cast;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import java.util.Optional;

/**
 * Base class for both exclusive and inclusive {@link RangeBound}.
 */
abstract class RangeBoundExclusiveInclusive<C extends Comparable> extends RangeBound<C> {

    /**
     * Package private
     */
    RangeBoundExclusiveInclusive(final C value) {
        super();
        this.value = value;
    }

    /**
     * Always false
     */
    @Override
    public final boolean isAll() {
        return false;
    }

    /**
     * The value.
     */
    @Override
    public final Optional<C> value() {
        return Optional.of(this.value);
    }

    // Range.toJsonNode......................................................................

    @Override
    final JsonObjectNode toJsonNode() {
        return JsonObjectNode.object()
                .set(this.property(), HasJsonNode.toJsonNodeWithType(this.value));
    }

    abstract JsonNodeName property();

    // Object....................................................................

    @Override
    public final int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                this.canBeEquals(other) &&
                        this.equals0(Cast.to(other));
    }

    abstract boolean canBeEquals(final Object other);

    private boolean equals0(final RangeBoundExclusiveInclusive<?> other) {
        return this.value.equals(other.value);
    }

    @Override
    public final String toString() {
        return this.label() + ": " + this.value;
    }

    final C value;

    abstract String label();
}
