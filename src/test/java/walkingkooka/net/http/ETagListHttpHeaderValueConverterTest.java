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
import walkingkooka.net.header.HeaderValueException;

import java.util.List;

public final class ETagListHttpHeaderValueConverterTest extends
        HttpHeaderValueConverterTestCase<ETagListHttpHeaderValueConverter, List<ETag>> {

    @Override
    protected String requiredPrefix() {
        return ETag.class.getSimpleName();
    }

    @Test
    public void testParseETagOne() {
        this.parseAndToTextAndCheck("W/\"123\"",
                Lists.of(ETag.with("123", ETagValidator.WEAK)));
    }

    @Test
    public void testParseETagSeveral() {
        this.toTextAndCheck(Lists.of(ETag.with("123", ETagValidator.WEAK),
                ETag.with("456", ETagValidator.WEAK)), "W/\"123\", W/\"456\"");
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesNullFails() {
        this.check(Lists.of(this.etag(), null));
    }

    @Test(expected = HeaderValueException.class)
    public void testCheckIncludesWrongTypeFails() {
        this.check(Lists.of(this.etag(), "WRONG!"));
    }

    private ETag etag() {
        return ETag.with("value", ETagValidator.WEAK);
    }

    @Override
    protected ETagListHttpHeaderValueConverter converter() {
        return ETagListHttpHeaderValueConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<List<ETag>> name() {
        return HttpHeaderName.IF_MATCH;
    }

    @Override
    protected String invalidHeaderValue() {
        return "I/";
    }

    @Override
    protected List<ETag> value() {
        return ETag.parseList("\"1\",\"2\"");
    }

    @Override
    protected String converterToString() {
        return "List<ETag>";
    }

    @Override
    protected Class<ETagListHttpHeaderValueConverter> type() {
        return ETagListHttpHeaderValueConverter.class;
    }
}
