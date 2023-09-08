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

import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Some very basic CSV utilities. Note no attempt is made to unescape or escape commas within the string.
 */
public final class Csv implements PublicStaticHelper {

    /**
     * The separator.
     */
    public final static CharacterConstant SEPARATOR = CharacterConstant.with(',');

    /**
     * Parses the given {@link String text} into csv delimited components, each component is then parsed.
     * An empty input {@link String} will produce an empty {@link List}.
     */
    public static <T> List<T> parseCsv(final String text,
                                       final Function<String, T> component) {
        Objects.requireNonNull(text, "text");

        final List<T> parsed = Lists.array();

        final int length = text.length();
        int i = 0;
        int start = 0;

        while (i < length) {
            final char c = text.charAt(i);
            if (SEPARATOR.character() == c) {
                parse0(
                        text,
                        start,
                        i,
                        component,
                        parsed
                );

                start = i + 1;
            }
            i++;
        }

        if(start != length) {
            parse0(
                    text,
                    start,
                    length,
                    component,
                    parsed
            );
        }

        return Lists.immutable(parsed);
    }

    private static <T> void parse0(final String text,
                                   final int start,
                                   final int end,
                                   final Function<String, T> component,
                                   final List<T> parsed) {
        try {
            parsed.add(
                    component.apply(text.substring(start, end))
            );
        } catch (final InvalidCharacterException cause) {
            throw cause.setTextAndPosition(
                    text,
                    start + cause.position()
            );
        } catch (final RuntimeException cause) {
            throw new IllegalArgumentException("Unable to parse " + CharSequences.quote(text) + ", " + cause.getMessage(), cause);
        }
    }

    /**
     * THe inverse of the {link #parse}, turns the given values back into a CSV String.
     */
    public static <T> String toCsv(final List<T> values,
                                   final Function<T, String> component) {
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(component, "component");

        return values.stream()
                .map(component)
                .collect(Collectors.joining(SEPARATOR.string()));
    }

    /**
     * Stop creation
     */
    private Csv() {
        throw new UnsupportedOperationException();
    }
}
