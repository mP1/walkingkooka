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

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * A converter that converts between a {@link String text} and a type.
 */
public interface HeaderValueConverter<T> {

    /**
     * Parses the given text into a typed value.
     */
    T parse(final String value, final Name name);

    /**
     * Formats the value into its header or text equivalent.
     */
    String format(final T value, final Name name);

    /**
     * Only if the type parameter is string should this return true.
     */
    boolean isString();
}
