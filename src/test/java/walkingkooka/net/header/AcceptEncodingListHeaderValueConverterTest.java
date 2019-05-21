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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptEncodingListHeaderValueConverterTest extends NonStringHeaderValueConverterTestCase<AcceptEncodingListHeaderValueConverter, List<AcceptEncoding>> {

    @Test
    public void testParseToken() {
        this.parseAndCheck2("gzip",
                AcceptEncoding.GZIP);
    }

    @Test
    public void testParseTokenToken() {
        this.parseAndCheck2("gzip; q=0.5, *",
                AcceptEncoding.GZIP.setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f)),
                AcceptEncoding.WILDCARD_ACCEPT_ENCODING);
    }

    private void parseAndCheck2(final String text, final AcceptEncoding... encodings) {
        this.parseAndCheck(text, Lists.of(encodings));
    }

    @Test
    public void testCheckEmptyListFails() {
        assertThrows(HeaderValueException.class, () -> {
            AcceptEncodingListHeaderValueConverter.INSTANCE.check(Lists.empty(), HttpHeaderName.ACCEPT_ENCODING);
        });
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(Lists.of(AcceptEncoding.WILDCARD_ACCEPT_ENCODING),
                "*");
    }

    @Test
    public void testToText2() {
        this.toTextAndCheck(Lists.of(AcceptEncoding.GZIP),
                "gzip");
    }

    @Test
    public void testToText3() {
        this.toTextAndCheck(Lists.of(AcceptEncoding.WILDCARD_ACCEPT_ENCODING, AcceptEncoding.GZIP),
                "*, gzip");
    }

    @Test
    public void testToTextWithParameters() {
        this.toTextAndCheck(Lists.of(AcceptEncoding.with("abc").setParameters(Maps.of(AcceptEncodingParameterName.Q_FACTOR, 0.5f))),
                "abc; q=0.5");
    }

    @Override
    Name name() {
        return HttpHeaderName.ACCEPT_ENCODING;
    }

    @Override
    String invalidHeaderValue() {
        return "\0invalid";
    }

    @Override
    String converterToString() {
        return AcceptEncoding.class.getSimpleName();
    }

    @Override
    AcceptEncodingListHeaderValueConverter converter() {
        return AcceptEncodingListHeaderValueConverter.INSTANCE;
    }

    @Override
    List<AcceptEncoding> value() {
        return Lists.of(AcceptEncoding.GZIP, AcceptEncoding.DEFLATE);
    }

    @Override
    String valueType() {
        return this.listValueType(AcceptEncoding.class);
    }

    @Override
    public String typeNamePrefix() {
        return AcceptEncoding.class.getSimpleName();
    }

    @Override
    public Class<AcceptEncodingListHeaderValueConverter> type() {
        return AcceptEncodingListHeaderValueConverter.class;
    }
}
