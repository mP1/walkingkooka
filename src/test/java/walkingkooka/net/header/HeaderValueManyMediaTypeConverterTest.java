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
import walkingkooka.net.http.HttpHeaderName;

import java.util.List;

public final class HeaderValueManyMediaTypeConverterTest extends
        HeaderValueConverterTestCase<HeaderValueManyMediaTypeConverter, List<MediaType>> {

    private final static String TEXT = "type1/subType1; p1=v1, type2/subType2";
    @Test
    public void testContentType() {
        this.parseAndFormatAndCheck(TEXT,
                Lists.of(this.mediaType1(), this.mediaType2()));
    }

    private MediaType mediaType1() {
        return MediaType.with("type1", "subType1").setParameters(Maps.one(MediaTypeParameterName.with("p1"), "v1"));
    }

    private MediaType mediaType2() {
        return MediaType.with("type2", "subType2");
    }

    @Override
    protected HeaderValueManyMediaTypeConverter converter() {
        return HeaderValueManyMediaTypeConverter.INSTANCE;
    }

    @Override
    protected HttpHeaderName<List<MediaType>> name() {
        return HttpHeaderName.ACCEPT;
    }

    @Override
    protected String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    protected String converterToString() {
        return "List<MediaType>";
    }

    @Override
    protected Class<HeaderValueManyMediaTypeConverter> type() {
        return HeaderValueManyMediaTypeConverter.class;
    }
}
