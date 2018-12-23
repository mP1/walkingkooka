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

final class ETagOneHeaderParser extends ETagHeaderParser {

    static ETag parseOne(final String text) {
        final ETagOneHeaderParser parser = new ETagOneHeaderParser(text);
        parser.parse();
        return parser.etag;
    }

    private ETagOneHeaderParser(final String text) {
        super(text);
    }

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void multiValueSeparator() {
        this.failInvalidCharacter();
    }

    void etag(final ETag etag) {
        this.etag = etag;
    }

    private ETag etag;
}
