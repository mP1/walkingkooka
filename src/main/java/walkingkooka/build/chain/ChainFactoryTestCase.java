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

package walkingkooka.build.chain;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for any {@link ChainFactory} that includes mostly parameter check tests
 */
abstract public class ChainFactoryTestCase<F extends ChainFactory<T>, T>
        extends ClassTestCase<F>
        implements TypeNameTesting<F> {

    protected ChainFactoryTestCase() {
        super();
    }

    @Test
    public void testCreateWithNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createFactory().create(null);
        });
    }

    abstract protected F createFactory();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return ChainFactory.class.getSimpleName();
    }
}
