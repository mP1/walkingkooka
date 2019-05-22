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
import walkingkooka.naming.Name;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ContentEncodingListHeaderValueConverterTest extends NonStringHeaderValueConverterTestCase<ContentEncodingListHeaderValueConverter, List<ContentEncoding>> {

    @Test
    public void testParse() {
        this.parseAndCheck2("gzip",
                ContentEncoding.GZIP);
    }

    @Test
    public void testParse2() {
        this.parseAndCheck2("GZIP",
                ContentEncoding.GZIP);
    }

    @Test
    public void testParseCommaSeparated() {
        this.parseAndCheck2("gzip,deflate",
                ContentEncoding.GZIP,
                ContentEncoding.DEFLATE);
    }

    @Test
    public void testParseWhitespaceCommaSeparated() {
        this.parseAndCheck2("gzip, deflate,  br",
                ContentEncoding.GZIP,
                ContentEncoding.DEFLATE,
                ContentEncoding.BR);
    }

    private void parseAndCheck2(final String text, final ContentEncoding...encodings) {
        this.parseAndCheck(text, Lists.of(encodings));
    }

    @Test
    public void testCheckEmptyListFails() {
        assertThrows(HeaderValueException.class, () -> {
            ContentEncodingListHeaderValueConverter.INSTANCE.check(Lists.empty(), HttpHeaderName.CONTENT_ENCODING);
        });
    }

    @Test
    public void testToText() {
        this.toTextAndCheck(Lists.of(ContentEncoding.BR),
                "br");
    }

    @Test
    public void testToText2() {
        this.toTextAndCheck(Lists.of(ContentEncoding.BR, ContentEncoding.GZIP),
                "br, gzip");
    }

    @Override
    Name name() {
        return HttpHeaderName.CONTENT_ENCODING;
    }

    @Override
    String invalidHeaderValue() {
        return "\0invalid";
    }

    @Override
    String converterToString() {
        return ContentEncoding.class.getSimpleName();
    }

    @Override
    ContentEncodingListHeaderValueConverter converter() {
        return ContentEncodingListHeaderValueConverter.INSTANCE;
    }

    @Override
    List<ContentEncoding> value() {
        return Lists.of(ContentEncoding.GZIP, ContentEncoding.DEFLATE);
    }

    @Override
    String valueType() {
        return this.listValueType(ContentEncoding.class);
    }

    @Override
    public String typeNamePrefix() {
        return ContentEncoding.class.getSimpleName();
    }

    @Override
    public Class<ContentEncodingListHeaderValueConverter> type() {
        return ContentEncodingListHeaderValueConverter.class;
    }
}
