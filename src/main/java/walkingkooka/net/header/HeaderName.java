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
import walkingkooka.test.HashCodeEqualsDefined;

/**
 * A header name including support methods to convert text to values and back. This interface applies to both
 * header and parameter names.
 */
public interface HeaderName<T> extends Name, HashCodeEqualsDefined {

    /**
     * Converts the header text to the value.
     */
    T toValue(final String text);

    /**
     * Validates the value and returns it as its type.
     */
    T checkValue(final Object value);
}
