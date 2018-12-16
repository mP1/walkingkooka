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

/**
 * A parser that only parses text with a single etag.
 */
final class ETagOneHttpHeaderParser extends ETagHttpHeaderParser {

    static ETag parseOne(final String text) {
        final ETagOneHttpHeaderParser parser = new ETagOneHttpHeaderParser(text);
        final ETag tag = parser.parse(ETagOneHttpHeaderParser.MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN);

        final int position = parser.position;
        if (position != text.length()) {
            parser.failInvalidCharacter();
        }
        return tag;
    }

    private ETagOneHttpHeaderParser(final String text) {
        super(text);
    }

    @Override
    void separator() {
        this.failInvalidCharacter();
    }
}
