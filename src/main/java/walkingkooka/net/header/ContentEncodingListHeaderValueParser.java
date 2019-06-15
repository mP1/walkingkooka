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

import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.character.CharPredicates;

import java.util.List;

/**
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
final class ContentEncodingListHeaderValueParser extends HeaderValueParser {

    static List<ContentEncoding> parseContentEncodingList(final String text) {
        final ContentEncodingListHeaderValueParser parser = new ContentEncodingListHeaderValueParser(text);
        parser.parse();
        return Lists.readOnly(parser.contentEncodings);
    }

    private ContentEncodingListHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    void whitespace() {
        this.token(CharPredicates.whitespace());
    }

    @Override
    void tokenSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void keyValueSeparator() {
        this.failInvalidCharacter();
    }

    @Override
    void multiValueSeparator() {
        // skip
    }

    @Override
    void wildcard() {
        this.failInvalidCharacter();
    }

    @Override
    void slash() {
        this.failInvalidCharacter();
    }

    @Override
    void quotedText() {
        this.failInvalidCharacter();
    }

    @Override
    void comment() {
        this.failCommentPresent();
    }

    @Override
    void token() {
        this.contentEncodings.add(ContentEncoding.with(this.token(RFC2045TOKEN)));
    }

    @Override
    void endOfText() {
        if(this.contentEncodings.isEmpty()) {
            this.missingValue();
        }
    }

    @Override
    void missingValue() {
        this.failMissingValue("Content-Encoding");
    }

    private List<ContentEncoding> contentEncodings = Lists.array();
}
