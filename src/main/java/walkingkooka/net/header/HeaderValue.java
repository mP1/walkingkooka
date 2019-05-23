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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Contract implemented by header value types.
 */
public interface HeaderValue extends HashCodeEqualsDefined, HasHeaderScope {

    /**
     * Remove any comments from text. If the text contains an unterminated comment an exception will be thrown.
     */
    static String removeComments(final String text) {
        return CommentRemovingHeaderValueParser.removeComments(text);
    }

    /**
     * Builds a header value representation of a list of headerValues.
     */
    static <H extends HeaderValue> String toHeaderTextList(final List<H> headerValues,
                                                           final String separator) {
        Objects.requireNonNull(headerValues, "headerValues");
        Objects.requireNonNull(separator, "separator");

        return headerValues.stream()
                .map(t -> t.toHeaderText())
                .collect(Collectors.joining(separator));
    }

    /**
     * The separator character that separates multiple header values.
     */
    CharacterConstant SEPARATOR = CharacterConstant.with(',');

    /**
     * A special name that identifies a wildcard selection.
     */
    CharacterConstant WILDCARD = CharacterConstant.with('*');

    /**
     * The separator between parameter name and value.
     */
    CharacterConstant PARAMETER_NAME_VALUE_SEPARATOR = CharacterConstant.with('=');

    /**
     * The separator character that separates parameters belonging to a header value.
     */
    CharacterConstant PARAMETER_SEPARATOR = CharacterConstant.with(';');

    /**
     * Converts this value to its text form.
     */
    String toHeaderText();

    /**
     * Returns true only if this is a wildcard.
     */
    boolean isWildcard();
}
