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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ChainTypeTest extends ClassTestCase<ChainType>
        implements HashCodeEqualsDefinedTesting<ChainType>,
        SerializationTesting<ChainType> {
    // constants

    private final static String TYPE = "ALL";

    // tests

    @Test
    public void testWithNullFails() {
        this.withFails(null);
    }

    @Test
    public void testWithEmptyFails() {
        this.withFails("");
    }

    @Test
    public void testWithWhitespaceFails() {
        this.withFails("   ");
    }

    private void withFails(final String type) {
        try {
            ChainType.with(type);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test
    public void testWith() {
        final ChainType type = ChainType.with(TYPE);
        assertSame("type", TYPE, type.type);
    }

    @Test
    public void testToString() {
        assertEquals(TYPE, ChainType.with(TYPE).toString());
    }

    @Test
    public void testConstantsAreSingletons() throws Exception {
        this.constantsAreSingletons();
    }
    
    // tests

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(ChainType.with("different"));
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkNotEquals(ChainType.with("TYpe"));
    }

    @Override
    public ChainType createObject() {
        return ChainType.with(TYPE);
    }

    @Override
    public Class<ChainType> type() {
        return ChainType.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public ChainType serializableInstance() {
        return ChainType.with("all");
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return false;
    }
}
