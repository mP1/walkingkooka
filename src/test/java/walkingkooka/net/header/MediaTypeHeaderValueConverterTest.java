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
import walkingkooka.collect.map.Maps;

public final class MediaTypeHeaderValueConverterTest extends
        HeaderValueConverterTestCase<MediaTypeHeaderValueConverter, MediaType> {

    @Override public String typeNamePrefix() {
        return MediaType.class.getSimpleName();
    }

    @Test
    public void testContentType() {
        this.parseAndToTextAndCheck("type1/subType1; p1=v1",
                MediaType.with("type1", "subType1")
                        .setParameters(Maps.of(MediaTypeParameterName.with("p1"), "v1")));
    }

    @Override
    MediaTypeHeaderValueConverter converter() {
        return MediaTypeHeaderValueConverter.INSTANCE;
    }

    @Override
    HttpHeaderName<MediaType> name() {
        return HttpHeaderName.CONTENT_TYPE;
    }

    @Override
    String invalidHeaderValue() {
        return "invalid!!!";
    }

    @Override
    MediaType value() {
        return MediaType.parse("type1/sub1;p1=v1");
    }

    @Override
    String valueType() {
        return this.valueType(MediaType.class);
    }

    @Override
    String converterToString() {
        return MediaType.class.getSimpleName();
    }

    @Override
    public Class<MediaTypeHeaderValueConverter> type() {
        return MediaTypeHeaderValueConverter.class;
    }
}
