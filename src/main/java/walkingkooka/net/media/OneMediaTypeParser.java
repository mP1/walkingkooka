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

final class OneMediaTypeParser extends MediaTypeParser {

    static MediaType parseOneOrFail(final String text) {
        return new OneMediaTypeParser(text).parse(MODE_TYPE);
    }

    private OneMediaTypeParser(final String text) {
        super(text);
    }

    /**
     * If a comma is encountered when parsing a single mime type it is invalid.
     */
    @Override
    void mediaTypeSeparator() {
        this.failInvalidCharacter();
    }
}
