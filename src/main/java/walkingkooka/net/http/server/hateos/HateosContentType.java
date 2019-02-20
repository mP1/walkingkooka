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

import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.tree.Node;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.xml.HasXmlNode;
import walkingkooka.tree.xml.XmlNode;

import javax.xml.parsers.DocumentBuilder;
import java.util.Collection;

/**
 * Controls the content type of hateos messages. Ideally this should have been an enum but currently enums do not
 * support type parameters.
 */
public abstract class HateosContentType<N extends Node<N, ?, ?, ?>, V> {

    /**
     * Selects JSON formatted bodies.
     */
    public final static HateosContentType<JsonNode, HasJsonNode> JSON = HateosContentTypeJsonNode.instance();

    /**
     * Selects XML formatted bodies.
     */
    public final static HateosContentType<XmlNode, HasXmlNode> XML = HateosContentTypeXmlNode.instance();

    /**
     * Package private use constants.
     */
    HateosContentType() {
        super();
    }

    /**
     * Returns the {@link MediaType content type}.
     */
    public abstract MediaType contentType();

    /**
     * Parses the text into a {@link Node}
     */
    abstract N parse(final DocumentBuilder documentBuilder, final String text);

    /**
     * Formats the node as text.
     */
    abstract String toText(final N node);

    /**
     * Helper called by {@link HateosHandlerBuilderRouterHateosHandlerContext}.
     */
    abstract N addLinks(final Comparable<?> id,
                        final N node,
                        final HttpMethod method,
                        final AbsoluteUrl base,
                        final HateosResourceName resourceName,
                        final Collection<LinkRelation<?>> linkRelations);

    /**
     * Converts the given value into a {@link Node}.
     */
    public abstract N toNode(final V value);

    abstract public String toString();
}
