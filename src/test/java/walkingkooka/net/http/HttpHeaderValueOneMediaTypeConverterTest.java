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
import walkingkooka.collect.map.Maps;
import walkingkooka.net.media.MediaType;
import walkingkooka.net.media.MediaTypeParameterName;

public final class HttpHeaderValueOneMediaTypeConverterTest extends
        HttpHeaderValueConverterTestCase<HttpHeaderValueOneMediaTypeConverter, MediaType> {

    @Test
    public void testContentType() {
        this.parseAndCheck("type1/subType1; p1=v1",
                MediaType.with("type1", "subType1")
                        .setParameters(Maps.one(MediaTypeParameterName.with("p1"), "v1")));
    }

    @Override
    HttpHeaderName<MediaType> headerOrParameterName() {
        return HttpHeaderName.CONTENT_TYPE;
    }

    @Override
    HttpHeaderValueOneMediaTypeConverter converter() {
        return HttpHeaderValueOneMediaTypeConverter.INSTANCE;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    String converterToString() {
        return MediaType.class.getSimpleName();
    }

    @Override
    protected Class<HttpHeaderValueOneMediaTypeConverter> type() {
        return HttpHeaderValueOneMediaTypeConverter.class;
    }
}
