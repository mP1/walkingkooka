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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptLanguageHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<AcceptLanguageHeaderValueHandler, AcceptLanguage> {

    private final static String TEXT = "en; q=1.0, en-AU; q=0.5";

    @Test
    public void testContentType() {
        this.parseAndToTextAndCheck(TEXT,
                AcceptLanguage.with(Lists.of(this.en_10(), this.en_au_05())));
    }

    @Test
    public void testCheckIncludesNullFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.en_10(), null));
        });
    }

    @Test
    public void testCheckIncludesWrongTypeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.en_10(), "WRONG!"));
        });
    }

    private LanguageWithParameters en_10() {
        return LanguageWithParameters.with(LanguageName.with("en"))
                .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 1.0f));
    }

    private LanguageWithParameters en_au_05() {
        return LanguageWithParameters.with(LanguageName.with("en-au"))
                .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.5f));
    }

    @Override
    AcceptLanguageHeaderValueHandler handler() {
        return AcceptLanguageHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<AcceptLanguage> name() {
        return HttpHeaderName.ACCEPT_LANGUAGE;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    AcceptLanguage value() {
        return AcceptLanguage.parse(TEXT);
    }

    @Override
    String valueType() {
        return this.valueType(AcceptLanguage.class);
    }

    @Override
    String handlerToString() {
        return AcceptLanguage.class.getSimpleName();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<AcceptLanguageHeaderValueHandler> type() {
        return AcceptLanguageHeaderValueHandler.class;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }
}
