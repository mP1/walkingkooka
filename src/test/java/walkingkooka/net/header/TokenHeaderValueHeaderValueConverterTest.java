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

public final class TokenHeaderValueHeaderValueConverterTest extends
        HeaderValueConverterTestCase<TokenHeaderValueHeaderValueConverter, TokenHeaderValue> {

    @Override
    protected String requiredPrefix() {
        return TokenHeaderValue.class.getSimpleName();
    }

    @Test
    public void testToken() {
        this.parseAndCheck2("EN", this.en());
    }

    @Test
    public void testTokenWithQParameter() {
        this.parseAndCheck2("EN; q=0.5",
                this.en().setParameters(Maps.one(TokenHeaderValueParameterName.Q, 0.5f)));
    }


    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesNullFails() {
        this.check(Lists.of(en(), null));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesWrongTypeFails() {
        this.check(Lists.of(en(), "WRONG!"));
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(this.en(), "EN");
    }

    private void parseAndCheck2(final String headerValue, final TokenHeaderValue token) {
        this.parseAndCheck(headerValue, token);
    }

    private TokenHeaderValue en() {
        return TokenHeaderValue.with("EN");
    }

    @Override
    protected TokenHeaderValueHeaderValueConverter converter() {
        return TokenHeaderValueHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<TokenHeaderValue> name() {
        return null;
    }

    @Override
    protected String invalidHeaderValue() {
        return "/////";
    }

    @Override
    protected TokenHeaderValue value() {
        return this.en();
    }

    @Override
    protected String converterToString() {
        return "TokenHeaderValue";
    }

    @Override
    protected Class<TokenHeaderValueHeaderValueConverter> type() {
        return TokenHeaderValueHeaderValueConverter.class;
    }
}
