/**
 * Copyright 2018 Miroslav Pokorny (github.com/mP1)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package walkingkooka.net.header;

import walkingkooka.collect.map.Maps;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Map;

/**
 * Holds a link relation.<br>
 * <a href="https://tools.ietf.org/search/rfc5988"></a>
 */
public abstract class LinkRelation<T> extends HeaderValue2<T> implements Comparable<LinkRelation> {

    /**
     * Holds all constants.
     */
    private final static Map<String, LinkRelation> CONSTANTS = Maps.ordered();

    /**
     * Refers to a resource that is the subject of the link's context.
     */
    public final static LinkRelation ABOUT = registerConstant("about");

    /**
     * Refers to a substitute for this context
     */
    public final static LinkRelation ALTERNATE = registerConstant("alternate");

    /**
     * Refers to an appendix.
     */
    public final static LinkRelation APPENDIX = registerConstant("appendix");

    /**
     * Refers to a collection of records, documents, or other materials of historical interest.
     */
    public final static LinkRelation ARCHIVES = registerConstant("archives");

    /**
     * Refers to the context's author.
     */
    public final static LinkRelation AUTHOR = registerConstant("author");

    /**
     * Identifies the entity that blocks access to a resource following receipt of a legal demand.
     */
    public final static LinkRelation BLOCKED_BY = registerConstant("blocked-by");

    /**
     * Gives a permanent link to use for bookmarking purposes.
     */
    public final static LinkRelation BOOKMARK = registerConstant("bookmark");

    /**
     * Designates the preferred version of a resource (the IRI and its contents).
     */
    public final static LinkRelation CANONICAL = registerConstant("canonical");

    /**
     * Refers to a chapter in a collection of resources.
     */
    public final static LinkRelation CHAPTER = registerConstant("chapter");

    /**
     * Indicates that the link target is preferred over the link context for the purpose of referencing.
     */
    public final static LinkRelation CITE_AS = registerConstant("cite-as");

    /**
     * The target IRI points to a resource which represents the collection resource for the context IRI.
     */
    public final static LinkRelation COLLECTION = registerConstant("collection");

    /**
     * Refers to a table of contents.
     */
    public final static LinkRelation CONTENTS = registerConstant("contents");

    /**
     * The document linked to was later converted to the document that contains this link relation. For example, an RFC can have a link to the Internet-Draft that became the RFC; in that case, the link relation would be "convertedFrom".<br>This relation is different than "predecessor-version" in that "predecessor-version" is for items in a version control system. It is also different than "previous" in that this relation is used for converted resources, not those that are part of a sequence of resources.
     */
    public final static LinkRelation CONVERTEDFROM = registerConstant("convertedFrom");

    /**
     * Refers to a copyright statement that applies to the link's context.
     */
    public final static LinkRelation COPYRIGHT = registerConstant("copyright");

    /**
     * The target IRI points to a resource where a submission form can be obtained.
     */
    public final static LinkRelation CREATE_FORM = registerConstant("create-form");

    /**
     * Refers to a resource containing the most recent item(s) in a collection of resources.
     */
    public final static LinkRelation CURRENT = registerConstant("current");

    /**
     * Refers to a resource providing information about the link's context.
     */
    public final static LinkRelation DESCRIBEDBY = registerConstant("describedby");

    /**
     * The relationship A 'describes' B asserts that resource A provides a description of resource B. There are no constraints on the format or representation of either A or B, neither are there any further constraints on either resource.<br>This link relation type is the inverse of the 'describedby' relation type. While 'describedby' establishes a relation from the described resource back to the resource that describes it, 'describes' established a relation from the describing resource to the resource it describes. If B is 'describedby' A, then A 'describes' B.
     */
    public final static LinkRelation DESCRIBES = registerConstant("describes");

    /**
     * Refers to a list of patent disclosures made with respect to material for which 'disclosure' relation is specified.
     */
    public final static LinkRelation DISCLOSURE = registerConstant("disclosure");

    /**
     * Used to indicate an origin that will be used to fetch required resources for the link context, and that the user agent ought to resolve as early as possible.
     */
    public final static LinkRelation DNS_PREFETCH = registerConstant("dns-prefetch");

    /**
     * Refers to a resource whose available representations are byte-for-byte identical with the corresponding representations of the context IRI.<br>This relation is for static resources. That is, an HTTP GET request on any duplicate will return the same representation. It does not make sense for dynamic or POSTable resources and should not be used for them.
     */
    public final static LinkRelation DUPLICATE = registerConstant("duplicate");

    /**
     * Refers to a resource that can be used to edit the link's context.
     */
    public final static LinkRelation EDIT = registerConstant("edit");

    /**
     * The target IRI points to a resource where a submission form for editing associated resource can be obtained.
     */
    public final static LinkRelation EDIT_FORM = registerConstant("edit-form");

    /**
     * Refers to a resource that can be used to edit media associated with the link's context.
     */
    public final static LinkRelation EDIT_MEDIA = registerConstant("edit-media");

    /**
     * Identifies a related resource that is potentially large and might require special handling.
     */
    public final static LinkRelation ENCLOSURE = registerConstant("enclosure");

    /**
     * An IRI that refers to the furthest preceding resource in a series of resources.<br>This relation type registration did not indicate a reference. Originally requested by Mark Nottingham in December 2004.
     */
    public final static LinkRelation FIRST = registerConstant("first");

    /**
     * Refers to a glossary of terms.
     */
    public final static LinkRelation GLOSSARY = registerConstant("glossary");

    /**
     * Refers to context-sensitive help.
     */
    public final static LinkRelation HELP = registerConstant("help");

    /**
     * Refers to a resource hosted by the server indicated by the link context.<br>This relation is used in CoRE where links are retrieved as a "/.well-known/core" resource representation, and is the default relation type in the CoRE Link Format.
     */
    public final static LinkRelation HOSTS = registerConstant("hosts");

    /**
     * Refers to a hub that enables registration for notification of updates to the context.<br>This relation type was requested by Brett Slatkin.
     */
    public final static LinkRelation HUB = registerConstant("hub");

    /**
     * Refers to an icon representing the link's context.
     */
    public final static LinkRelation ICON = registerConstant("icon");

    /**
     * Refers to an index.
     */
    public final static LinkRelation INDEX = registerConstant("index");

    /**
     * refers to a resource associated with a time interval that ends before the beginning of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALAFTER = registerConstant("intervalAfter");

    /**
     * refers to a resource associated with a time interval that begins after the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALBEFORE = registerConstant("intervalBefore");

    /**
     * refers to a resource associated with a time interval that begins after the beginning of the time interval associated with the context resource, and ends before the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALCONTAINS = registerConstant("intervalContains");

    /**
     * refers to a resource associated with a time interval that begins after the end of the time interval associated with the context resource, or ends before the beginning of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALDISJOINT = registerConstant("intervalDisjoint");

    /**
     * refers to a resource associated with a time interval that begins before the beginning of the time interval associated with the context resource, and ends after the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALDURING = registerConstant("intervalDuring");

    /**
     * refers to a resource associated with a time interval whose beginning coincides with the beginning of the time interval associated with the context resource, and whose end coincides with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALEQUALS = registerConstant("intervalEquals");

    /**
     * refers to a resource associated with a time interval that begins after the beginning of the time interval associated with the context resource, and whose end coincides with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALFINISHEDBY = registerConstant("intervalFinishedBy");

    /**
     * refers to a resource associated with a time interval that begins before the beginning of the time interval associated with the context resource, and whose end coincides with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALFINISHES = registerConstant("intervalFinishes");

    /**
     * refers to a resource associated with a time interval that begins before or is coincident with the beginning of the time interval associated with the context resource, and ends after or is coincident with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALIN = registerConstant("intervalIn");

    /**
     * refers to a resource associated with a time interval whose beginning coincides with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALMEETS = registerConstant("intervalMeets");

    /**
     * refers to a resource associated with a time interval whose beginning coincides with the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALMETBY = registerConstant("intervalMetBy");

    /**
     * refers to a resource associated with a time interval that begins before the beginning of the time interval associated with the context resource, and ends after the beginning of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALOVERLAPPEDBY = registerConstant("intervalOverlappedBy");

    /**
     * refers to a resource associated with a time interval that begins before the end of the time interval associated with the context resource, and ends after the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALOVERLAPS = registerConstant("intervalOverlaps");

    /**
     * refers to a resource associated with a time interval whose beginning coincides with the beginning of the time interval associated with the context resource, and ends before the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALSTARTEDBY = registerConstant("intervalStartedBy");

    /**
     * refers to a resource associated with a time interval whose beginning coincides with the beginning of the time interval associated with the context resource, and ends after the end of the time interval associated with the context resource
     */
    public final static LinkRelation INTERVALSTARTS = registerConstant("intervalStarts");

    /**
     * The target IRI points to a resource that is a member of the collection represented by the context IRI.
     */
    public final static LinkRelation ITEM = registerConstant("item");

    /**
     * An IRI that refers to the furthest following resource in a series of resources.<br>This relation type registration did not indicate a reference. Originally requested by Mark Nottingham in December 2004.
     */
    public final static LinkRelation LAST = registerConstant("last");

    /**
     * Points to a resource containing the latest (e.g., current) version of the context.
     */
    public final static LinkRelation LATEST_VERSION = registerConstant("latest-version");

    /**
     * Refers to a license associated with this context.<br>For implications of use in HTML, see: http://www.w3.org/TR/html5/links.html#link-type-license
     */
    public final static LinkRelation LICENSE = registerConstant("license");

    /**
     * Refers to further information about the link's context, expressed as a LRDD ("Link-based Resource Descriptor Document") resource. See for information about processing this relation type in host-meta documents. When used elsewhere, it refers to additional links and other metadata. Multiple instances indicate additional LRDD resources. LRDD resources MUST have an "application/xrd+xml" representation, and MAY have others.
     */
    public final static LinkRelation LRDD = registerConstant("lrdd");

    /**
     * The Target IRI points to a Memento, a fixed resource that will not change state anymore.<br>A Memento for an Original Resource is a resource that encapsulates a prior state of the Original Resource.
     */
    public final static LinkRelation MEMENTO = registerConstant("memento");

    /**
     * Refers to a resource that can be used to monitor changes in an HTTP resource.
     */
    public final static LinkRelation MONITOR = registerConstant("monitor");

    /**
     * Refers to a resource that can be used to monitor changes in a specified group of HTTP resources.
     */
    public final static LinkRelation MONITOR_GROUP = registerConstant("monitor-group");

    /**
     * Indicates that the link's context is a part of a series, and that the next in the series is the link target.
     */
    public final static LinkRelation NEXT = registerConstant("next");

    /**
     * Refers to the immediately following archive resource.
     */
    public final static LinkRelation NEXT_ARCHIVE = registerConstant("next-archive");

    /**
     * Indicates that the context’s original author or publisher does not endorse the link target.
     */
    public final static LinkRelation NOFOLLOW = registerConstant("nofollow");

    /**
     * Indicates that no referrer information is to be leaked when following the link.
     */
    public final static LinkRelation NOREFERRER = registerConstant("noreferrer");

    /**
     * The Target IRI points to an Original Resource.<br>An Original Resource is a resource that exists or used to exist, and for which access to one of its prior states may be required.
     */
    public final static LinkRelation ORIGINAL = registerConstant("original");

    /**
     * Indicates a resource where payment is accepted.<br>This relation type registration did not indicate a reference. Requested by Joshua Kinberg and Robert Sayre. It is meant as a general way to facilitate acts of payment, and thus this specification makes no assumptions on the type of payment or transaction protocol. Examples may include a web page where donations are accepted or where goods and services are available for purchase. rel="payment" is not intended to initiate an automated transaction. In Atom documents, a link element with a rel="payment" attribute may exist at the feed/channel level and/or the entry/item level. For example, a rel="payment" link at the feed/channel level may point to a "tip jar" URI, whereas an entry/ item containing a book review may include a rel="payment" link that points to the location where the book may be purchased through an online retailer.
     */
    public final static LinkRelation PAYMENT = registerConstant("payment");

    /**
     * Gives the address of the pingback resource for the link context.
     */
    public final static LinkRelation PINGBACK = registerConstant("pingback");

    /**
     * Used to indicate an origin that will be used to fetch required resources for the link context. Initiating an early connection, which includes the DNS lookup, TCP handshake, and optional TLS negotiation, allows the user agent to mask the high latency costs of establishing a connection.
     */
    public final static LinkRelation PRECONNECT = registerConstant("preconnect");

    /**
     * Points to a resource containing the predecessor version in the version history.
     */
    public final static LinkRelation PREDECESSOR_VERSION = registerConstant("predecessor-version");

    /**
     * The prefetch link relation type is used to identify a resource that might be required by the next navigation from the link context, and that the user agent ought to fetch, such that the user agent can deliver a faster response once the resource is requested in the future.
     */
    public final static LinkRelation PREFETCH = registerConstant("prefetch");

    /**
     * Refers to a resource that should be loaded early in the processing of the link's context, without blocking rendering.<br>Additional target attributes establish the detailed fetch properties of the link.
     */
    public final static LinkRelation PRELOAD = registerConstant("preload");

    /**
     * Used to identify a resource that might be required by the next navigation from the link context, and that the user agent ought to fetch and execute, such that the user agent can deliver a faster response once the resource is requested in the future.
     */
    public final static LinkRelation PRERENDER = registerConstant("prerender");

    /**
     * Indicates that the link's context is a part of a series, and that the previous in the series is the link target.
     */
    public final static LinkRelation PREV = registerConstant("prev");

    /**
     * Refers to a resource that provides a preview of the link's context.
     */
    public final static LinkRelation PREVIEW = registerConstant("preview");

    /**
     * Refers to the previous resource in an ordered series of resources. Synonym for "prev".
     */
    public final static LinkRelation PREVIOUS = registerConstant("previous");

    /**
     * Refers to the immediately preceding archive resource.
     */
    public final static LinkRelation PREV_ARCHIVE = registerConstant("prev-archive");

    /**
     * Refers to a privacy policy associated with the link's context.
     */
    public final static LinkRelation PRIVACY_POLICY = registerConstant("privacy-policy");

    /**
     * Identifying that a resource representation conforms to a certain profile, without affecting the non-profile semantics of the resource representation.<br>Profile URIs are primarily intended to be used as identifiers, and thus clients SHOULD NOT indiscriminately access profile URIs.
     */
    public final static LinkRelation PROFILE = registerConstant("profile");

    /**
     * Identifies a related resource.
     */
    public final static LinkRelation RELATED = registerConstant("related");

    /**
     * Identifies the root of RESTCONF API as configured on this HTTP server. The "restconf" relation defines the root of the API defined in RFC8040. Subsequent revisions of RESTCONF will use alternate relation values to support protocol versioning.
     */
    public final static LinkRelation RESTCONF = registerConstant("restconf");

    /**
     * Identifies a resource that is a reply to the context of the link.
     */
    public final static LinkRelation REPLIES = registerConstant("replies");

    /**
     * Refers to a resource that can be used to search through the link's context and related resources.
     */
    public final static LinkRelation SEARCH = registerConstant("search");

    /**
     * Refers to a section in a collection of resources.
     */
    public final static LinkRelation SECTION = registerConstant("section");

    /**
     * Conveys an identifier for the link's context.
     */
    public final static LinkRelation SELF = registerConstant("self");

    /**
     * Indicates a URI that can be used to retrieve a service document.<br>When used in an Atom document, this relation type specifies Atom Publishing Protocol service documents by default. Requested by James Snell.
     */
    public final static LinkRelation SERVICE = registerConstant("service");

    /**
     * Refers to the first resource in a collection of resources.
     */
    public final static LinkRelation START = registerConstant("start");

    /**
     * Refers to a stylesheet.
     */
    public final static LinkRelation STYLESHEET = registerConstant("stylesheet");

    /**
     * Refers to a resource serving as a subsection in a collection of resources.
     */
    public final static LinkRelation SUBSECTION = registerConstant("subsection");

    /**
     * Points to a resource containing the successor version in the version history.
     */
    public final static LinkRelation SUCCESSOR_VERSION = registerConstant("successor-version");

    /**
     * Gives a tag (identified by the given address) that applies to the current document.
     */
    public final static LinkRelation TAG = registerConstant("tag");

    /**
     * Refers to the terms of service associated with the link's context.
     */
    public final static LinkRelation TERMS_OF_SERVICE = registerConstant("terms-of-service");

    /**
     * The Target IRI points to a TimeGate for an Original Resource.<br>A TimeGate for an Original Resource is a resource that is capable of datetime negotiation to support access to prior states of the Original Resource.
     */
    public final static LinkRelation TIMEGATE = registerConstant("timegate");

    /**
     * The Target IRI points to a TimeMap for an Original Resource.<br>A TimeMap for an Original Resource is a resource from which a list of URIs of Mementos of the Original Resource is available.
     */
    public final static LinkRelation TIMEMAP = registerConstant("timemap");

    /**
     * Refers to a resource identifying the abstract semantic type of which the link's context is considered to be an instance.
     */
    public final static LinkRelation TYPE = registerConstant("type");

    /**
     * Refers to a parent document in a hierarchy of documents. <br>This relation type registration did not indicate a reference. Requested by Noah Slater.
     */
    public final static LinkRelation UP = registerConstant("up");

    /**
     * Points to a resource containing the version history for the context.
     */
    public final static LinkRelation VERSION_HISTORY = registerConstant("version-history");

    /**
     * Identifies a resource that is the source of the information in the link's context.
     */
    public final static LinkRelation VIA = registerConstant("via");

    /**
     * Identifies a target URI that supports the Webmention protcol. This allows clients that mention a resource in some form of publishing process to contact that endpoint and inform it that this resource has been mentioned.<br>This is a similar "Linkback" mechanism to the ones of Refback, Trackback, and Pingback. It uses a different protocol, though, and thus should be discoverable through its own link relation type.
     */
    public final static LinkRelation WEBMENTION = registerConstant("webmention");

    /**
     * Points to a working copy for this resource.
     */
    public final static LinkRelation WORKING_COPY = registerConstant("working-copy");

    /**
     * Points to the versioned resource from which this working copy was obtained.
     */
    public final static LinkRelation WORKING_COPY_OF = registerConstant("working-copy-of");

    /**
     * Registers a constant.
     */
    private static LinkRelation registerConstant(final String text) {
        final LinkRelation linkRelation = LinkRelationRegular.regular(text);
        CONSTANTS.put(text, linkRelation);
        return linkRelation;
    }

    /**
     * Uses the text to find a constant or then create a {@link LinkRelationUrl} or {@link LinkRelationRegular}.
     */
    public static LinkRelation<?> with(final String value) {
        CharSequences.failIfNullOrEmpty(value, "value");

        LinkRelation relation = CONSTANTS.get(value);
        if (null == relation) {
            try {
                relation = LinkRelationUrl.url(value);
            } catch (final RuntimeException cause) {
                relation = LinkRelationRegular.regular(value);
            }
        }

        return relation;
    }

    /**
     * Parses the header text into a list of {@link LinkRelation}.
     */
    public static List<LinkRelation<?>> parse(final String text) {
        return LinkRelationHeaderValueConverter.INSTANCE.parse(text, null);
    }

    /**
     * Converts the list of relations into header text.
     */
    public static String toHeaderTextList(final List<LinkRelation<?>> relations) {
        return HeaderValue.toHeaderTextList(relations, " ");
    }

    /**
     * Package private
     */
    LinkRelation(final T value) {
        super(value);
    }

    /**
     * Returns true if the value is an absolute url.
     */
    abstract public boolean isUrl();

    /**
     * Always returns false.
     */
    public final boolean isWildcard() {
        return false;
    }

    // HasHeaderScope ....................................................................................................

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }

    // Comparable...........................................................................................................

    @Override
    public final int compareTo(final LinkRelation other) {
        return this.toHeaderText().compareTo(other.toHeaderText());
    }
}
