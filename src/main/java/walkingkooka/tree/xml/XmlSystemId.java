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

package walkingkooka.tree.xml;

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.search.HasSearchNode;
import walkingkooka.tree.search.SearchNode;

import java.util.Optional;

/**
 * A {@link Value} which is a system id
 */
final public class XmlSystemId implements Value<String>,
        HasSearchNode,
        HashCodeEqualsDefined {

    /**
     * Constant that may be used when no system id is present.
     */
    public final static Optional<XmlSystemId> NO_SYSTEM_ID = Optional.empty();

    /**
     * Factory that creates a {@link XmlSystemId}
     */
    static Optional<XmlSystemId> with(final String value) {
        return null == value ? NO_SYSTEM_ID : Optional.of(new XmlSystemId(value));
    }

    /**
     * package private constructor}.
     */
    private XmlSystemId(final String value) {
        this.value = value;
    }

    // Value

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // toSearchNode...............................................................................................

    @Override
    public SearchNode toSearchNode() {
        return SearchNode.text(this.value, this.value);
    }

    // Object..................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof XmlSystemId) && this.equals0((XmlSystemId) other));
    }

    private boolean equals0(final XmlSystemId other) {
        return this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return CharSequences.quote(this.value).toString();
    }
}
