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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.test.ClassTesting2;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.Set;

public final class HateosHandlerBuilderRouterHateosHandlerContextTest implements ClassTesting2<HateosHandlerBuilderRouterHateosHandlerContext<JsonNode, HasJsonNode>>,
        HateosHandlerContextTesting<HateosHandlerBuilderRouterHateosHandlerContext<JsonNode, HasJsonNode>, JsonNode> {

    @Test
    public void testAddLinksSelf() {
        this.addLinksAndCheck2(this.resourceName1(),
                "{\"prop1\": \"value1\", \"prop2\": \"value2\"}");
    }

    @Test
    public void testAddLinksNoneSelf() {
        this.addLinksAndCheck2(this.resourceName2(),
                "{\"prop1\": \"value1\", \"prop2\": \"value2\"}");
    }

    @Test
    public void testAddLinksManyLinks() {
        this.addLinksAndCheck2(this.resourceName3(),
                "{\"prop1\": \"value1\", \"prop2\": \"value2\"}");
    }

    private void addLinksAndCheck2(final HateosResourceName resourceName,
                                   final String expected) {
        this.addLinksAndCheck(resourceName,
                BigInteger.valueOf(123),
                this.node(),
                JsonNode.parse(expected));
    }

    @Override
    public HateosHandlerBuilderRouterHateosHandlerContext<JsonNode, HasJsonNode> createContext() {
        return HateosHandlerBuilderRouterHateosHandlerContext.with(HttpMethod.GET,
                HateosContentType.json(),
                Url.parseAbsolute("http://example.com/api/"),
                this.nameAndLinkRelations());
    }

    private Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations() {
        final Set<HateosHandlerBuilderRouterKey> nameAndLinkRelations = Sets.sorted();

        nameAndLinkRelations.add(HateosHandlerBuilderRouterKey.with(this.resourceName1(), LinkRelation.SELF));
        nameAndLinkRelations.add(HateosHandlerBuilderRouterKey.with(this.resourceName1(), LinkRelation.ABOUT));
        nameAndLinkRelations.add(HateosHandlerBuilderRouterKey.with(this.resourceName2(), LinkRelation.SELF));
        nameAndLinkRelations.add(HateosHandlerBuilderRouterKey.with(this.resourceName2(), LinkRelation.PREV));
        nameAndLinkRelations.add(HateosHandlerBuilderRouterKey.with(this.resourceName2(), LinkRelation.NEXT));

        return nameAndLinkRelations;
    }

    private HateosResourceName resourceName1() {
        return HateosResourceName.with("entity1");
    }

    private HateosResourceName resourceName2() {
        return HateosResourceName.with("entity2");
    }

    private HateosResourceName resourceName3() {
        return HateosResourceName.with("entity3");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createContext(), "http://example.com/api/");
    }

    @Override
    public HateosResourceName name() {
        return HateosResourceName.with("entity1");
    }

    @Override
    public BigInteger id() {
        return BigInteger.valueOf(123);
    }

    @Override
    public JsonNode node() {
        return JsonNode.parse("{\"prop1\": \"value1\", \"prop2\": \"value2\"}");
    }

    @Override
    public Class<HateosHandlerBuilderRouterHateosHandlerContext<JsonNode, HasJsonNode>> type() {
        return Cast.to(HateosHandlerBuilderRouterHateosHandlerContext.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
