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

public final class ContentLanguageHeaderValueParserTest extends HeaderValueParserTestCase<ContentLanguageHeaderValueParser, ContentLanguage> {

    @Test
    public void testWhitespaceFails() {
        this.parseMissingValueFails("  ");
    }

    @Test
    public void testCommentFails() {
        this.parseCommentFails("(abc)", 0);
    }

    @Test
    public void testParametersFails() {
        this.parseInvalidCharacterFails("en;x=1", ';');
    }

    @Test
    public void testQuotedTextFails() {
        this.parseInvalidCharacterFails("\"hello\"", 0);
    }

    @Test
    public void testSlashFails() {
        this.parseInvalidCharacterFails("ab/c", '/');
    }

    @Test
    public void testWildcardFails() {
        this.parseInvalidCharacterFails("*", '*');
    }

    @Test
    public void testTokenCommaWildcardFails() {
        this.parseInvalidCharacterFails("abc, *, def", '*');
    }

    @Test
    public void testTokenCommentFails() {
        this.parseCommentFails("en(abc)", 2);
    }

    @Test
    public void testTokenSeparatorFails() {
        this.parseInvalidCharacterFails("abc;", ';');
    }

    @Test
    public void testToken() {
        this.parseAndCheck2("en",
                this.en());
    }

    @Test
    public void testTokenWhitespace() {
        this.parseAndCheck2("en ",
                this.en());
    }

    @Test
    public void testWhitespaceToken() {
        this.parseAndCheck2(" en",
                this.en());
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("en,fr",
                this.en(),
                this.fr());
    }

    @Test
    public void testTokenWhitespaceCommaWhitespaceTokenCommaWhitespaceToken() {
        this.parseAndCheck2("en, fr,  gr",
                this.en(),
                this.fr(),
                this.gr());
    }

    private LanguageName en() {
        return LanguageName.with("en");
    }

    private LanguageName fr() {
        return LanguageName.with("fr");
    }

    private LanguageName gr() {
        return LanguageName.with("gr");
    }

    private void parseAndCheck2(final String text, final LanguageName...languages) {
        this.parseAndCheck(text, ContentLanguage.with(Lists.of(languages)));
    }

    @Override
    public ContentLanguage parse(final String text) {
        return ContentLanguageHeaderValueParser.parseContentLanguage(text);
    }

    @Override
    String valueLabel() {
        return HttpHeaderName.CONTENT_LANGUAGE.toString();
    }

    @Override
    public Class<ContentLanguageHeaderValueParser> type() {
        return ContentLanguageHeaderValueParser.class;
    }
}
