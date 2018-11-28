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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class HttpHeaderValueHttpETagListConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueHttpETagListConverter, List<HttpETag>> {

    @Test
    public void testRequest() {
        this.parseAndCheck("W/\"123\"", Lists.of(HttpETag.with("123", HttpETagValidator.WEAK)));
    }

    @Test
    public void testResponse() {
        this.formatAndCheck(Lists.of(HttpETag.with("123", HttpETagValidator.WEAK)),
                "W/\"123\"");
    }

    @Test
    public void testResponse2() {
        this.formatAndCheck(Lists.of(HttpETag.with("123", HttpETagValidator.WEAK),
                HttpETag.with("456", HttpETagValidator.WEAK)), "W/\"123\", W/\"456\"");
    }

    @Override
    HttpHeaderName<List<HttpETag>> headerOrParameterName() {
        return HttpHeaderName.IF_MATCH;
    }

    @Override
    HttpHeaderValueHttpETagListConverter converter() {
        return HttpHeaderValueHttpETagListConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "I/";
    }

    @Override
    String converterToString() {
        return "List<HttpETag>";
    }

    @Override
    protected Class<HttpHeaderValueHttpETagListConverter> type() {
        return HttpHeaderValueHttpETagListConverter.class;
    }
}
