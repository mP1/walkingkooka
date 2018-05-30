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
 * A {@link SerializationProxy} for anything not a {@link Indentation#CONSTANTS} storing only the
 * value.
 */
final class IndentationSerializationProxy implements SerializationProxy {

    // Serializable
    private static final long serialVersionUID = 4276334733408700270L;

    /**
     * Factory only called by {@link Indentation#writeReplace()}.
     */
    static IndentationSerializationProxy with(final String value) {
        return new IndentationSerializationProxy(value);
    }

    /**
     * Private constructor use factory
     */
    private IndentationSerializationProxy(final String value) {
        super();
        this.value = value;
    }

    private final String value;

    private Object readResolve() {
        return new Indentation(this.value);
    }

    // Object

    @Override
    public String toString() {
        return this.value;
    }
}
