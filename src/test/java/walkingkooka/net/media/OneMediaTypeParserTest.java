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

package walkingkooka.net.media;

import org.junit.Test;

import java.util.Map;

public final class OneMediaTypeParserTest extends MediaTypeParserTestCase<OneMediaTypeParser> {

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
                                       final Map<MediaTypeParameterName, String> parameters) {
        this.check(MediaType.parseOne(text), type, subtype, parameters);
    }

    @Override
    protected Class<OneMediaTypeParser> type() {
        return OneMediaTypeParser.class;
    }
}
