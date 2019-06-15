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

import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.Map;
import java.util.Objects;

/**
 * The {@link Name} of content disposition type.<br>
 * <a href="https://tools.ietf.org/html/rfc2183"></a>
 * <pre>
 * In the extended BNF notation of [RFC 822], the Content-Disposition
 * header field is defined as follows:
 *
 *      disposition := "Content-Disposition" ":"
 *                     disposition-type
 *                     *(";" disposition-parm)
 *
 *      disposition-type := "inline"
 *                        / "attachment"
 *                        / extension-token
 *                        ; values are not case-sensitive
 *
 *      disposition-parm := filename-parm
 *                        / creation-date-parm
 *                        / modification-date-parm
 *                        / read-date-parm
 *                        / size-parm
 *                        / parameter
 *
 *      filename-parm := "filename" "=" value
 *
 *      creation-date-parm := "creation-date" "=" quoted-date-time
 *
 *      modification-date-parm := "modification-date" "=" quoted-date-time
 *
 *      read-date-parm := "read-date" "=" quoted-date-time
 *
 *      size-parm := "size" "=" 1*DIGIT
 *
 *      quoted-date-time := quoted-string
 *                       ; contents MUST be an RFC 822 `date-time'
 *                       ; numeric timezones (+HHMM or -HHMM) MUST be used
 * </pre>
 */
final public class ContentDispositionType extends HeaderNameValue implements Comparable<ContentDispositionType> {

    /**
     * {@link CaseSensitivity} for disposition type.
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * A read only cache of already prepared {@link ContentDispositionType names}. These constants are incomplete.
     */
    private final static Map<String, ContentDispositionType> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Creates and adds a new {@link ContentDispositionType} to the cache being built.
     */
    private static ContentDispositionType registerConstant(final String name) {
        final ContentDispositionType type = new ContentDispositionType(name);
        ContentDispositionType.CONSTANTS.put(name, type);
        return type;
    }

    /**
     * A {@link ContentDispositionType} <code>attachment</code>
     */
    public final static ContentDispositionType ATTACHMENT = registerConstant("attachment");

    /**
     * A {@link ContentDispositionType} <code>form-data</code>
     */
    public final static ContentDispositionType FORM_DATA = registerConstant("form-data");

    /**
     * A {@link ContentDispositionType} <code>inline</code>
     */
    public final static ContentDispositionType INLINE = registerConstant("inline");

    /**
     * Factory that creates a {@link ContentDispositionType}.
     */
    public static ContentDispositionType with(final String name) {
        CharPredicates.failIfNullOrEmptyOrFalse(name, "name", CharPredicates.rfc2045Token());

        final ContentDispositionType httpHeaderValueParameterName = CONSTANTS.get(name);
        return null != httpHeaderValueParameterName ?
                httpHeaderValueParameterName :
                new ContentDispositionType(name);
    }

    /**
     * Private constructor use factory.
     */
    private ContentDispositionType(final String name) {
        super(name);
    }

    /**
     * Factory that creates a {@link ContentDisposition} with this type and filename.
     */
    public ContentDisposition setFilename(final ContentDispositionFileName filename) {
        Objects.requireNonNull(filename, "filename");

        return this.setParameters(Maps.of(ContentDispositionParameterName.FILENAME, filename));
    }

    /**
     * Factory that creates a {@link ContentDisposition} with this type and filename.
     */
    public ContentDisposition setParameters(final Map<ContentDispositionParameterName<?>, Object> parameters) {
        return ContentDisposition.with(this)
                .setParameters(parameters);
    }

    // Comparable..............................................................................................

    @Override
    public int compareTo(final ContentDispositionType other) {
        return this.compareTo0(other);
    }

    // HeaderNameValue..............................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof ContentDispositionType;
    }

    // Object..................................................................................................

    @Override
    public String toString() {
        return this.name;
    }

    // HasCaseSensitivity................................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }
}
