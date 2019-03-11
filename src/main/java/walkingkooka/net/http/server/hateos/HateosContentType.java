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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.xml.XmlNode;

import javax.xml.parsers.DocumentBuilder;
import java.util.Collection;
import java.util.List;

/**
 * Controls the content type of hateos messages. Ideally this should have been an enum but currently enums do not
 * support type parameters.
 */
public abstract class HateosContentType<N extends Node<N, ?, ?, ?>> {

    /**
     * Selects JSON formatted request and response bodies.
     */
    public static HateosContentType<JsonNode> json() {
        return HateosContentTypeJsonNode.INSTANCE;
    }

    /**
     * Selects XML formatted request and response bodies.
     */
    public static HateosContentType<XmlNode> xml() {
        return HateosContentTypeXmlNode.INSTANCE;
    }

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
     * Reads a resource object from its {@link Node} representation.
     */
    abstract <R extends HateosResource<?>> R fromNode(final String text,
                                                      final DocumentBuilder documentBuilder,
                                                      final Class<R> resourceType);

    /**
     * Reads a list of resource objects from their {@link Node} representation.
     */
    abstract <R extends HateosResource<?>> List<R> fromNodeList(final String text,
                                                                final DocumentBuilder documentBuilder,
                                                                final Class<R> resourceType);

    /**
     * Adds links to the resource, converts it to a text.
     */
    abstract <R extends HateosResource<?>> String toText(final R resource,
                                                         final DocumentBuilder documentBuilder,
                                                         final HttpMethod method,
                                                         final AbsoluteUrl base,
                                                         final HateosResourceName resourceName,
                                                         final Collection<LinkRelation<?>> linkRelations);

    /**
     * Adds links to the resource, converts it to a text.
     */
    abstract <R extends HateosResource<?>> String toTextList(final List<R> resource,
                                                             final DocumentBuilder documentBuilder,
                                                             final HttpMethod method,
                                                             final AbsoluteUrl base,
                                                             final HateosResourceName resourceName,
                                                             final Collection<LinkRelation<?>> linkRelations);

    abstract public String toString();
}
