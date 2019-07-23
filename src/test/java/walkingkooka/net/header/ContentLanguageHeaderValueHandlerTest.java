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
import walkingkooka.naming.Name;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentLanguageHeaderValueHandlerTest extends NonStringHeaderValueHandlerTestCase<ContentLanguageHeaderValueHandler, ContentLanguage> {

    @Test
    public void testParse() {
        this.parseAndCheck("en", ContentLanguage.with(Lists.of(this.en())));
    }

    @Test
    public void testCheckEmptyListFails() {
        assertThrows(HeaderValueException.class, () -> {
            ContentLanguageHeaderValueHandler.INSTANCE.check(Lists.empty(), HttpHeaderName.CONTENT_LANGUAGE);
        });
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(ContentLanguage.with(Lists.of(this.en())), "en");
    }

    @Test
    public void testToText2() {
        this.toTextAndCheck(ContentLanguage.with(Lists.of(this.en(), this.fr())), "en, fr");
    }

    @Override
    Name name() {
        return HttpHeaderName.CONTENT_LANGUAGE;
    }

    @Override
    String invalidHeaderValue() {
        return "\0invalid";
    }

    @Override
    String handlerToString() {
        return ContentLanguage.class.getSimpleName();
    }

    @Override
    ContentLanguageHeaderValueHandler handler() {
        return ContentLanguageHeaderValueHandler.INSTANCE;
    }

    @Override
    ContentLanguage value() {
        return ContentLanguage.with(Lists.of(this.en()));
    }

    @Override
    String valueType() {
        return this.valueType(ContentLanguage.class);
    }

    @Override
    public String typeNamePrefix() {
        return ContentLanguage.class.getSimpleName();
    }

    private LanguageName en() {
        return LanguageName.with("en");
    }

    private LanguageName fr() {
        return LanguageName.with("fr");
    }

    @Override
    public Class<ContentLanguageHeaderValueHandler> type() {
        return ContentLanguageHeaderValueHandler.class;
    }
}
