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
import walkingkooka.naming.Name;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AcceptEncodingHeaderValueHandlerTest extends NonStringHeaderValueHandlerTestCase<AcceptEncodingHeaderValueHandler, AcceptEncoding> {

    @Test
    public void testParseToken() {
        this.parseAndCheck2("gzip",
                EncodingWithParameters.GZIP);
    }

    @Test
    public void testParseTokenToken() {
        this.parseAndCheck2("gzip; q=0.5, *",
                EncodingWithParameters.WILDCARD_ENCODING,
                EncodingWithParameters.GZIP.setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f)));
    }

    private void parseAndCheck2(final String text, final EncodingWithParameters... encodings) {
        this.parseAndCheck(text, AcceptEncoding.with(Lists.of(encodings)));
    }

    @Test
    public void testCheckEmptyListFails() {
        assertThrows(HeaderValueException.class, () -> {
            AcceptEncodingHeaderValueHandler.INSTANCE.check(Lists.empty(), HttpHeaderName.ACCEPT_ENCODING);
        });
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(acceptEncoding(EncodingWithParameters.WILDCARD_ENCODING),
                "*");
    }

    @Test
    public void testToText2() {
        this.toTextAndCheck(acceptEncoding(EncodingWithParameters.GZIP),
                "gzip");
    }

    @Test
    public void testToText3() {
        this.toTextAndCheck(acceptEncoding(EncodingWithParameters.WILDCARD_ENCODING, EncodingWithParameters.GZIP),
                "*, gzip");
    }

    @Test
    public void testToTextWithParameters() {
        this.toTextAndCheck(acceptEncoding(EncodingWithParameters.with("abc").setParameters(Maps.of(EncodingParameterName.Q_FACTOR, 0.5f))),
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
    String handlerToString() {
        return AcceptEncoding.class.getSimpleName();
    }

    @Override
    AcceptEncodingHeaderValueHandler handler() {
        return AcceptEncodingHeaderValueHandler.INSTANCE;
    }

    @Override
    AcceptEncoding value() {
        return acceptEncoding(EncodingWithParameters.GZIP, EncodingWithParameters.DEFLATE);
    }

    private static AcceptEncoding acceptEncoding(final EncodingWithParameters...encodings) {
        return AcceptEncoding.with(Lists.of(encodings));
    }

    @Override
    String valueType() {
        return this.valueType(AcceptEncoding.class);
    }

    @Override
    public String typeNamePrefix() {
        return AcceptEncoding.class.getSimpleName();
    }

    @Override
    public Class<AcceptEncodingHeaderValueHandler> type() {
        return AcceptEncodingHeaderValueHandler.class;
    }
}
