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

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class HateosPostHandlerTestCase<H extends HateosPostHandler<N>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTestCase<H, N> {

    @Test
    public void testNullIdFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(null,
                    this.resource(),
                    this.createContext());
        });
    }

    @Test
    public void testNullResourceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(this.id(),
                    null,
                    this.createContext());
        });
    }

    @Test
    public void testNullContextFails() {
        assertThrows(NullPointerException.class, () -> {
            this.post(this.id(),
                    this.resource(),
                    null);
        });
    }

    protected N post(final Optional<BigInteger> id,
                     final N resource,
                     final HateosHandlerContext<N> context) {
        return this.createHandler().post(id, resource, context);
    }

    abstract protected Optional<BigInteger> id();

    abstract protected N resource();

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNameSuffix() {
        return "PostHandler";
    }
}
