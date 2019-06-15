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
 * A {@link HeaderValueHandler} that converts a {@link String} into one {@link LanguageTagName}.
 */
final class LanguageTagNameHeaderValueHandler extends NonStringHeaderValueHandler<LanguageTagName> {

    /**
     * Singleton
     */
    final static LanguageTagNameHeaderValueHandler INSTANCE = new LanguageTagNameHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private LanguageTagNameHeaderValueHandler() {
        super();
    }

    @Override
    LanguageTagName parse0(final String value, final Name name) {
        return LanguageTagName.with(value);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, LanguageTagName.class, name);
    }

    @Override
    String toText0(final LanguageTagName value, final Name name) {
        return value.toString();
    }

    @Override
    public String toString() {
        return toStringType(LanguageTagName.class);
    }
}
