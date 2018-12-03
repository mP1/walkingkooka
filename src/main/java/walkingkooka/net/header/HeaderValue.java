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

import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharacterConstant;

/**
 * Contract implemented by header value types.
 */
public interface HeaderValue extends HashCodeEqualsDefined {

    /**
     * The separator character that separates multiple header values.
     */
    CharacterConstant SEPARATOR = CharacterConstant.with(',');

    /**
     * A special name that identifies a wildcard selection.
     */
    CharacterConstant WILDCARD = CharacterConstant.with('*');

    /**
     * Converts this value to its text form.
     */
    String headerValue();
}
