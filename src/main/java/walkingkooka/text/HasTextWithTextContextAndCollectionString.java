/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An alternative interface to convert a value to text which may have multiple values with the given {@link LineEnding}.
 */
public interface HasTextWithTextContextAndCollectionString extends HasTextWithTextContext,
    Collection<String> {

    /**
     * Returns this value as text with the given {@link HasTextWithTextContext}.
     */
    @Override
    default String textWithTextContext(final TextContext context) {
        Objects.requireNonNull(context, "context");

        final LineEnding lineEnding = context.lineEnding();

        return this.stream()
            .map((String string) -> string.replace("\r", "\\r").replace("\n", "\\n"))
            .collect(
                Collectors.joining(
                    lineEnding,
                    "",
                    lineEnding
                )
            );
    }
}
