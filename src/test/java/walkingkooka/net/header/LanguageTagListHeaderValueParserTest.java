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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;

public final class LanguageTagListHeaderValueParserTest extends LanguageTagHeaderValueParserTestCase<LanguageTagListHeaderValueParser,
        List<LanguageTag>> {

    @Test
    public void testMultipleLanguages() {
        this.parseAndCheck3("en-US,en;q=0.5",
                this.tag("en-US"),
                this.tag("en", 0.5f));
    }

    @Test
    public void testMultipleLanguagesSorted() {
        this.parseAndCheck3("de;q=0.75,fr;q=0.25,en;q=0.5",
                this.tag("de", 0.75f),
                this.tag("en", 0.5f),
                this.tag("fr", 0.25f));
    }

    private LanguageTag tag(final String tag, final float qFactor) {
        return this.tag(tag)
                .setParameters(Maps.of(LanguageTagParameterName.Q_FACTOR, qFactor));
    }

    private LanguageTag tag(final String tag) {
        return LanguageTag.with(LanguageTagName.with(tag));
    }

    private void parseAndCheck3(final String text, final LanguageTag... tags) {
        this.parseAndCheck(text, Lists.of(tags));
    }

    @Override
    void parseAndCheck2(final String text, final LanguageTag tag) {
        this.parseAndCheck3(text, tag);
    }

    @Override
    public List<LanguageTag> parse(final String text) {
        return listReadOnlyCheck(LanguageTagListHeaderValueParser.parseList(text));
    }

    @Override
    public Class<LanguageTagListHeaderValueParser> type() {
        return LanguageTagListHeaderValueParser.class;
    }
}
