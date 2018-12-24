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

import org.junit.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.List;

public final class LanguageTagListHeaderParserTest extends LanguageTagHeaderParserTestCase<LanguageTagListHeaderParser,
        List<LanguageTag>> {

    @Test
    public void testMultipleLanguages() {
        this.parseAndCheck3("en-US,en;q=0.5",
                LanguageTag.with("en-US"),
                LanguageTag.with("en").setParameters(Maps.one(LanguageTagParameterName.Q_FACTOR, 0.5f)));
    }

    private void parseAndCheck3(final String text, final LanguageTag...tags) {
        this.parseAndCheck(text, Lists.of(tags));
    }

    @Override
    void parseAndCheck2(final String text, final LanguageTag tag) {
        this.parseAndCheck3(text, tag);
    }

    @Override
    List<LanguageTag> parse(final String text) {
        return LanguageTagListHeaderParser.parseList(text);
    }

    @Override
    protected Class<LanguageTagListHeaderParser> type() {
        return LanguageTagListHeaderParser.class;
    }
}
