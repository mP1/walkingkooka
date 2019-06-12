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

import walkingkooka.naming.Name;

import java.util.List;

/**
 * A {@link HeaderValueHandler} that expects a list of {@link CacheControlDirective directives}.
 */
final class CacheControlDirectiveListHeaderValueHandler extends NonStringHeaderValueHandler<List<CacheControlDirective<?>>> {

    /**
     * Singleton
     */
    final static CacheControlDirectiveListHeaderValueHandler INSTANCE = new CacheControlDirectiveListHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private CacheControlDirectiveListHeaderValueHandler() {
        super();
    }

    @Override
    List<CacheControlDirective<?>> parse0(final String text, final Name name) {
        return CacheControlDirective.parse(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkListOfType(value, CacheControlDirective.class, name);
    }

    @Override
    String toText0(final List<CacheControlDirective<?>> directives, final Name name) {
        return HeaderValue.toHeaderTextList(directives, SEPARATOR);
    }

    @Override
    public String toString() {
        return this.toStringListOf(CacheControlDirective.class);
    }
}
