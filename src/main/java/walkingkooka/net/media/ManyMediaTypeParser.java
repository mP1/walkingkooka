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

import walkingkooka.collect.list.Lists;

import java.util.List;

/**
 * A {@link MediaTypeParser} that parses a header value containing one or more {@link MediaType} separated by commas
 * as appears in some header values.
 */
final class ManyMediaTypeParser extends MediaTypeParser {

    static List<MediaType> parseMany(final String text) {
        final List<MediaType> result = Lists.array();
        final ManyMediaTypeParser parser = new ManyMediaTypeParser(text);
        final int length = text.length();

        do {
            result.add(parser.parse());
        } while (parser.position < length);

        return result;
    }

    private ManyMediaTypeParser(final String text) {
        super(text);
    }

    /**
     * Ignore the comma
     */
    @Override
    void mediaTypeSeparator() {
        // not an error...parsing will continue eventually.
    }
}
