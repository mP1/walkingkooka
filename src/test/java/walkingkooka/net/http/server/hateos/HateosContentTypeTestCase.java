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
import walkingkooka.net.http.HttpMethod;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.Node;
import walkingkooka.type.MemberVisibility;

import java.math.BigInteger;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class HateosContentTypeTestCase<C extends HateosContentType<N>, N extends Node<N, ?, ?, ?>> implements ClassTesting2<C>,
        TypeNameTesting<C> {

    HateosContentTypeTestCase() {
        super();
    }

    final void addLinksAndCheck(final BigInteger id,
                                final String node,
                                final HttpMethod method,
                                final AbsoluteUrl base,
                                final HateosResourceName resourceName,
                                final Set<LinkRelation<?>> linkRelations,
                                final String expected) throws Exception {
        assertEquals(this.parse(expected),
                this.addLinks(id, this.parse(node), method, base, resourceName, linkRelations),
                "add links " + id + " " + CharSequences.quoteAndEscape(node) + " " + method + " " + base + " " + linkRelations);
    }

    final N addLinks(final BigInteger id,
                     final N node,
                     final HttpMethod method,
                     final AbsoluteUrl base,
                     final HateosResourceName resourceName,
                     final Set<LinkRelation<?>> linkRelations) {
        return this.constant().addLinks(id, node, method, base, resourceName, linkRelations);
    }

    abstract N parse(final String text) throws Exception;

    abstract C constant();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
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
