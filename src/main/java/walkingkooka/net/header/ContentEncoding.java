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

import walkingkooka.Cast;
import walkingkooka.Value;
import walkingkooka.collect.map.Maps;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;

import java.util.List;
import java.util.Map;


/**
 * Content encoding
 * <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html"></a>
 * <pre>
 * 14.11 Content-Encoding
 * The Content-Encoding entity-header field is used as a modifier to the media-type. When present, its value indicates what additional content codings have been applied to the entity-body, and thus what decoding mechanisms must be applied in order to obtain the media-type referenced by the Content-Type header field. Content-Encoding is primarily used to allow a document to be compressed without losing the identity of its underlying media type.
 *
 *        Content-Encoding  = "Content-Encoding" ":" 1#content-coding
 * Content codings are defined in section 3.5. An example of its use is
 *
 *        Content-Encoding: gzip
 * The content-coding is a characteristic of the entity identified by the Request-URI. Typically, the entity-body is stored with this encoding and is only decoded before rendering or analogous usage. However, a non-transparent proxy MAY modify the content-coding if the new coding is known to be acceptable to the recipient, unless the "no-transform" cache-control directive is present in the message.
 *
 * If the content-coding of an entity is not "identity", then the response MUST include a Content-Encoding entity-header (section 14.11) that lists the non-identity content-coding(s) used.
 *
 * If the content-coding of an entity in a request message is not acceptable to the origin server, the server SHOULD respond with a status code of 415 (Unsupported Media Type).
 *
 * If multiple encodings have been applied to an entity, the content codings MUST be listed in the order in which they were applied. Additional information about the encoding parameters MAY be provided by other entity-header fields not defined by this specification.    
 * </pre>
 */
public final class ContentEncoding implements Value<String>,
        HeaderValue,
        Comparable<ContentEncoding> {

    /**
     * {@see CaseSensitivity}
     */
    private final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    /**
     * Holds all constants.
     */
    private final static Map<String, ContentEncoding> CONSTANTS = Maps.sorted(CASE_SENSITIVITY.comparator());

    /**
     * Holds a {@link ContentEncoding} for br.
     */
    public final static ContentEncoding BR = registerConstant("br");

    /**
     * Holds a {@link ContentEncoding} for deflate
     */
    public final static ContentEncoding COMPRESS = registerConstant("compress");

    /**
     * Holds a {@link ContentEncoding} for deflate.
     */
    public final static ContentEncoding DEFLATE = registerConstant("deflate");

    /**
     * Holds a {@link ContentEncoding} for gzip.
     */
    public final static ContentEncoding GZIP = registerConstant("gzip");

    /**
     * Holds a {@link ContentEncoding} for identity.
     */
    public final static ContentEncoding IDENTITY = registerConstant("identity");

    /**
     * Creates and then registers the constant.
     */
    static private ContentEncoding registerConstant(final String text) {
        final ContentEncoding contentType = with(text);
        CONSTANTS.put(text, contentType);
        return contentType;
    }

    /**
     * Parses the text into a {@link List} of {@link ContentEncoding}.
     */
    public static List<ContentEncoding> parse(final String text) {
        return ContentEncodingListHeaderValueHandler.INSTANCE.parse(text, HttpHeaderName.CONTENT_ENCODING);
    }

    /**
     * Factory that creates a {@link ContentEncoding} after verifying the individual characters.
     */
    public static ContentEncoding with(final String value) {
        CharPredicates.failIfNullOrEmptyOrFalse(value, "value", ContentEncodingListHeaderValueParser.RFC2045TOKEN);

        final ContentEncoding contentEncoding = CONSTANTS.get(value);
        return null != contentEncoding ?
                contentEncoding :
                new ContentEncoding(value);
    }

    /**
     * Private ctor.
     */
    private ContentEncoding(final String value) {
        super();

        this.value = value;
    }

    @Override
    public final String value() {
        return this.value;
    }

    private final String value;

    // HasHeaderScope ....................................................................................................

    @Override
    public final boolean isMultipart() {
        return true;
    }

    @Override
    public final boolean isRequest() {
        return false;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    // headerText.............................................................................

    @Override
    public String toHeaderText() {
        return this.value;
    }

    @Override
    public boolean isWildcard() {
        return false;
    }

    // Comparable....................................................................................................

    @Override
    public final int compareTo(final ContentEncoding other) {
        return CASE_SENSITIVITY.comparator().compare(this.value, other.value);
    }

    // Object.....................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.value);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ContentEncoding &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ContentEncoding other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Returns the value in its raw form.
     */
    @Override
    public String toString() {
        return this.value;
    }
}
