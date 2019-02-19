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

import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.Node;

public interface HateosHandlerTesting<H extends HateosHandler<I, N>, I extends Comparable<I>, N extends Node<N, ?, ?, ?>> extends TypeNameTesting<H> {

    H createHandler();

    HateosHandlerContext<N> createContext();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "Hateos";
    }
}
