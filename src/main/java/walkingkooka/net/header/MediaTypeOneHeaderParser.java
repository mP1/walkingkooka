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

/**
 * Parsers text which holds a single media type. This will if a separator (comma) is found.
 */
final class MediaTypeOneHeaderParser extends MediaTypeHeaderParser{

    static MediaType parseMediaType(final String text) {
        final MediaTypeOneHeaderParser parser = new MediaTypeOneHeaderParser(text);
        parser.parse();
        return parser.mediaType;
    }

    private MediaTypeOneHeaderParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return false;
    }

    @Override
    void valueComplete(final MediaType mediaType) {
        this.mediaType = mediaType;
    }

    private MediaType mediaType;
}
