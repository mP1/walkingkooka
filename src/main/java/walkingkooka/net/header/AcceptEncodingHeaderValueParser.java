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
import walkingkooka.net.HasQFactorWeight;
import walkingkooka.predicate.character.CharPredicate;

import java.util.List;

/**
 * <a href="https://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html"></a>
 * <pre>
 * 14.3 Accept-Encoding
 * The Accept-Encoding request-header field is similar to Accept, but restricts the content-codings (section 3.5) that are acceptable in the response.
 *
 *        Accept-Encoding  = "Accept-Encoding" ":"
 *                           1#( codings [ ";" "q" "=" qvalue ] )
 *        codings          = ( content-coding | "*" )
 * Examples of its use are:
 *
 *        Accept-Encoding: compress, gzip
 *        Accept-Encoding:
 *        Accept-Encoding: *
 *        Accept-Encoding: compress;q=0.5, gzip;q=1.0
 *        Accept-Encoding: gzip;q=1.0, identity; q=0.5, *;q=0
 * A server tests whether a content-coding is acceptable, according to an Accept-Encoding field, using these rules:
 *
 *       1. If the content-coding is one of the content-codings listed in
 *          the Accept-Encoding field, then it is acceptable, unless it is
 *          accompanied by a qvalue of 0. (As defined in section 3.9, a
 *          qvalue of 0 means "not acceptable.")
 *       2. The special "*" symbol in an Accept-Encoding field matches any
 *          available content-coding not explicitly listed in the header
 *          field.
 *       3. If multiple content-codings are acceptable, then the acceptable
 *          content-coding with the highest non-zero qvalue is preferred.
 *       4. The "identity" content-coding is always acceptable, unless
 *          specifically refused because the Accept-Encoding field includes
 *          "identity;q=0", or because the field includes "*;q=0" and does
 *          not explicitly include the "identity" content-coding. If the
 *          Accept-Encoding field-value is empty, then only the "identity"
 *          encoding is acceptable.
 * If an Accept-Encoding field is present in a request, and if the server cannot send a response which is acceptable according to the Accept-Encoding header, then the server SHOULD send an error response with the 406 (Not Acceptable) status code.
 *
 * If no Accept-Encoding field is present in a request, the server MAY assume that the client will accept any content coding. In this case, if "identity" is one of the available content-codings, then the server SHOULD use the "identity" content-coding, unless it has additional information that a different content-coding is meaningful to the client.
 *
 *       Note: If the request does not include an Accept-Encoding field,
 *       and if the "identity" content-coding is unavailable, then
 *       content-codings commonly understood by HTTP/1.0 clients (i.e.,
 *       "gzip" and "compress") are preferred; some older clients
 *       improperly display messages sent with other content-codings.  The
 *       server might also make this decision based on information about
 *       the particular user-agent or client.
 *       Note: Most HTTP/1.0 applications do not recognize or obey qvalues
 *       associated with content-codings. This means that qvalues will not
 *       work and are not permitted with x-gzip or x-compress.
 * </pre>
 */
final class AcceptEncodingHeaderValueParser extends HeaderValueParserWithParameters<Encoding, EncodingParameterName<?>> {

    static AcceptEncoding parseAcceptEncoding(final String text) {
        final AcceptEncodingHeaderValueParser parser = new AcceptEncodingHeaderValueParser(text);
        parser.parse();
        parser.encodings.sort(HasQFactorWeight.qFactorDescendingComparator());
        return new AcceptEncoding(Lists.readOnly(parser.encodings));
    }

    private AcceptEncodingHeaderValueParser(final String text) {
        super(text);
    }

    @Override
    boolean allowMultipleValues() {
        return true;
    }

    @Override
    Encoding wildcardValue() {
        this.position++; // consume star
        return Encoding.WILDCARD_ENCODING;
    }

    @Override
    Encoding value() {
        return Encoding.with(this.token(RFC2045TOKEN));
    }

    @Override
    void missingValue() {
        this.failEmptyToken("Accept-Encoding");
    }

    @Override
    EncodingParameterName<?> parameterName() {
        return this.parameterName(PARAMETER_NAME, EncodingParameterName::with);
    }

    private final static CharPredicate PARAMETER_NAME = RFC2045TOKEN;

    @Override
    String quotedParameterValue(final EncodingParameterName<?> parameterName) {
        return this.quotedText(QUOTED_PARAMETER_VALUE, ESCAPING_SUPPORTED);
    }

    final static CharPredicate QUOTED_PARAMETER_VALUE = ASCII;

    @Override
    String unquotedParameterValue(final EncodingParameterName<?> parameterName) {
        return this.token(UNQUOTED_PARAMETER_VALUE);
    }

    final static CharPredicate UNQUOTED_PARAMETER_VALUE = RFC2045TOKEN;

    @Override
    void valueComplete(final Encoding encoding) {
        this.encodings.add(encoding);
    }

    private final List<Encoding> encodings = Lists.array();
}
