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
package walkingkooka.text.cursor.parser;

import walkingkooka.Value;

import java.util.List;

/**
 * An interface implemented by container type tokens that hold a list of other tokens. This is particularly useful
 * for tokens that are defined as recursive often including themselves or similar containers. An example of this is
 * may be found in the definition of concatenation at <a href="https://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_form"></a>
 */
public interface SupportsFlat<P extends SupportsFlat<P, T>, T extends ParserToken> extends Value<List<T>> {

    /**
     * Return a new {@link ParserToken} if necessary with all tokens of type {@link SupportsFlat} flattened into a single
     * list rather than a nested form.
     */
    P flat();
}
