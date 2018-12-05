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

import walkingkooka.collect.list.Lists;

import java.util.List;

/**
 * A parser that handles comma separated etags.
 */
final class HttpETagListHttpHeaderParser extends HttpETagHttpHeaderParser {

    static List<HttpETag> parseList(final String text) {
        final List<HttpETag> result = Lists.array();
        final HttpETagListHttpHeaderParser parser = new HttpETagListHttpHeaderParser(text);
        final int length = text.length();

        int mode = MODE_WEAK_OR_WILDCARD_OR_QUOTE_BEGIN;
        do {
            result.add(parser.parse(mode));
            mode = MODE_SEPARATOR;
        } while (parser.position < length);

        return result;
    }

    private HttpETagListHttpHeaderParser(final String text) {
        super(text);
    }

    @Override
    void separator() {
        // separator ok!
    }
}
