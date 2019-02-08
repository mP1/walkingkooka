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
import walkingkooka.compare.Range;
import walkingkooka.net.http.server.HttpRequestParameterName;
import walkingkooka.tree.Node;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HateosGetHandlerTestCase<H extends HateosGetHandler<N>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTestCase<H, N> {

    @Test
    public void testGetNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(null,
                    this.parameters(),
                    this.createContext());
        });
    }

    @Test
    public void testGetNullParametersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(this.id(),
                    null,
                    this.createContext());
        });
    }

    @Test
    public void testGetNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.get(this.id(),
                    this.parameters(),
                    null);
        });
    }

    protected Optional<N> get(final BigInteger id,
                              final Map<HttpRequestParameterName, List<String>> parameters,
                              final HateosHandlerContext<N> context) {
        return this.createHandler().get(id, parameters, context);
    }

    @Test
    public void testGetCollectionNullCollectionFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(null,
                    this.parameters(),
                    this.createContext());
        });
    }

    @Test
    public void testGetCollectionNullParametCollectionersFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(this.collection(),
                    null,
                    this.createContext());
        });
    }

    @Test
    public void testGetCollectionNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.getCollection(this.collection(),
                    this.parameters(),
                    null);
        });
    }

    protected Optional<N> getCollection(final Range<BigInteger> collection,
                                        final Map<HttpRequestParameterName, List<String>> parameters,
                                        final HateosHandlerContext<N> context) {
        return this.createHandler().getCollection(collection, parameters, context);
    }

    abstract protected BigInteger id();

    abstract protected Range<BigInteger> collection();

    abstract protected Map<HttpRequestParameterName, List<String>> parameters();

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNameSuffix() {
        return "GetHandler";
    }
}
