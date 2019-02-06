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

public final class LanguageTagOneHeaderParserTest extends LanguageTagHeaderParserTestCase<LanguageTagOneHeaderParser, LanguageTag> {

    @Test
    public void testLanguageTagValueSeparatorFails() {
        this.parseInvalidCharacterFails("en,");
    }

    @Override
    void parseAndCheck2(final String text, final LanguageTag expected) {
        this.parseAndCheck(text, expected);
    }

    @Override
    public LanguageTag parse(final String text) {
        return LanguageTagOneHeaderParser.parseOne(text);
    }

    @Override
    public Class<LanguageTagOneHeaderParser> type() {
        return LanguageTagOneHeaderParser.class;
    }
}
