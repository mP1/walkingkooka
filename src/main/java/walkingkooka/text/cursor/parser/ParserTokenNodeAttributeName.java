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

/**
 * Represents an attribute belonging to a {@link ParserTokenNode}
 */
public final class ParserTokenNodeAttributeName implements Name {

    /**
     * Attribute used to retrieve the text property from a {@link ParserToken} wrapped in a node.
     */
    public final static ParserTokenNodeAttributeName TEXT = new ParserTokenNodeAttributeName("text");

    /**
     * Package private ctor to limit creation.
     */
    ParserTokenNodeAttributeName(final String value) {
        this.value = value;
    }

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
        return this == other || other instanceof ParserTokenNodeAttributeName && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ParserTokenNodeAttributeName other) {
        return this.value().equals(other.value());
    }

    @Override
    public String toString() {
        return this.value();
    }
}
