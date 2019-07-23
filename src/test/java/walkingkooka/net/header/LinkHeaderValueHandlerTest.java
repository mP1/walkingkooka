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
import walkingkooka.net.Url;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class LinkHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<LinkHeaderValueHandler, List<Link>> {

    @Override
    public String typeNamePrefix() {
        return Link.class.getSimpleName();
    }

    // http://www.rfc-editor.org/rfc/rfc5988.txt

    // </TheBook/chapter2>;
    @Test
    public void testParseLink() {
        this.parseAndToTextAndCheck2("</TheBook/chapter2>",
                Link.with(Url.parse("/TheBook/chapter2")));
    }

    // </TheBook/chapter2>;rel="previous"; title=Hello,
    @Test
    public void testParseLinkWithParameters() {
        final Map<LinkParameterName<?>, Object> parameters = Maps.of(LinkParameterName.REL, LinkRelation.parse("previous"),
                LinkParameterName.TITLE, "Hello");

        this.parseAndToTextAndCheck2("</TheBook/chapter2>;rel=previous;title=Hello",
                Link.with(Url.parse("/TheBook/chapter2")).setParameters(parameters));
    }

    // </TheBook/chapter2>;rel="previous"; title*=UTF-8'de'letztes%20Kapitel,
    @Test
    public void testParseLinkWithParameters2() {
        final Map<LinkParameterName<?>, Object> parameters = Maps.of(LinkParameterName.REL, LinkRelation.parse("previous"),
                LinkParameterName.TITLE_STAR, EncodedText.with(CharsetName.UTF_8, Optional.of(LanguageName.with("de")), "letztes Kapitel"));

        this.parseAndToTextAndCheck2("</TheBook/chapter2>;rel=previous;title*=UTF-8'de'letztes%20Kapitel",
                Link.with(Url.parse("/TheBook/chapter2")).setParameters(parameters));
    }

    private void parseAndToTextAndCheck2(final String text,
                                         final Link... links) {
        this.parseAndToTextAndCheck(text, Lists.of(links));
    }

    @Override
    LinkHeaderValueHandler handler() {
        return LinkHeaderValueHandler.INSTANCE;
    }

    @Override
    HttpHeaderName<List<Link>> name() {
        return HttpHeaderName.LINK;
    }

    @Override
    String invalidHeaderValue() {
        return "forgot surrounding lt gt";
    }

    @Override
    List<Link> value() {
        return Lists.of(
                Link.with(Url.parse("/file")),
                Link.with(Url.parse("http://example.com")));
    }

    @Override
    String valueType() {
        return this.listValueType(Link.class);
    }

    @Override
    String handlerToString() {
        return "List<Link>";
    }

    @Override
    public Class<LinkHeaderValueHandler> type() {
        return LinkHeaderValueHandler.class;
    }
}
