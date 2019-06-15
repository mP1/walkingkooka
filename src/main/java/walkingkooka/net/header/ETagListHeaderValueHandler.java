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

import java.util.List;

/**
 * A {@link HeaderValueHandler} that expects comma separated {@link ETag tags}.
 */
final class ETagListHeaderValueHandler extends NonStringHeaderValueHandler<List<ETag>> {

    /**
     * Singleton
     */
    final static ETagListHeaderValueHandler INSTANCE = new ETagListHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private ETagListHeaderValueHandler() {
        super();
    }

    @Override
    List<ETag> parse0(final String text, final Name name) {
        return ETag.parseList(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkListOfType(value, ETag.class, name);
    }

    @Override
    String toText0(final List<ETag> value, final Name name) {
        return HeaderValue.toHeaderTextList(value, SEPARATOR);
    }

    @Override
    public String toString() {
        return toStringListOf(ETag.class);
    }
}
