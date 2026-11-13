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

/**
 * An alternative interface to convert a value to text which may have multiple values with the given {@link LineEnding}.
 */
public interface HasMultiLineTextAndCollectionString extends HasMultiLineText,
    Collection<String> {

    /**
     * Returns this value as text with the given {@link HasMultiLineText}.
     */
    @Override
    default MultiLineText multiLineText(final TextContext context) {
        Objects.requireNonNull(context, "context");

        final StringBuilder b = new StringBuilder();

        int i = -1;
        for(String string : this) {
            if(i >= 0) {
                b.append(
                    this.multiLineTextSeparator(
                        i,
                        context
                    )
                );
            }
            i++;

            // escape eols.
            b.append(
                string.replace("\r", "\\r")
                    .replace("\n", "\\n")
            );
        }

        if(i >= 0) {
            b.append(
                this.multiLineTextSeparator(
                    i,
                    context
                )
            );
        }

        return MultiLineText.EMPTY.setText(
            b.toString()
        );
    }

    /**
     * Sub-classes must provide the separator inserted between values for the given nth element.
     */
    CharSequence multiLineTextSeparator(final int i,
                                        final TextContext context);
}
