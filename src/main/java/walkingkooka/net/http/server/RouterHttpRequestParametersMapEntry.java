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

package walkingkooka.net.http.server;

import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * Read only {@link Entry} used by {@link RouterHttpRequestParametersMapEntrySet} and other iterators.
 */
final class RouterHttpRequestParametersMapEntry implements Entry<HttpRequestAttribute, Object>, HashCodeEqualsDefined {

    static RouterHttpRequestParametersMapEntry with(final HttpRequestAttribute key, final Object value) {
        return new RouterHttpRequestParametersMapEntry(key, value);
    }

    private RouterHttpRequestParametersMapEntry(final HttpRequestAttribute key, final Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public HttpRequestAttribute getKey() {
        return this.key;
    }

    private final HttpRequestAttribute key;

    @Override
    public Object getValue() {
        return this.value;
    }

    private final Object value;

    @Override
    public Object setValue(final Object value) {
        throw new UnsupportedOperationException();
    }

    // Object .......................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.key, this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof Entry<?, ?> &&
                        equals0(Cast.to(other));
    }

    private boolean equals0(final Entry<?, ?> other) {
        return this.key.equals(other.getKey()) &&
                this.value.equals(other.getValue());
    }

    @Override
    public String toString() {
        return this.key + "=" + CharSequences.quoteIfChars(this.value);
    }
}
