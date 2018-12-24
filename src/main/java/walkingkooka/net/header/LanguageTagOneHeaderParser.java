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

/**
 * A parser that knows how to parse either a wildcard or language tag with or without parameters.
 */
final class LanguageTagOneHeaderParser extends LanguageTagHeaderParser {

    static LanguageTag parseOne(final String text) {
        final LanguageTagOneHeaderParser parser = new LanguageTagOneHeaderParser(text);
        parser.parse();
        return parser.languageTag;
    }

    private LanguageTagOneHeaderParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return false;
    }

    @Override
    void valueComplete(final LanguageTag languageTag) {
        this.languageTag = languageTag;
    }

    private LanguageTag languageTag;
}
