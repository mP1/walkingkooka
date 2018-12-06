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

import walkingkooka.naming.Name;

/**
 * Represents an attribute belonging to a {@link ParserTokenNode}
 */
public enum ParserTokenNodeAttributeName implements Name {

    /**
     * Attribute used to retrieve the text property from a {@link ParserToken} wrapped in a node.
     */
    TEXT("text");

    /**
     * Use constant.
     */
    ParserTokenNodeAttributeName(final String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return this.value;
    }

    private final String value;

    @Override
    public String toString() {
        return this.value();
    }
}
