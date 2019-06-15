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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClientCookieListHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<ClientCookieListHeaderValueHandler, List<ClientCookie>> {

    @Override
    public String typeNamePrefix() {
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

    @Test
    public void testCheckIncludesNullFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.cookie(), null));
        });
    }

    @Test
    public void testCheckIncludesWrongTypeFails() {
        assertThrows(HeaderValueException.class, () -> {
            this.check(Lists.of(this.cookie(), "WRONG!"));
        });
    }

    private ClientCookie cookie() {
        return Cookie.client(CookieName.with("cookie"), "456");
    }

    @Override
    ClientCookieListHeaderValueHandler handler() {
        return ClientCookieListHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<List<ClientCookie>> name() {
        return HttpHeaderName.COOKIE;
    }

    @Override
    String invalidHeaderValue() {
        return "";
    }

    @Override
    List<ClientCookie> value() {
        return ClientCookie.parseHeader("cookie1=value1; cookie2=value2;");
    }

    @Override
    String valueType() {
        return this.listValueType(ClientCookie.class);
    }

    @Override
    String handlerToString() {
        return "List<ClientCookie>";
    }

    @Override
    public Class<ClientCookieListHeaderValueHandler> type() {
        return ClientCookieListHeaderValueHandler.class;
    }
}
