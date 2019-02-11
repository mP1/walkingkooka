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
import walkingkooka.ContextTesting;
import walkingkooka.tree.Node;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixing testing interface for {@link HateosHandlerContext}
 */
public interface HateosHandlerContextTesting<C extends HateosHandlerContext<N>,
        N extends Node<N, ?, ?, ?>>
        extends ContextTesting<C> {

    @Test
    default void testAddLinksNullNameFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(null, this.id(), this.node());
        });
    }

    @Test
    default void testAddLinksNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(this.name(), null, this.node());
        });
    }

    @Test
    default void testAddLinksNullNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.addLinksAndCheck(this.name(), this.id(), null);
        });
    }

    default void addLinksAndCheck(final HateosResourceName name,
                                    final BigInteger id,
                                    final N node,
                                    final N expected) {
        this.addLinksAndCheck(this.createContext(),
                name,
                id,
                node,
                expected);
    }

    default void addLinksAndCheck(final C context,
                                    final HateosResourceName name,
                                    final BigInteger id,
                                    final N node,
                                    final N expected) {
    }

    default N addLinksAndCheck(final HateosResourceName name,
                                 final BigInteger id,
                                 final N node) {
        return this.createContext().addLinks(name,
                id,
                node);
    }

    default N addLinksAndCheck(final C context,
                                 final HateosResourceName name,
                                 final BigInteger id,
                                 final N node) {
        return context.addLinks(name,
                id,
                node);
    }

    @Override
    default String typeNameSuffix() {
        return HateosHandlerContext.class.getSimpleName();
    }

    HateosResourceName name();

    BigInteger id();

    N node();
}
