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

import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.build.tostring.UsesToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.http.HttpHeaderScope;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Represents a content disposition header and its component values.<br>
 * <a href="https://en.wikipedia.org/wiki/MIME#Content-Disposition"></a>
 */
public final class ContentDisposition implements HeaderValueWithParameters<ContentDispositionParameterName<?>>,
        UsesToStringBuilder {

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
        return ContentDispositionHeaderParser.parse(text);
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
        super();
        this.type = type;
        this.parameters = parameters;
    }

    // type .....................................................

    /**
     * Getter that retrieves the type
     */
    public ContentDispositionType type() {
        return this.type;
    }

    /**
     * Would be setter that returns a {@link ContentDisposition} with the given type creating a new instance if necessary.
     */
    public ContentDisposition setType(final ContentDispositionType type) {
        checkType(type);
        return this.type.equals(type) ?
                this :
                this.replace(type, this.parameters);
    }

    private final ContentDispositionType type;

    private static void checkType(final ContentDispositionType type) {
        Objects.requireNonNull(type, "type");
    }

    // parameters.........................................................................................

    /**
     * A map view of all parameters to their text or string value.
     */
    @Override
    public Map<ContentDispositionParameterName<?>, Object> parameters() {
        return this.parameters;
    }

    @Override
    public ContentDisposition setParameters(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        final Map<ContentDispositionParameterName<?>, Object> copy = checkParameters(parameters);
        return this.parameters.equals(copy) ?
                this :
                this.replace(this.type, copy);
    }

    private final Map<ContentDispositionParameterName<?>, Object> parameters;

    /**
     * Makes a copy of the parameters and also checks the value.
     */
    private static Map<ContentDispositionParameterName<?>, Object> checkParameters(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        final Map<ContentDispositionParameterName<?>, Object> copy = Maps.ordered();
        for(Entry<ContentDispositionParameterName<?>, Object> nameAndValue  : parameters.entrySet()) {
            final ContentDispositionParameterName name = nameAndValue.getKey();
            copy.put(name,
                    name.checkValue(nameAndValue.getValue()));
        }
        return copy;
    }

    // replace ...........................................................................................................

    private ContentDisposition replace(final ContentDispositionType type, final Map<ContentDispositionParameterName<?>, Object> parameters) {
        return new ContentDisposition(type, parameters);
    }

    // HeaderValue.................................................................

    @Override
    public String toHeaderText() {
        return this.toString();
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public HttpHeaderScope scope() {
        return HttpHeaderScope.REQUEST_RESPONSE;
    }

    // Object .............................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.parameters);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentDisposition &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentDisposition other) {
        return this.type.equals(other.type) &&
                this.parameters.equals(other.parameters);
    }

    /**
     * Dumps the raw header value without quotes.
     */
    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.disable(ToStringBuilderOption.QUOTE);
        builder.value(this.type);

        builder.separator(TO_STRING_PARAMETER_SEPARATOR);
        builder.valueSeparator(TO_STRING_PARAMETER_SEPARATOR);
        builder.labelSeparator(PARAMETER_NAME_VALUE_SEPARATOR.string());
        builder.value(this.parameters);
    }

    /**
     * Separator between parameters used by {@link #toString()}.
     */
    private final static String TO_STRING_PARAMETER_SEPARATOR = PARAMETER_SEPARATOR.string().concat(" ");
}
