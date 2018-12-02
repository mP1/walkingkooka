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

import java.util.Map;

public final class MediaTypeHeaderParserOneTest extends MediaTypeHeaderParserTestCase<MediaTypeHeaderParserOne,
        MediaType> {

    @Test
    public void testTrailingCommaFails() {
        this.parseFails("type/subtype,");
    }

    @Test
    public void testTrailingCommaFails2() {
        this.parseFails("type/subtype;p=v,");
    }

    @Override
    final void parseAndCheck(final String text,
                             final String type,
                             final String subtype,
                             final Map<MediaTypeParameterName<?>, Object> parameters) {
        this.check(MediaTypeHeaderParserOne.parseMediaType(text), type, subtype, parameters);
    }

    @Override
    MediaTypeHeaderParserOne createHeaderParser(final String text) {
        return new MediaTypeHeaderParserOne(text);
    }

    @Override
    MediaType parse(final String text) {
        return MediaTypeHeaderParserOne.parseMediaType(text);
    }

    @Override
    protected Class<MediaTypeHeaderParserOne> type() {
        return MediaTypeHeaderParserOne.class;
    }
}
