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

import walkingkooka.collect.list.Lists;

import java.util.List;

final class ETagListHeaderParser extends ETagHeaderParser {

    static List<ETag> parseList(final String text) {
        final ETagListHeaderParser parser = new ETagListHeaderParser(text);
        parser.parse();
        return parser.etags;
    }

    private ETagListHeaderParser(final String text) {
        super(text);
    }

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void multiValueSeparator() {
        this.requireValue = true;
        this.validator = ETagValidator.STRONG;
    }

    void etag(final ETag etag) {
        this.etags.add(etag);
    }

    private final List<ETag> etags = Lists.array();
}
