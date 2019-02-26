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
import walkingkooka.tree.xml.HasXmlNode;
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

final class HateosContentTypeXmlNode<V extends HasXmlNode> extends HateosContentType<XmlNode, V> {

    /**
     * Singleton
     */
    @SuppressWarnings("unchecked")
    final static <V extends HasXmlNode> HateosContentTypeXmlNode<V> instance() {
        return INSTANCE;
    }

    private final static HateosContentTypeXmlNode INSTANCE = new HateosContentTypeXmlNode<>();


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

    @Override
    XmlNode parse(final DocumentBuilder documentBuilder,
                  final String text) {
        try {
            return XmlNode.fromXml(documentBuilder, new StringReader(text));
        } catch (final Exception cause) {
            throw new HttpServerException(cause.getMessage(), cause);
        }
    }

    @Override
    String toText(final XmlNode node) {
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

    @Override
    XmlNode addLinks(final Comparable<?> id,
                     final XmlNode node,
                     final HttpMethod method,
                     final AbsoluteUrl base,
                     final HateosResourceName resourceName,
                     final Collection<LinkRelation<?>> linkRelations) {

        final XmlDocument document = node.document();

        // base + resource name.
        final UrlPath pathAndResourceNameAndId = base.path()
                .append(UrlPathName.with(resourceName.value()))
                .append(UrlPathName.with(id.toString()));

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

    /**
     * The xml element that receives the actual links.
     */
    private final static XmlName LINKS = XmlName.element("links");

    /**
     * Converts the given value into a {@link XmlNode}.
     */
    public XmlNode toNode(final HasXmlNode value) {
        return value.toXmlNode();
    }

    @Override
    public String toString() {
        return "XML";
    }
}
