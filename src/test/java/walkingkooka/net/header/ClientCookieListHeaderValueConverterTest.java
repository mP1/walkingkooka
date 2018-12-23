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

import java.util.List;

public final class ClientCookieListHeaderValueConverterTest extends
        HeaderValueConverterTestCase<ClientCookieListHeaderValueConverter, List<ClientCookie>> {

    @Override
    protected String requiredPrefix() {
        return ClientCookie.class.getSimpleName();
    }

    @Test
    public void testClientCookie() {
        final String header = "cookie123=value456;";
        this.parseAndToTextAndCheck(header, ClientCookie.parseHeader(header));
    }

    @Test
    public void testClientCookie2() {
        final String header = "cookie1=value1; cookie2=value2;";
        this.parseAndToTextAndCheck(header, ClientCookie.parseHeader(header));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesNullFails() {
        this.check(Lists.of(this.cookie(), null));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesWrongTypeFails() {
        this.check(Lists.of(this.cookie(), "WRONG!"));
    }

    private ClientCookie cookie() {
        return Cookie.client(CookieName.with("cookie"), "456");
    }

    @Override
    protected ClientCookieListHeaderValueConverter converter() {
        return ClientCookieListHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<List<ClientCookie>> name() {
        return HttpHeaderName.COOKIE;
    }

    @Override
    protected String invalidHeaderValue() {
        return "";
    }

    @Override
    protected List<ClientCookie> value() {
        return ClientCookie.parseHeader("cookie1=value1; cookie2=value2;");
    }

    @Override
    protected String converterToString() {
        return "List<ClientCookie>";
    }

    @Override
    protected Class<ClientCookieListHeaderValueConverter> type() {
        return ClientCookieListHeaderValueConverter.class;
    }
}
