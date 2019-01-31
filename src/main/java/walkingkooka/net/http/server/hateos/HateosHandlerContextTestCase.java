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
import walkingkooka.ContextTestCase;
import walkingkooka.tree.Node;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HateosHandlerContextTestCase<C extends HateosHandlerContext<N>,
        N extends Node<N, ?, ?, ?>>
        extends ContextTestCase<C> {


    @Test
    public void testAddLinksNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(null, this.id(), this.node());
        });
    }

    @Test
    public void testAddLinksNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(this.name(), null, this.node());
        });
    }

    @Test
    public void testAddLinksNullNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(this.name(), this.id(), null);
        });
    }

    protected void addLinksAndCheck(final HateosResourceName name,
                                    final BigInteger id,
                                    final N node,
                                    final N expected) {
        this.addLinksAndCheck(this.createContext(),
                name,
                id,
                node,
                expected);
    }

    protected void addLinksAndCheck(final C context,
                                    final HateosResourceName name,
                                    final BigInteger id,
                                    final N node,
                                    final N expected) {
    }

    protected N addLinksAndCheck(final HateosResourceName name,
                                 final BigInteger id,
                                 final N node) {
        return this.createContext().addLinks(name,
                id,
                node);
    }

    protected N addLinksAndCheck(final C context,
                                 final HateosResourceName name,
                                 final BigInteger id,
                                 final N node) {
        return context.addLinks(name,
                id,
                node);
    }

    @Override
    protected String requiredNameSuffix() {
        return HateosHandlerContext.class.getSimpleName();
    }

    protected abstract HateosResourceName name();

    protected abstract BigInteger id();

    protected abstract N node();

}
