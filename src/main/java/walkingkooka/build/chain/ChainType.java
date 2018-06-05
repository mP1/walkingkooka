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

package walkingkooka.build.chain;

import walkingkooka.collect.map.Maps;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.Whitespace;

import java.io.Serializable;
import java.util.Map;

/**
 * A type of {@link Chained}. Note no behaviour is defined this simply represents different types.
 */
final public class ChainType implements HashCodeEqualsDefined, Serializable {

    // constants

    /**
     * A read only cache of already prepared {@link ChainType types}.
     */
    final static Map<String, ChainType> CONSTANTS = Maps.sorted();

    /**
     * Creates and adds a new {@link ChainType} to the cache being built.
     */
    static ChainType registerConstant(final String type) {
        final ChainType chainType = new ChainType(type);
        ChainType.CONSTANTS.put(type, chainType);
        return chainType;
    }

    /**
     * A {@link ChainType} with <code>ALL</code>. All in the chain will be called/used.
     */
    public final static ChainType ALL = ChainType.registerConstant("ALL");

    /**
     * A {@link ChainType} with <code>FIRST</code>. When the chain is invoked typically it stops
     * upon the first successful.
     */
    public final static ChainType FIRST = ChainType.registerConstant("FIRST");

    /**
     * Creates a {@link ChainType} which should not be null or empty.
     */
    public static ChainType with(final String type) {
        Whitespace.failIfNullOrWhitespace(type, "type");

        final ChainType chainType = ChainType.CONSTANTS.get(type);
        return null != chainType ? chainType : new ChainType(type);
    }

    /**
     * Private constructor
     */
    private ChainType(final String type) {
        super();
        this.type = type;
    }

    final String type;

    // Object

    @Override
    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return (this == other) || ((other instanceof ChainType) && this.equals0((ChainType) other));
    }

    private boolean equals0(final ChainType other) {
        return this.type.equals(other.type);
    }

    @Override
    public String toString() {
        return this.type;
    }

    // Serializable

    private static final long serialVersionUID = 4487133789635235129L;

    private Object readResolve() {
        final ChainType chainType = ChainType.CONSTANTS.get(this.type);
        return null != chainType ? chainType : this;
    }
}
