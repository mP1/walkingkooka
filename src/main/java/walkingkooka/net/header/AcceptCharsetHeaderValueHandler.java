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

/**
 * A {@link HeaderValueHandler} that parses a content header value into a {@link AcceptCharset}.
 */
final class AcceptCharsetHeaderValueHandler extends NonStringHeaderValueHandler<AcceptCharset> {

    /**
     * Singleton
     */
    final static AcceptCharsetHeaderValueHandler INSTANCE = new AcceptCharsetHeaderValueHandler();

    /**
     * Private ctor use singleton.
     */
    private AcceptCharsetHeaderValueHandler() {
        super();
    }

    @Override
    AcceptCharset parse0(final String text, final Name name) {
        return AcceptCharset.parse(text);
    }

    @Override
    void check0(final Object value, final Name name) {
        this.checkType(value, AcceptCharset.class, name);
    }

    @Override
    String toText0(final AcceptCharset value, final Name name) {
        return value.toHeaderText();
    }

    @Override
    public String toString() {
        return this.toStringType(AcceptCharset.class);
    }
}
