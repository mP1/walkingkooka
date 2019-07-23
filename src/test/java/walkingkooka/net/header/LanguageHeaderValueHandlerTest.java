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
import walkingkooka.collect.map.Maps;

public final class LanguageHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<LanguageHeaderValueHandler, Language> {

    @Override
    public String typeNamePrefix() {
        return Language.class.getSimpleName();
    }

    @Test
    public void testRoundtrip() {
        this.parseAndToTextAndCheck("en; q=0.5",
                Language.with(LanguageName.with("en"))
                        .setParameters(Maps.of(LanguageParameterName.Q_FACTOR, 0.5f)));
    }

    @Override
    LanguageHeaderValueHandler handler() {
        return LanguageHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<Language> name() {
        return HttpHeaderName.CONTENT_LANGUAGE;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    Language value() {
        return Language.parse("en; q=0.5");
    }

    @Override
    String valueType() {
        return this.valueType(Language.class);
    }

    @Override
    String handlerToString() {
        return Language.class.getSimpleName();
    }

    @Override
    public Class<LanguageHeaderValueHandler> type() {
        return LanguageHeaderValueHandler.class;
    }
}
