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
 */

package walkingkooka.naming;


import walkingkooka.Cast;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.CharSequences;
import walkingkooka.text.ShouldBeQuoted;
import walkingkooka.text.Whitespace;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple {@link Name} that accepts a {@link String} composed of any character.
 */
final public class StringName implements Name,
        Comparable<StringName>,
        ShouldBeQuoted,
        Serializable,
        HasJsonNode {

    private final static long serialVersionUID = 1L;

    /**
     * Root singleton
     */
    final static StringName ROOT = new StringName("");

    /**
     * Factory that creates a {@link StringName}, only the root path has this name.
     */
    static StringName with(final String name) {
        Whitespace.failIfNullOrEmptyOrWhitespace(name, "name");
        if (-1 != name.indexOf(StringPath.SEPARATOR.character())) {
            throw new IllegalArgumentException("Name " + CharSequences.quote(name) +
                    " cannot contain " + CharSequences.quoteIfChars(StringPath.SEPARATOR.character()));
        }

        return new StringName(name);
    }

    private StringName(final String name) {
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Serializable

    /**
     * Ensures singleton instance of any {@link StringName#ROOT}.
     */
    private Object readResolve() {
        return this.name.equals(StringName.ROOT.name) ? StringName.ROOT : this;
    }

    // Object..................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof StringName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final StringName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return CharSequences.quote(this.name).toString();
    }

    // Comparable ...................................................................................................

    @Override
    public int compareTo(final StringName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;

    // HasJsonNode...................................................................................................

    static {
        HasJsonNode.register("string-name",
                StringName::fromJsonNode,
                StringName.class);
    }

    public static StringName fromJsonNode(final JsonNode node) {
        Objects.requireNonNull(node, "node");

        return with(node.stringValueOrFail());
    }

    @Override
    public JsonNode toJsonNode() {
        return JsonNode.string(this.value());
    }
}
