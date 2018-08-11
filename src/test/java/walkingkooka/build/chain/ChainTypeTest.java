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
import walkingkooka.test.HashCodeEqualsDefinedTestCase;

import static org.junit.Assert.assertEquals;

final public class ChainTypeTest extends HashCodeEqualsDefinedTestCase<ChainType> {
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
        final ChainType type = ChainType.with(ChainTypeTest.TYPE);
        assertSame("type", ChainTypeTest.TYPE, type.type);
    }

    @Test
    public void testToString() {
        assertEquals(ChainTypeTest.TYPE, ChainType.with(ChainTypeTest.TYPE).toString());
    }

    @Override
    protected Class<ChainType> type() {
        return ChainType.class;
    }
}
