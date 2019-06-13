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
 */

package walkingkooka.collect.iterator;

import walkingkooka.Cast;
import walkingkooka.build.chain.ChainFactoryTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Iterator;

final public class IteratorChainFactoryTest implements ClassTesting2<IteratorChainFactory<Object>>,
        ChainFactoryTesting<IteratorChainFactory<Object>, Iterator<Object>> {

    @Override
    public IteratorChainFactory<Object> createFactory() {
        return IteratorChainFactory.instance();
    }

    @Override
    public Class<IteratorChainFactory<Object>> type() {
        return Cast.to(IteratorChainFactory.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
