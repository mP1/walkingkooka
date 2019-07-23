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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

public final class AcceptLanguageHeaderValueParserTest extends AcceptLanguageOrLanguageHeaderValueParserTestCase<AcceptLanguageHeaderValueParser,
        AcceptLanguage> {

    @Test
    public void testMultipleLanguages() {
        this.parseAndCheck3("en-US,en;q=0.5",
                this.language("en-US"),
                this.language("en", 0.5f));
    }

    @Test
    public void testMultipleLanguagesSorted() {
        this.parseAndCheck3("de;q=0.75,fr;q=0.25,en;q=0.5",
                this.language("de", 0.75f),
                this.language("en", 0.5f),
                this.language("fr", 0.25f));
    }

    private LanguageWithParameters language(final String language, final float qFactor) {
        return this.language(language)
                .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, qFactor));
    }

    private LanguageWithParameters language(final String language) {
        return LanguageWithParameters.with(LanguageName.with(language));
    }

    private void parseAndCheck3(final String text,
                                final LanguageWithParameters... languages) {
        this.parseAndCheck(text, AcceptLanguage.with(Lists.of(languages)));
    }

    @Override
    void parseAndCheck2(final String text, final LanguageWithParameters language) {
        this.parseAndCheck3(text, language);
    }

    @Override
    public AcceptLanguage parse(final String text) {
        return AcceptLanguageHeaderValueParser.parseAcceptLanguage(text);
    }

    @Override
    public Class<AcceptLanguageHeaderValueParser> type() {
        return AcceptLanguageHeaderValueParser.class;
    }
}
