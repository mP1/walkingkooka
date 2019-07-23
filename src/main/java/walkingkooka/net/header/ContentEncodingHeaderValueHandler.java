/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
 */

package walkingkooka.net.header;

import walkingkooka.naming.Name;

/**
 * A {@link HeaderValueHandler} that handles content-encoding headers.<br>
 * <pre>
 * Content-Encoding: gzip
 * Content-Encoding: compress
 * Content-Encoding: deflate
 * Content-Encoding: identity
 * Content-Encoding: br
 *
 * // Multiple, in the order in which they were applied
 * Content-Encoding: gzip, identity
 * Content-Encoding: deflate, gzip
 * </pre>
 */
final class ContentEncodingHeaderValueHandler extends NonStringHeaderValueHandler<ContentEncoding> {

    /**
     * Singleton
     */
    final static ContentEncodingHeaderValueHandler INSTANCE = new ContentEncodingHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private ContentEncodingHeaderValueHandler() {
        super();
    }

    @Override
    ContentEncoding parse0(final String text, final Name name) {
        return ContentEncodingHeaderValueParser.parseContentEncoding(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, ContentEncoding.class, name);
    }

    @Override
    String toText0(final ContentEncoding contentEncoding, final Name name) {
        return contentEncoding.toHeaderText();
    }

    @Override
    public String toString() {
        return ContentEncoding.class.getSimpleName();
    }
}
