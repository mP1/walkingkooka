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
import walkingkooka.io.printer.IndentingPrinter;
import walkingkooka.io.printer.IndentingPrinters;
import walkingkooka.io.printer.Printers;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.UrlPath;
import walkingkooka.net.UrlPathName;
import walkingkooka.net.header.Link;
import walkingkooka.net.header.LinkParameterName;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.header.MediaType;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonArrayNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.json.JsonObjectNode;

import javax.xml.parsers.DocumentBuilder;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The {@link HateosContentType} that handles {@link JsonNode}.
 */
final class HateosContentTypeJsonNode extends HateosContentType<JsonNode> {

    /**
     * Singleton
     */
    final static HateosContentTypeJsonNode INSTANCE = new HateosContentTypeJsonNode();

    /**
     * Private ctor use singleton.
     */
    private HateosContentTypeJsonNode() {
        super();
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    private final static MediaType CONTENT_TYPE = MediaType.with("application", "hal+json");

    /**
     * Reads a resource object from its {@link JsonNode} representation.
     */
    @Override
    <R extends HateosResource<?>> R fromNode(final String text,
                                             final DocumentBuilder documentBuilder,
                                             final Class<R> resourceType) {
        return JsonNode.parse(text)
                .fromJsonNode(resourceType);
    }

    /**
     * Reads a list of resource objects from their {@link JsonNode} representation.
     */
    @Override
    <R extends HateosResource<?>> List<R> fromNodeList(final String text,
                                                       final DocumentBuilder documentBuilder,
                                                       final Class<R> resourceType) {
        return JsonNode.parse(text).fromJsonNodeList(resourceType);
    }

    /**
     * The format for hateos urls is base + "/" + resource name + "/" + id + "/" + link relation
     * <a href="https://en.wikipedia.org/wiki/Hypertext_Application_Language"></a>
     * <pre>
     * "_links": {
     *   "self": {
     *     "href": "http://example.com/api/book/hal-cookbook"
     *   }
     * },
     * </pre>
     */
    @Override
    <R extends HateosResource<?>> String toText(final R resource,
                                                final DocumentBuilder documentBuilder,
                                                final HttpMethod method,
                                                final AbsoluteUrl base,
                                                final HateosResourceName resourceName,
                                                final Collection<LinkRelation<?>> linkRelations) {
        return toJsonText(addLinks(resource, method, base, resourceName, linkRelations));
    }

    /**
     * Converts the count to text holding JSON.
     */
    @Override
    String toTextValue(final Object value,
                       final DocumentBuilder documentBuilder) {
        return HasJsonNode.toJsonNodeObject(value).toString();
    }

    @Override
    <R extends HateosResource<?>> String toTextList(final List<R> resources,
                                                    final DocumentBuilder documentBuilder,
                                                    final HttpMethod method,
                                                    final AbsoluteUrl base,
                                                    final HateosResourceName resourceName,
                                                    final Collection<LinkRelation<?>> linkRelations) {
        return toJsonText(
                JsonNode.array().setChildren(resources.
                        stream()
                        .map(r -> addLinks(r, method, base, resourceName, linkRelations))
                        .collect(Collectors.toList())));
    }

    private <R extends HateosResource<?>> JsonNode addLinks(final R resource,
                                                            final HttpMethod method,
                                                            final AbsoluteUrl base,
                                                            final HateosResourceName resourceName,
                                                            final Collection<LinkRelation<?>> linkRelations) {
        final JsonNode node = resource.toJsonNode();
        return node.isObject() ?
                this.addLinks0(resource, node.objectOrFail(), method, base, resourceName, linkRelations) :
                node;

    }

    private <R extends HateosResource<?>> JsonNode addLinks0(final R resource,
                                                             final JsonObjectNode object,
                                                             final HttpMethod method,
                                                             final AbsoluteUrl base,
                                                             final HateosResourceName resourceName,
                                                             final Collection<LinkRelation<?>> linkRelations) {
        // base + resource name.
        final UrlPath pathAndResourceNameAndId = base.path()
                .append(UrlPathName.with(resourceName.value()))
                .append(UrlPathName.with(resource.hateosLinkId()));

        JsonArrayNode links = JsonNode.array();

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

            links = links.appendChild(link.toJsonNode());
        }

        return object.set(LINKS, links);
    }

    /**
     * The property that receives the actual links.
     */
    private final static JsonNodeName LINKS = JsonNodeName.with("_links");

    private String toJsonText(final JsonNode node) {
        final StringBuilder b = new StringBuilder();

        try(final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.SYSTEM))) {
            node.printJson(printer);
            printer.flush();
        }
        return b.toString();
    }

    @Override
    public String toString() {
        return "JSON";
    }
}
