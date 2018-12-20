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
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.HashCodeEqualsDefined;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * An entry that holds both the parameter and value.
 */
final class HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry implements Entry<HttpRequestParameterName, List<String>>,
        HashCodeEqualsDefined {

    static HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry with(final Entry<String, String[]> entry) {
        return new HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry(entry);
    }

    private HttpServletRequestHttpRequestParametersMapEntrySetIteratorEntry(final Entry<String, String[]> entry) {
        super();
        this.entry = entry;
    }

    @Override
    public HttpRequestParameterName getKey() {
        if (null == this.key) {
            this.key = HttpRequestParameterName.with(this.entry.getKey());
        }
        return this.key;
    }

    private HttpRequestParameterName key;

    @Override
    public List<String> getValue() {
        if (null == this.value) {
            this.value = Lists.of(this.entry.getValue());
        }
        return this.value;
    }

    private List<String> value;

    @Override
    public List<String> setValue(final List<String> value) {
        throw new UnsupportedOperationException();
    }

    private final Entry<String, String[]> entry;

    // Object .............................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey(), this.getValue());
    }

    @Override
    public final boolean equals(final Object other) {
        return this == other ||
                other instanceof Entry &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final Entry<Object, Object> other) {
        return this.getKey().equals(other.getKey()) &&
                this.getValue().equals(other.getValue());
    }

    @Override
    public String toString() {
        final ToStringBuilder b = ToStringBuilder.empty();
        b.label(this.getKey().value());
        b.value(this.getValue());
        return b.build();
    }
}
