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
import java.util.Map;

/**
 * The {@link HateosContentType} that handles {@link JsonNode}.
 */
final class HateosContentTypeJsonNode<V extends HasJsonNode> extends HateosContentType<JsonNode, V> {

    /**
     * Singleton
     */
    @SuppressWarnings("unchecked")
    static <V extends HasJsonNode> HateosContentTypeJsonNode<V> instance() {
        return INSTANCE;
    }

    private final static HateosContentTypeJsonNode INSTANCE = new HateosContentTypeJsonNode<>();

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


    @Override
    JsonNode parse(final DocumentBuilder documentBuilder,
                   final String text) {
        return JsonNode.parse(text);
    }

    @Override
    String toText(final JsonNode node) {
        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(Printers.stringBuilder(b, LineEnding.SYSTEM));
        node.printJson(printer);
        return b.toString();
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
    JsonNode addLinks(final Comparable<?> id,
                      final JsonNode node,
                      final HttpMethod method,
                      final AbsoluteUrl base,
                      final HateosResourceName resourceName,
                      final Collection<LinkRelation<?>> linkRelations) {

        // base + resource name.
        final UrlPath pathAndResourceNameAndId = base.path()
                .append(UrlPathName.with(resourceName.value()))
                .append(UrlPathName.with(id.toString()));

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

        return JsonObjectNode.class.cast(node).set(LINKS, links);
    }

    /**
     * The property that receives the actual links.
     */
    private final static JsonNodeName LINKS = JsonNodeName.with("_links");

    /**
     * Converts the given value into a {@link JsonNode}.
     */
    public JsonNode toNode(final HasJsonNode value) {
        return value.toJsonNode();
    }

    @Override
    public String toString() {
        return "JSON";
    }
}
