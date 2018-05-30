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

package walkingkooka.text;

import walkingkooka.io.serialize.SerializationProxy;

/**
 * A {@link SerializationProxy} for any of the {@link Indentation#CONSTANTS} storing only the
 * length.
 */
final class IndentationConstantSerializationProxy implements SerializationProxy {

    // Serializable
    private static final long serialVersionUID = 4128966311126826683L;

    /**
     * Factory only called by {@link Indentation#writeReplace()}.
     */
    static IndentationConstantSerializationProxy with(final int length) {
        return new IndentationConstantSerializationProxy(length);
    }

    /**
     * Private constructor use factory
     */
    private IndentationConstantSerializationProxy(final int length) {
        super();
        this.length = length;
    }

    private final int length;

    private Object readResolve() {
        return Indentation.CONSTANTS[this.length];
    }

    // Object

    @Override
    public String toString() {
        return Indentation.CONSTANTS[this.length].toString();
    }
}
