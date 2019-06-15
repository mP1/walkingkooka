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

package walkingkooka.tree.text;

import walkingkooka.naming.Name;
import walkingkooka.naming.NameTesting2;
import walkingkooka.type.JavaVisibility;

public abstract class TextNodeNameNameTestCase<N extends Name & Comparable<N>> extends TextNodeTestCase<N>
        implements NameTesting2<N, N> {

    TextNodeNameNameTestCase() {
        super();
    }

    @Override
    public final String nameText() {
        return "xyz";
    }

    @Override
    public final String differentNameText() {
        return "different";
    }

    @Override
    public final String nameTextLess() {
        return "before";
    }

    @Override
    public final int minLength() {
        return 1;
    }

    @Override
    public final int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public final String possibleValidChars(final int position) {
        return 0 == position ?
                ASCII_LETTERS :
                ASCII_LETTERS_DIGITS + "-.";
    }

    @Override
    public final String possibleInvalidChars(final int position) {
        return 0 == position ?
                CONTROL + ASCII_DIGITS :
                CONTROL;
    }

    // ClassTesting.....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Text";
    }
}