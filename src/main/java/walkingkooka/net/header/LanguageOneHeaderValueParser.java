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

package walkingkooka.net.header;

/**
 * A parser that knows how to parse either a wildcard or language with or without parameters.
 */
final class LanguageOneHeaderValueParser extends LanguageHeaderValueParser {

    static Language parseOne(final String text) {
        final LanguageOneHeaderValueParser parser = new LanguageOneHeaderValueParser(text);
        parser.parse();
        return parser.languageTag;
    }

    private LanguageOneHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return false;
    }

    @Override
    void valueComplete(final Language languageTag) {
        this.languageTag = languageTag;
    }

    private Language languageTag;
}
