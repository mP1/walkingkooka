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
package walkingkooka.text.cursor.parser;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.predicate.Predicates;

import java.util.Objects;

/**
 * Represents the (simple) class name of a {@link ParserTokenNode}
 */
public final class ParserTokenNodeName implements Name {

    /**
     * Factory that creates a {@link ParserTokenNodeName} given a {@link Class ParserToken class}.
     */
    public static ParserTokenNodeName fromClass(final Class<? extends ParserToken> token) {
        Objects.requireNonNull(token, "token");

        final String name = token.getSimpleName();

        // chop ParserToken
        return new ParserTokenNodeName(name.endsWith(ENDING) ?
                name.substring(0, name.length() - ENDING.length()) :
                name);
    }

    private final static String ENDING = ParserToken.class.getSimpleName();

    /**
     * Factory that creates a {@link ParserTokenNodeName}. Note the characters used by also be valid java identifier characters.
     */
    public static ParserTokenNodeName with(final String value) {
        Predicates.failIfNullOrFalse(value, Predicates.javaIdentifier(), "Name is invalid name %s");

        return new ParserTokenNodeName(value);
    }

    /**
     * Creates a new {@link ParserTokenNodeName} with an index.
     */
    public static ParserTokenNodeName with(final int index) {
        if(index < 0) {
            throw new IllegalArgumentException("Index " + index + " must not be negative");
        }
        return new ParserTokenNodeName(index);
    }

    /**
     * Package private ctor to limit creation.
     */
    ParserTokenNodeName(final String value) {
        this(value, -1);
    }

    /**
     * Package private ctor to limit creation.
     */
    ParserTokenNodeName(final int index) {
        this(String.valueOf(index), index);
    }

    /**
     * Private ctor to limit creation.
     */
    private ParserTokenNodeName(final String value, final int index) {
        this.value = value;
        this.index = index;
    }

    int index() {
        return this.index;
    }

    private final int index;

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    @Override
    public int hashCode() {
        return this.value().hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof ParserTokenNodeName && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ParserTokenNodeName other) {
        return this.value().equals(other.value());
    }

    @Override
    public String toString() {
        return this.value();
    }
}
