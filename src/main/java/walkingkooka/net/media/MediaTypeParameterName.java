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

package walkingkooka.net.media;

import walkingkooka.Cast;
import walkingkooka.naming.Name;
import walkingkooka.test.HashCodeEqualsDefined;

import java.io.Serializable;


/**
 * The {@link Name} of an optional parameter accompanying a {@link MediaType}. Note the name may not contain the whitespace, equals sign, semi colons
 * or commas.
 */
final public class MediaTypeParameterName implements Name, Comparable<MediaTypeParameterName>, HashCodeEqualsDefined, Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * Holds the charset parameter name.
     */
    public final static MediaTypeParameterName CHARSET = MediaTypeParameterName.with("charset");

    /**
     * The q factor weight parameter.
     */
    public final static MediaTypeParameterName Q_FACTOR = MediaTypeParameterName.with("q");

    /**
     * Factory that creates a {@link MediaTypeParameterName}
     */
    public static MediaTypeParameterName with(final String value) {
        MediaType.check(value, "value");

        return new MediaTypeParameterName(value);
    }

    /**
     * Private ctor use factory.
     */
    private MediaTypeParameterName(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    // Comparable

    @Override
    public int compareTo(final MediaTypeParameterName name) {
        return this.value.compareTo(name.value());
    }

    // Object

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof MediaTypeParameterName &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final MediaTypeParameterName other) {
        return this.value.equals(other.value);
    }

    /**
     * Dumps the request raw name
     */
    @Override
    public String toString() {
        return this.value;
    }
}
