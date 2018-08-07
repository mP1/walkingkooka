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

package walkingkooka.net;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class Ip4AddressEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<Ip4Address> {

    @Test
    public void testDifferent() {
        this.checkNotEquals(Ip4Address.with(new byte[]{5, 6, 7, 8}));
    }

    @Test
    public void testIp6() {
        this.checkNotEquals(Ip6Address.with(new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
    }

    @Override
    protected Ip4Address createObject() {
        return Ip4Address.with(new byte[]{1, 2, 3, 4});
    }
}
