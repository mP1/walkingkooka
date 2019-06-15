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

package walkingkooka.net;

import walkingkooka.Value;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class with getters that return the common components of a {@link AbsoluteUrl} or {@link RelativeUrl}.
 * <h1>Definitions</h1>
 * <p>
 * <a href="http://tools.ietf.org/html/rfc3986#page-49}></a>
 * </p>
 *
 * <pre>
 * Appendix A.  Collected ABNF for URI
 *
 *    URI           = scheme ":" hier-part [ "?" query ] [ "#" fragment ]
 *
 *    hier-part     = "//" authority path-abempty
 *                  / path-absolute
 *                  / path-rootless
 *                  / path-empty
 *
 *    URI-reference = URI / relative-ref
 *
 *    absolute-URI  = scheme ":" hier-part [ "?" query ]
 *
 *    relative-ref  = relative-part [ "?" query ] [ "#" fragment ]
 *
 *    relative-part = "//" authority path-abempty
 *                  / path-absolute
 *                  / path-noscheme
 *                  / path-empty
 *
 *    scheme        = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
 *
 *    authority     = [ userinfo "@" ] host [ ":" port ]
 *    userinfo      = *( unreserved / pct-encoded / sub-delims / ":" )
 *    host          = IP-literal / IPv4address / reg-name
 *    port          = *DIGIT
 *
 *    IP-literal    = "[" ( IPv6address / IPvFuture  ) "]"
 *
 *    IPvFuture     = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )
 *
 *    IPv6address   =                            6( h16 ":" ) ls32
 *                  /                       "::" 5( h16 ":" ) ls32
 *                  / [               h16 ] "::" 4( h16 ":" ) ls32
 *                  / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
 *                  / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
 *                  / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
 *                  / [ *4( h16 ":" ) h16 ] "::"              ls32
 *                  / [ *5( h16 ":" ) h16 ] "::"              h16
 *                  / [ *6( h16 ":" ) h16 ] "::"
 *
 *    h16           = 1*4HEXDIG
 *    ls32          = ( h16 ":" h16 ) / IPv4address
 *    IPv4address   = dec-octet "." dec-octet "." dec-octet "." dec-octet
 *
 *    dec-octet     = DIGIT                 ; 0-9
 *                  / %x31-39 DIGIT         ; 10-99
 *                  / "1" 2DIGIT            ; 100-199
 *                  / "2" %x30-34 DIGIT     ; 200-249
 *                  / "25" %x30-35          ; 250-255
 *
 *    reg-name      = *( unreserved / pct-encoded / sub-delims )
 *
 *    path          = path-abempty    ; begins with "/" or is empty
 *                  / path-absolute   ; begins with "/" but not "//"
 *                  / path-noscheme   ; begins with a non-colon segment
 *                  / path-rootless   ; begins with a segment
 *                  / path-empty      ; zero characters
 *
 *    path-abempty  = *( "/" segment )
 *    path-absolute = "/" [ segment-nz *( "/" segment ) ]
 *    path-noscheme = segment-nz-nc *( "/" segment )
 *    path-rootless = segment-nz *( "/" segment )
 *    path-empty    = 0<pchar>
 *
 *    segment       = *pchar
 *    segment-nz    = 1*pchar
 *    segment-nz-nc = 1*( unreserved / pct-encoded / sub-delims / "@" )
 *                  ; non-zero-length segment without any colon ":"
 *
 *    pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
 *
 *    query         = *( pchar / "/" / "?" )
 *
 *    fragment      = *( pchar / "/" / "?" )
 *
 *    pct-encoded   = "%" HEXDIG HEXDIG
 *
 *    unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
 *    reserved      = gen-delims / sub-delims
 *    gen-delims    = ":" / "/" / "?" / "#" / "[" / "]" / "@"
 *    sub-delims    = "!" / "$" / "&" / "'" / "(" / ")"
 *                  / "*" / "+" / "," / ";" / "="
 * </pre>
 */
public abstract class AbsoluteOrRelativeUrl extends Url implements Value<String> {

    /**
     * Helper used by all parse methods.
     */
    static String nullToEmpty(final String value) {
        return null == value ?
                "" :
                value;
    }

    // ctor...........................................................................................................

    /**
     * Package private constructor to limit sub classing.
     */
    AbsoluteOrRelativeUrl(final UrlPath path, final UrlQueryString query, final UrlFragment fragment) {
        super();
        this.path = path;
        this.query = query;
        this.fragment = fragment;
    }

    // type safe casters

    /**
     * Only {@link RelativeUrl} returns true
     */
    public abstract boolean isRelative();

    /**
     * Only {@link AbsoluteUrl} returns true.
     */
    public abstract boolean isAbsolute();

    // properties

    /**
     * Would be setter that attempts to set or replace the absolute url only properties. This has the added benefit
     * of being useful and able to convert a relative url to an absolute url.
     */
    public abstract AbsoluteUrl set(final UrlScheme scheme,
                                    final Optional<UrlCredentials> credentials,
                                    final HostAddress host,
                                    final Optional<IpPort> port);

    /**
     * Returns the {@link RelativeUrl} form of this url.
     */
    public abstract RelativeUrl relativeUrl();

    /**
     * Returns the entire {@link AbsoluteOrRelativeUrl} as a {@link String}.
     */
    @Override
    public final String value() {
        return this.toString();
    }

    /**
     * Returns the path portion of this {@link AbsoluteOrRelativeUrl} which may be empty but never null.
     */
    public final UrlPath path() {
        return this.path;
    }

    /**
     * Would be setter that returns a {@link AbsoluteOrRelativeUrl} with the given path creating a new instance if necessary.
     */
    abstract public AbsoluteOrRelativeUrl setPath(final UrlPath path);

    final AbsoluteOrRelativeUrl setPath0(final UrlPath path) {
        Objects.requireNonNull(path, "path");
        return this.path.equals(path) ?
                this :
                this.replace(path, this.query, this.fragment);
    }

    final UrlPath path;

    /**
     * Returns the query string which may be empty but never null. Note the query string will have spaces and other similar invalid characters encoded.
     */
    public final UrlQueryString query() {
        return this.query;
    }

    /**
     * Would be setter that returns a {@link AbsoluteOrRelativeUrl} with the given query string creating a new instance if necessary.
     */
    abstract public AbsoluteOrRelativeUrl setQuery(final UrlQueryString query);

    final AbsoluteOrRelativeUrl setQuery0(final UrlQueryString query) {
        Objects.requireNonNull(query, "query");
        return this.query.equals(query) ?
                this :
                this.replace(this.path, query, this.fragment);
    }

    final UrlQueryString query;

    // anchor

    /**
     * {@see UrlFragment}
     */
    public final UrlFragment fragment() {
        return this.fragment;
    }

    /**
     * Would be setter that returns a {@link AbsoluteOrRelativeUrl} with the given fragment creating a new instance if necessary.
     */
    abstract public AbsoluteOrRelativeUrl setFragment(final UrlFragment fragment);

    final AbsoluteOrRelativeUrl setFragment0(final UrlFragment fragment) {
        Objects.requireNonNull(fragment, "fragment");
        return this.fragment.equals(fragment) ?
                this :
                this.replace(this.path, this.query, fragment);
    }

    final UrlFragment fragment;

    /**
     * Factory that creates a new {@link AbsoluteOrRelativeUrl}
     */
    abstract AbsoluteOrRelativeUrl replace(final UrlPath path, final UrlQueryString query, final UrlFragment fragment);

    // Object...........................................................................................................

    @Override
    public final boolean isData() {
        return false;
    }

    // Object...........................................................................................................

    /**
     * Dumps the full URL in {@link String} form.
     */
    @Override
    public final String toString() {
        final StringBuilder b = new StringBuilder();
        this.toString0(b);
        this.path.toString0(b);
        this.query.toString0(b);
        this.fragment.toString0(b);
        return b.toString();
    }

    abstract void toString0(final StringBuilder b);

    // Serializable

    private final static long serialVersionUID = 1L;
}
