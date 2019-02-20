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
import walkingkooka.tree.Node;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link HateosPostHandler}
 */
public interface HateosPostHandlerTesting<H extends HateosPostHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTesting<H, I, N> {

    @Test
    default void testPostNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(null,
                    this.resource(),
                    this.createContext());
        });
    }

    @Test
    default void testPostNullResourceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(this.id(),
                    null,
                    this.createContext());
        });
    }

    @Test
    default void testPostNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(this.id(),
                    this.resource(),
                    null);
        });
    }

    default N post(final Optional<I> id,
                   final N resource,
                   final HateosHandlerContext<N> context) {
        return this.createHandler().post(id, resource, context);
    }

    Optional<I> id();

    N resource();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNameSuffix() {
        return "PostHandler";
    }
}
