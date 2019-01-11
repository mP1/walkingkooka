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
import walkingkooka.compare.Range;
import walkingkooka.tree.Node;

import java.math.BigInteger;

public abstract class HateosDeleteHandlerTestCase<H extends HateosDeleteHandler<N>, N extends Node<N, ?, ?, ?>> extends HateosHandlerTestCase<H, N> {

    @Test(expected = NullPointerException.class)
    public void testDeleteNullIdFails() {
        this.delete(null,
                this.createContext());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteNullContextFails() {
        this.delete(this.id(),
                null);
    }

    protected void delete(final BigInteger id,
                          final HateosHandlerContext<N> context) {
        this.createHandler().delete(id, context);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteCollectionNullIdRangeFails() {
        this.deleteCollection(null,
                this.createContext());
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteCollectionNullContextFails() {
        this.deleteCollection(this.collection(),
                null);
    }

    protected void deleteCollection(final Range<BigInteger> collection,
                                    final HateosHandlerContext<N> context) {
        this.createHandler().deleteCollection(collection, context);
    }

    abstract protected BigInteger id();

    abstract protected Range<BigInteger> collection();

    abstract protected N resource();
}
