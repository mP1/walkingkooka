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
import walkingkooka.net.HasQFactorWeight;

import java.util.List;

/**
 * Parsers text which holds a multiple media type separated by commas.
 */
final class MediaTypeListHeaderParser extends MediaTypeHeaderParser{

    static List<MediaType> parseMediaTypeList(final String text) {
        final MediaTypeListHeaderParser parser = new MediaTypeListHeaderParser(text);
        parser.parse();
        parser.list.sort(HasQFactorWeight.qFactorDescendingComparator());
        return Lists.readOnly(parser.list);
    }

    private MediaTypeListHeaderParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return true;
    }

    @Override
    void valueComplete(final MediaType mediaType) {
        this.list.add(mediaType);
    }

    private final List<MediaType> list = Lists.array();
}
