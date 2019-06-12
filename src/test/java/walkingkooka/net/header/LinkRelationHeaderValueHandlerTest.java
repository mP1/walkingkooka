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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;

import java.util.List;

public final class LinkRelationHeaderValueHandlerTest extends
        NonStringHeaderValueHandlerTestCase<LinkRelationHeaderValueHandler, List<LinkRelation<?>>> {

    @Override
    public String typeNamePrefix() {
        return LinkRelation.class.getSimpleName();
    }

    // http://www.rfc-editor.org/rfc/rfc5988.txt

    // </TheBook/chapter2>;
    @Test
    public void testParseLinkRegular() {
        this.parseAndToTextAndCheck2("abc123",
                LinkRelation.with("abc123"));
    }

    @Test
    public void testParseLinkUrl() {
        this.parseAndToTextAndCheck2("http://example.com",
                LinkRelation.with("http://example.com"));
    }

    @Test
    public void testParseLinkMultiple() {
        this.parseAndToTextAndCheck2("abc123 http://example.com",
                LinkRelation.with("abc123"),
                LinkRelation.with("http://example.com"));
    }

    private void parseAndToTextAndCheck2(final String text,
                                         final LinkRelation<?>... relations) {
        this.parseAndToTextAndCheck(text, Lists.of(relations));
    }

    @Override
    LinkRelationHeaderValueHandler handler() {
        return LinkRelationHeaderValueHandler.INSTANCE;
    }

    @Override
    LinkParameterName<List<LinkRelation<?>>> name() {
        return LinkParameterName.REL;
    }

    @Override
    String invalidHeaderValue() {
        return "\r";
    }

    @Override
    List<LinkRelation<?>> value() {
        return Lists.of(LinkRelation.with("abc123"), LinkRelation.with("http://example.com"));
    }

    @Override
    String valueType() {
        return this.listValueType(LinkRelation.class);
    }

    @Override
    String handlerToString() {
        return "List<LinkRelation>";
    }

    @Override
    public Class<LinkRelationHeaderValueHandler> type() {
        return LinkRelationHeaderValueHandler.class;
    }
}
