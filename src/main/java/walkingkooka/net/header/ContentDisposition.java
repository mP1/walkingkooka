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

import walkingkooka.collect.map.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * Represents a content disposition header and its component values.<br>
 * <a href="https://en.wikipedia.org/wiki/MIME#Content-Disposition"></a>
 */
public final class ContentDisposition extends HeaderValueWithParameters2<ContentDisposition,
        ContentDispositionParameterName<?>,
        ContentDispositionType> {
    /**
     * A constants with no parameters.
     */
    public final static Map<ContentDispositionParameterName<?>, Object> NO_PARAMETERS = Maps.empty();

    /**
     * Parses a header value into tokens, which aill also be sorted using their q factor weights.
     * <pre>
     * Content-Disposition: inline
     * Content-Disposition: attachment
     * Content-Disposition: attachment; filename="filename.jpg"
     * ...
     * Content-Disposition: form-data
     * Content-Disposition: form-data; name="fieldName"
     * Content-Disposition: form-data; name="fieldName"; filename="filename.jpg"
     * </pre>
     */
    public static ContentDisposition parse(final String text) {
        return ContentDispositionHeaderParser.parseContentDisposition(text);
    }

    /**
     * Factory that creates a new {@link ContentDisposition}
     */
    public static ContentDisposition with(final ContentDispositionType type) {
        checkType(type);

        return new ContentDisposition(type, NO_PARAMETERS);
    }

    /**
     * Private ctor use factory
     */
    private ContentDisposition(final ContentDispositionType type, final Map<ContentDispositionParameterName<?>, Object> parameters) {
        super(type, parameters);
    }

    // type .....................................................

    /**
     * Getter that retrieves the type
     */
    public ContentDispositionType type() {
        return this.value();
    }

    /**
     * Would be setter that returns a {@link ContentDisposition} with the given type creating a new instance if necessary.
     */
    public ContentDisposition setType(final ContentDispositionType type) {
        checkType(type);
        return this.value.equals(type) ?
                this :
                this.replace(type, this.parameters);
    }

    private static void checkType(final ContentDispositionType type) {
        Objects.requireNonNull(type, "type");
    }

    // replace ...........................................................................................................

    @Override
    ContentDisposition replace(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        return this.replace(this.value, parameters);
    }

    private ContentDisposition replace(final ContentDispositionType type, final Map<ContentDispositionParameterName<?>, Object> parameters) {
        return new ContentDisposition(type, parameters);
    }

    // HeaderValue.................................................................

    @Override
    public boolean isWildcard() {
        return false;
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public boolean isMultipart() {
        return true;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    // Object ..........................................................................................................

    @Override
    int hashCode0(final ContentDispositionType value) {
        return value.hashCode();
    }

    @Override
    boolean equals1(final ContentDispositionType value, final ContentDispositionType otherValue) {
        return value.equals(otherValue);
    }

    @Override
    boolean canBeEquals(final Object other) {
        return other instanceof ContentDisposition;
    }
}
