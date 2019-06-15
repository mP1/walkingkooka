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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.Node;
import walkingkooka.type.JavaVisibility;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class HateosContentTypeTestCase<C extends HateosContentType<N>, N extends Node<N, ?, ?, ?>> implements ClassTesting2<C>,
        TypeNameTesting<C> {

    HateosContentTypeTestCase() {
        super();
    }

    final void fromNodeAndCheck(final String text,
                                final Class<TestHateosResource> resourceType,
                                final TestHateosResource resource) {
        assertEquals(resource,
                this.contentType()
                        .fromNode(text, this.documentBuilder(), resourceType),
                () -> "fromNode failed: " + text);
    }

    final void fromNodeListAndCheck(final String text,
                                    final Class<TestHateosResource> resourceType,
                                    final TestHateosResource... resources) {
        assertEquals(Lists.of(resources),
                this.contentType()
                        .fromNodeList(text, this.documentBuilder(), resourceType),
                () -> "fromNodeList failed: " + text);
    }

    final <R extends HateosResource<?>> void toTextAndCheck(final R resource,
                                                            final Collection<LinkRelation<?>> linkRelations,
                                                            final String text) {
        final AbsoluteUrl base = AbsoluteUrl.parseAbsolute("http://example.com/api");
        final HateosResourceName resourceName = HateosResourceName.with("test");

        assertEquals(text,
                this.contentType()
                        .toText(resource,
                                this.documentBuilder(),
                                HttpMethod.PUT,
                                base,
                                resourceName,
                                linkRelations),
                () -> "toText failed: " + resource + " PUT " + base + " " + resourceName + " " + linkRelations);
    }

    final <R extends HateosResource<?>> void toTextListAndCheck(final List<R> resources,
                                                                final Collection<LinkRelation<?>> linkRelations,
                                                                final String text) {
        final AbsoluteUrl base = AbsoluteUrl.parseAbsolute("http://example.com/api");
        final HateosResourceName resourceName = HateosResourceName.with("test");

        assertEquals(text,
                this.contentType()
                        .toTextList(resources,
                                this.documentBuilder(),
                                HttpMethod.PUT,
                                base,
                                resourceName,
                                linkRelations),
                () -> "toTextList failed: " + resources + " PUT " + base + " " + resourceName + " " + linkRelations);
    }

    abstract C contentType();

    final DocumentBuilder documentBuilder() {
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

    // ClassTesting.....................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return HateosContentType.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
