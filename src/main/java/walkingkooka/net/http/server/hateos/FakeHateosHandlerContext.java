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

import walkingkooka.test.Fake;
import walkingkooka.tree.Node;

import java.math.BigInteger;

public class FakeHateosHandlerContext<N extends Node<N, ?, ?, ?>> implements HateosHandlerContext<N>, Fake {

    @Override
    public N addLinks(final HateosResourceName name,
                      final BigInteger id,
                      final N node) {
        throw new UnsupportedOperationException();
    }
}
