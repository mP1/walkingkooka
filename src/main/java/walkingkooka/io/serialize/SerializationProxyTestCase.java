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

package walkingkooka.io.serialize;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base class for testing a {@link SerializationProxy} with mostly parameter checking tests.
 */
abstract public class SerializationProxyTestCase<P extends SerializationProxy>
        extends ClassTestCase<P>
        implements ToStringTesting<P> {

    public SerializationProxyTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(SerializationProxy.class);
    }

    @Test
    public void testReadResolve() {
        boolean found = false;
        Class<?> type = this.type();
        for (; ; ) {
            try {
                type.getDeclaredMethod("readResolve");
                found = true;
                break;
            } catch (final Exception ignore) {
            }
            type = type.getSuperclass();
            if (type == Object.class) {
                break;
            }
        }

        final Class<?> type2 = type;
        assertTrue(found, () -> "Unable to find no args readResolve method on " + type2.getName());
    }

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
