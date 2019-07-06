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

package walkingkooka.net.http.server.hateos;

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.Link;
import walkingkooka.net.header.LinkParameterName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.net.http.server.HttpServerException;
import walkingkooka.tree.Node;
import walkingkooka.tree.xml.XmlDocument;
import walkingkooka.tree.xml.XmlName;
import walkingkooka.tree.xml.XmlNode;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class HateosContentTypeXmlNode extends HateosContentType<XmlNode> {

    /**
     * Singleton
     */
    final static HateosContentTypeXmlNode INSTANCE = new HateosContentTypeXmlNode();


    /**
     * Private ctor use singleton.
     */
    private HateosContentTypeXmlNode() {
        super();
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    private final static MediaType CONTENT_TYPE = MediaType.with("application", "hal+xml");

    /**
     * Reads a resource object from its {@link XmlNode} representation.
     */
    @Override <R extends HateosResource<?>> R fromNode(final String text,
                                                       final DocumentBuilder documentBuilder,
                                                       final Class<R> resourceType) {
        return fromXmlNode(parseXml(documentBuilder, text), resourceType);
    }


    /**
     * Reads a list of resource objects from their {@link Node} representation.
     */
    @Override <R extends HateosResource<?>> List<R> fromNodeList(final String text,
                                                                 final DocumentBuilder documentBuilder,
                                                                 final Class<R> resourceType) {
        return parseXml(documentBuilder, text)
                .children()
                .stream()
                .map(c -> fromXmlNode(c, resourceType))
                .collect(Collectors.toList());
    }

    private XmlNode parseXml(final DocumentBuilder documentBuilder,
                             final String text) {
        try {
            return XmlNode.fromXml(documentBuilder, new StringReader(text));
        } catch (final Exception cause) {
            throw new HttpServerException(cause.getMessage(), cause);
        }
    }

    private <R extends HateosResource<?>, I extends Comparable<I>> R fromXmlNode(final XmlNode node, final Class<R> resourceType) {
        throw new UnsupportedOperationException();
    }


    @Override <R extends HateosResource<?>> String toText(final R resource,
                                                          final DocumentBuilder documentBuilder,
                                                          final HttpMethod method,
                                                          final AbsoluteUrl base,
                                                          final HateosResourceName resourceName,
                                                          final Collection<LinkRelation<?>> linkRelations) {
        return toXmlText(addLinks(resource, method, base, resourceName, linkRelations));
    }

    /**
     * Converts the value to text with XML.
     */
    @Override
    String toTextValue(final Object value,
                       final DocumentBuilder documentBuilder) {
        throw new UnsupportedOperationException();
    }

    @Override <R extends HateosResource<?>> String toTextList(final List<R> resources,
                                                              final DocumentBuilder documentBuilder,
                                                              final HttpMethod method,
                                                              final AbsoluteUrl base,
                                                              final HateosResourceName resourceName,
                                                              final Collection<LinkRelation<?>> linkRelations) {
        return toXmlText(
                XmlNode.createDocument(documentBuilder)
                        .createElement(XmlName.element("list"))
                        .setChildren(resources.stream()
                                .map(r -> addLinks(r, method, base, resourceName, linkRelations))
                                .collect(Collectors.toList())));
    }

    private <R extends HateosResource<?>> XmlNode addLinks(final R resource,
                                                           final HttpMethod method,
                                                           final AbsoluteUrl base,
                                                           final HateosResourceName resourceName,
                                                           final Collection<LinkRelation<?>> linkRelations) {
        final XmlNode node = resource.toXmlNode();
        final XmlDocument document = node.document();

        // base + resource name.
        final UrlPath pathAndResourceNameAndId = base.path()
                .append(UrlPathName.with(resourceName.value()))
                .append(UrlPathName.with(resource.idForHateosLink()));

        final List<XmlNode> links = Lists.array();

        for (LinkRelation<?> relation : linkRelations) {
            // TODO add support for title/title* and hreflang
            final Map<LinkParameterName<?>, Object> parameters = Maps.ordered();
            parameters.put(LinkParameterName.METHOD, method);
            parameters.put(LinkParameterName.REL, Lists.of(relation));
            parameters.put(LinkParameterName.TYPE, CONTENT_TYPE);

            final Link link = Link.with(base.setPath(LinkRelation.SELF == relation ?
                    pathAndResourceNameAndId :
                    pathAndResourceNameAndId.append(UrlPathName.with(relation.value().toString()))))
                    .setParameters(parameters);

            links.add(link.toXmlNode());
        }

        return node.appendChild(
                document.createElement(LINKS)
                        .setChildren(links));
    }

    private String toXmlText(final XmlNode node) {
        try {
            final StringWriter writer = new StringWriter();
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            node.toXml(transformer, writer);
            writer.flush();
            return writer.toString();
        } catch (final TransformerException cause) {
            throw new HttpServerException(cause.getMessage(), cause);
        }
    }

    /**
     * The xml element that receives the actual links.
     */
    private final static XmlName LINKS = XmlName.element("links");

    @Override
    public String toString() {
        return "XML";
    }
}
