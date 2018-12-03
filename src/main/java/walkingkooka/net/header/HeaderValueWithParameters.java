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

import walkingkooka.text.CharacterConstant;

import java.util.Map;

/**
 * Defines a method to retrieve the parameters from a header.
 */
public interface HeaderValueWithParameters<N extends HeaderParameterName<?>> extends HeaderValue{

    /**
     * The separator between parameter name and value.
     */
    CharacterConstant PARAMETER_NAME_VALUE_SEPARATOR = CharacterConstant.with('=');

    /**
     * The separator character that separates parameters belonging to a header value.
     */
    CharacterConstant PARAMETER_SEPARATOR = CharacterConstant.with(';');

    /**
     * A read only map view of all parameters.
     */
    Map<N, Object> parameters();

    /**
     * Would be setter that returns a {@link HeaderValueWithParameters} creating a new instance if the parameters
     * are different.
     */
    HeaderValueWithParameters<N> setParameters(final Map<N, Object> parameters);
}
