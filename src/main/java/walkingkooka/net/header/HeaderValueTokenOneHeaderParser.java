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
 * A parser that parses a single {@link HeaderValueToken}.
 */
final class HeaderValueTokenOneHeaderParser extends HeaderValueTokenHeaderParser {

    static HeaderValueToken parseHeaderValueToken(final String text) {
        checkText(text, "header");

        final HeaderValueTokenOneHeaderParser parser = new HeaderValueTokenOneHeaderParser(text);
        parser.parse();
        return parser.token;
    }

    // @VisibleForTesting
    HeaderValueTokenOneHeaderParser(final String text) {
        super(text);
    }

    @Override
    void headerValueTokenEnd() {
        // nop
    }

    @Override
    void separator() {
        this.failInvalidCharacter();
    }
}
