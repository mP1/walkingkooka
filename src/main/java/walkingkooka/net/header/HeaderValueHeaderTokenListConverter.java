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
 * A {@link HeaderValueConverter2} that expects comma separated {@link HeaderToken tokens}.
 */
final class HeaderValueHeaderTokenListConverter extends HeaderValueConverter2<List<HeaderToken>> {

    /**
     * Singleton
     */
    final static HeaderValueHeaderTokenListConverter INSTANCE = new HeaderValueHeaderTokenListConverter();

    /**
     * Private ctor use singleton.
     */
    private HeaderValueHeaderTokenListConverter() {
        super();
    }

    @Override
    List<HeaderToken> parse0(final String value, final Name name) {
        return HeaderToken.parse(value);
    }

    @Override
    String format0(final List<HeaderToken> values, final Name name) {
        return HeaderToken.format(values);
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String toString() {
        return toStringListOf(HeaderToken.class);
    }
}
