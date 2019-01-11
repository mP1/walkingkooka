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

import org.junit.Test;
import walkingkooka.tree.Node;

import java.math.BigInteger;

public abstract class HateosPutHandlerTestCase<H extends HateosPutHandler<N>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTestCase<H, N> {

    @Test(expected = NullPointerException.class)
    public void testNullIdFails() {
        this.put(null,
                this.resource(),
                this.createContext());
    }

    @Test(expected = NullPointerException.class)
    public void testNullResourceFails() {
        this.put(this.id(),
                null,
                this.createContext());
    }

    @Test(expected = NullPointerException.class)
    public void testNullContextFails() {
        this.put(this.id(),
                this.resource(),
                null);
    }

    protected N put(final BigInteger id,
                    final N resource,
                    final HateosHandlerContext<N> context) {
        return this.createHandler().put(id, resource, context);
    }

    abstract protected BigInteger id();

    abstract protected N resource();
}
