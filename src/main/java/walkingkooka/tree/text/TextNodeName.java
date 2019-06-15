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
import walkingkooka.text.CaseSensitivity;

/**
 * The name of an {@link TextNode}.
 */
public final class TextNodeName extends TextNodeNameName<TextNodeName> {

    public static TextNodeName with(final String name) {
        checkName(name);
        return new TextNodeName(name);
    }

    static TextNodeName fromClass(final Class<? extends TextNode> klass) {
        final String name = klass.getSimpleName();
        return new TextNodeName(name.substring("Text".length(), name.length() - Name.class.getSimpleName().length()));
    }

    // @VisibleForTesting
    private TextNodeName(final String name) {
        super(name);
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof TextNodeName;
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.SENSITIVE;
}
