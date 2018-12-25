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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.HasQFactorWeight;

import java.util.List;

/**
 * A parser that knows how to parse either one or more wildcard or language tag with or without parameters.
 */
final class LanguageTagListHeaderParser extends LanguageTagHeaderParser {

    static List<LanguageTag> parseList(final String text) {
        final LanguageTagListHeaderParser parser = new LanguageTagListHeaderParser(text);
        parser.parse();

        final List<LanguageTag> languageTags = parser.languageTags;
        languageTags.sort(HasQFactorWeight.qFactorDescendingComparator());
        return Lists.readOnly(languageTags);
    }

    private LanguageTagListHeaderParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return true;
    }

    @Override
    void valueComplete(final LanguageTag languageTag) {
        this.languageTags.add(languageTag);
    }

    private final List<LanguageTag> languageTags = Lists.array();
}
