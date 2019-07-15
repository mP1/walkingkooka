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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.RelativeUrl;
import walkingkooka.net.Url;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonNodeException;
import walkingkooka.tree.json.JsonNodeName;
import walkingkooka.tree.xml.XmlNode;
import walkingkooka.type.JavaVisibility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LinkTest extends HeaderValueWithParametersTestCase<Link,
        LinkParameterName<?>>
        implements HasJsonNodeTesting<Link>,
        ParseStringTesting<List<Link>> {

    @Test
    public void testWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            Link.with(null);
        });
    }

    @Test
    public void testWithAbsoluteUrl() {
        final AbsoluteUrl url = this.value();
        this.check(Link.with(url), url, Link.NO_PARAMETERS);
    }

    @Test
    public void testWithRelativeUrl() {
        final RelativeUrl url = Url.parseRelative("/path/file");
        this.check(Link.with(url), url, Link.NO_PARAMETERS);
    }

    @Test
    public void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createLink().setValue(null);
        });
    }

    @Test
    public void testSetValueSame() {
        final Link link = this.createLink();
        assertSame(link, link.setValue(link.value()));
    }

    @Test
    public void testSetValueSame2() {
        final Link link = this.createLink();
        assertSame(link, link.setValue(this.value()));
    }

    @Test
    public void testSetValueDifferent() {
        final Link link = this.createLink();
        final AbsoluteUrl value = Url.parseAbsolute("http://example2.com");
        final Link different = link.setValue(value);
        assertNotSame(link, different);

        this.check(different, value, Link.NO_PARAMETERS);
    }

    @Test
    public final void testSetParameterDifferent() {
        final Link link = this.createLink();
        final Map<LinkParameterName<?>, Object> parameters = this.parameters();
        final Link different = link.setParameters(parameters);
        this.check(different, link.value(), parameters);
    }

    @Test
    public void testSetParametersDifferentAndBack() {
        final Link link = this.createLink();
        assertEquals(link,
                link
                        .setParameters(this.parameters())
                        .setParameters(Link.NO_PARAMETERS));
    }

    private Map<LinkParameterName<?>, Object> parameters() {
        return Maps.of(LinkParameterName.REL, LinkRelation.parse("previous"));
    }

    final void check(final Link language,
                     final Url value,
                     final Map<LinkParameterName<?>, Object> parameters) {
        assertEquals(value, language.value(), "value");
        this.checkParameters(language, parameters);
    }


    // toHeaderTextList.......................................................................................

    @Test
    public void testToHeaderTextListListOfOne() {
        this.toHeaderTextListAndCheck("<http://example.com>",
                this.createLink());
    }

    @Test
    public void testToHeaderTextListListOfOneWithParameters() {
        this.toHeaderTextListAndCheck("<http://example.com>;rel=previous",
                this.createLink().setParameters(Maps.of(LinkParameterName.REL, LinkRelation.parse("previous"))));
    }

    @Test
    public void testToHeaderTextListListOfMany() {
        this.toHeaderTextListAndCheck("<http://example.com>, <http://example2.com>",
                this.createLink(),
                Link.with(Url.parse("http://example2.com")));
    }

    // toJsonNode .....................................................................................................

    @Test
    public void testFromJsonNodeBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true), JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeNumberFails() {
        this.fromJsonNodeFails(JsonNode.number(123), JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeStringFails() {
        this.fromJsonNodeFails(JsonNode.string("fails!"), JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeArrayFails() {
        this.fromJsonNodeFails(JsonNode.array(), JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeObjectEmptyFails() {
        this.fromJsonNodeFails(JsonNode.object(), JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeHrefNonStringFails() {
        this.fromJsonNodeFails(JsonNode.object().set(Link.HREF_JSON_PROPERTY, JsonNode.number(123)),
                JsonNodeException.class);
    }

    @Test
    public void testFromJsonNodeUnknownPropertyFails() {
        this.fromJsonNodeFails(JsonNode.object().set(JsonNodeName.with("unknown-property"), JsonNode.number(123)),
                JsonNodeException.class);
    }

    @Test
    public void testFromJsonLink() {
        final String href = "http://example.com";
        this.fromJsonNodeAndCheck(JsonNode.object().set(Link.HREF_JSON_PROPERTY, JsonNode.string(href)),
                Link.with(Url.parse(href)));
    }

    @Test
    public void testToJsonNode() {
        this.toJsonNodeAndCheck2("<http://example.com>",
                "{\"href\": \"http://example.com\"}");
    }

    @Test
    public void testToJsonNodeRel() {
        this.toJsonNodeAndCheck2("<http://example.com>;type=text/plain;rel=previous",
                "{\"href\": \"http://example.com\", \"rel\": \"previous\", \"type\": \"text/plain\"}");
    }

    private void toJsonNodeAndCheck2(final String link, final String json) {
        this.toJsonNodeAndCheck(Link.parse(link).get(0), json);
    }

    // toXmlNode .......................................................................................

    @Test
    public void testToXmlNode() throws Exception {
        this.toXmlNodeAndCheck("<http://example.com>",
                "<link href=\"http://example.com\"/>");
    }

    @Test
    public void testToXmlNodeRel() throws Exception {
        this.toXmlNodeAndCheck("<http://example.com>;type=text/plain;rel=previous",
                "<link href=\"http://example.com\" rel=\"previous\" type=\"text/plain\"/>");
    }

    private void toXmlNodeAndCheck(final String link, final String xml) throws Exception {
        assertEquals(XmlNode.fromXml(documentBuilder(), new StringReader(xml)).element().get(),
                Link.parse(link).get(0).toXmlNode(),
                "toXmlNode doesnt match=" + CharSequences.quoteAndEscape(link));
    }

    private DocumentBuilder documentBuilder() {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setExpandEntityReferences(false);
            return factory.newDocumentBuilder();
        } catch (final Exception cause) {
            throw new Error(cause);
        }
    }

    // HashCodeEqualsDefined ..................................................................................................

    @Test
    public void testEqualsDifferentUrl() {
        this.checkNotEquals(Link.with(Url.parse("/different")).setParameters(this.parameters()));
    }

    @Test
    public void testEqualsDifferentParameters() {
        this.checkNotEquals(this.createLink().setParameters(Maps.of(LinkParameterName.TYPE, MediaType.TEXT_PLAIN)));
    }

    @Test
    public void testEqualsDifferentParameters2() {
        this.checkNotEquals(this.createLink().setParameters(Maps.of(LinkParameterName.TYPE, MediaType.TEXT_PLAIN)),
                this.createLink().setParameters(Maps.of(LinkParameterName.TYPE, MediaType.BINARY)));
    }

    // parse.......................................................................................

    @Test
    public void testParseLinkWithMedia() {
        this.parseAndCheck("<http://example.com>;media=\"abc 123\"",
                this.createLink().setParameters(Maps.of(LinkParameterName.MEDIA, "abc 123")));
    }

    @Test
    public void testParseLinkWithMethod() {
        this.parseAndCheck("<http://example.com>;method=GET",
                this.createLink().setParameters(Maps.of(LinkParameterName.METHOD, HttpMethod.GET)));
    }

    @Test
    public void testParseLinkWithType() {
        this.parseAndCheck("<http://example.com>;type=text/plain",
                this.createLink().setParameters(Maps.of(LinkParameterName.TYPE, MediaType.TEXT_PLAIN)));
    }

    @Test
    public void testParseSeveralLinks() {
        this.parseAndCheck("<http://example.com>;rel=previous, <http://example2.com>",
                this.createLink().setParameters(Maps.of(LinkParameterName.REL, LinkRelation.parse("previous"))),
                Link.with(Url.parse("http://example2.com"))
        );
    }

    private void parseAndCheck(final String text, final Link... links) {
        this.parseAndCheck(text, Lists.of(links));
    }

    // helpers.......................................................................................

    @Override
    public Link createHeaderValueWithParameters() {
        return this.createLink();
    }

    private Link createLink() {
        return Link.with(this.value());
    }

    private AbsoluteUrl value() {
        return Url.parseAbsolute("http://example.com");
    }

    @Override
    LinkParameterName<?> parameterName() {
        return LinkParameterName.with("xyz");
    }

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public Class<Link> type() {
        return Link.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // ParseStringTesting ........................................................................................

    @Override
    public List<Link> parse(final String text) {
        return Link.parse(text);
    }

    // HasJsonNodeTesting..................................................................

    @Override
    public Link createHasJsonNode() {
        return this.createLink();
    }

    @Override
    public Link fromJsonNode(final JsonNode from) {
        return Link.fromJsonNode(from);
    }
}
