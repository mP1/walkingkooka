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

public final class TokenHeaderValueListHeaderValueConverterTest extends
        HeaderValueConverterTestCase<TokenHeaderValueListHeaderValueConverter, List<TokenHeaderValue>> {

    @Override
    protected String requiredPrefix() {
        return TokenHeaderValue.class.getSimpleName();
    }

    @Test
    public void testToken() {
        this.parseAndCheck2("EN", this.en());
    }

    @Test
    public void testTokenCommaToken() {
        this.parseAndCheck2("EN,EN_AU", this.en(), this.en_AU());
    }

    @Test
    public void testTokenCommaWhitespaceToken() {
        this.parseAndCheck2("EN, EN_AU", this.en(), this.en_AU());
    }

    @Test
    public void testTokenWithParameters() {
        this.parseAndCheck2("ES; q=0.5", this.es());
    }

    @Test
    public void testManyTokensSomeWithParameters() {
        // sorted by q factor
        this.parseAndCheck2("ES; q=0.5, EN, FR; q=0.25", this.en(), this.es(), fr());
    }

    @Test
    public void testMultipleSameQFactor() {
        // sorted by q factor
        this.parseAndCheck2("ES; q=0.5, EN, FR; q=0.25, EN_AU, EN_NZ;q=1.0",
                this.en(), this.en_AU(), this.en_NZ(), this.es(), fr());
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
        this.toTextAndCheck(Lists.of(this.en(), this.en_AU()), "EN, EN_AU");
    }

    private void parseAndCheck2(final String headerValue, final TokenHeaderValue... tokens) {
        this.parseAndCheck(headerValue, Lists.of(tokens));
    }

    private TokenHeaderValue en() {
        return TokenHeaderValue.with("EN");
    }

    private TokenHeaderValue en_AU() {
        return TokenHeaderValue.with("EN_AU");
    }

    private TokenHeaderValue en_NZ() {
        return TokenHeaderValue.with("EN_NZ")
                .setParameters(Maps.one(TokenHeaderValueParameterName.Q, 1.0f));
    }

    private TokenHeaderValue es() {
        return TokenHeaderValue.with("ES")
                .setParameters(Maps.one(TokenHeaderValueParameterName.Q, 0.5f));
    }

    private TokenHeaderValue fr() {
        return TokenHeaderValue.with("FR")
                .setParameters(Maps.one(TokenHeaderValueParameterName.Q, 0.25f));
    }

    @Override
    TokenHeaderValueListHeaderValueConverter converter() {
        return TokenHeaderValueListHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<List<TokenHeaderValue>> name() {
        return HttpHeaderName.ACCEPT_ENCODING;
    }

    @Override
    String invalidHeaderValue() {
        return "/////";
    }

    @Override
    List<TokenHeaderValue> value() {
        return Lists.of(this.en(), this.en_AU());
    }

    @Override
    String converterToString() {
        return "List<TokenHeaderValue>";
    }

    @Override
    protected Class<TokenHeaderValueListHeaderValueConverter> type() {
        return TokenHeaderValueListHeaderValueConverter.class;
    }
}
