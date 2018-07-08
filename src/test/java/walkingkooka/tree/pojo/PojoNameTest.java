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
package walkingkooka.tree.pojo;

import org.junit.Test;
import walkingkooka.naming.NameTestCase;

public final class PojoNameTest extends NameTestCase<PojoName> {

    private final static String PROPERTY = "abc";

    @Test(expected = NullPointerException.class)
    public void testPropertyNullFails() {
        PojoName.property(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyEmptyFails() {
        PojoName.property("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyInvalidInitialFails() {
        PojoName.property("9abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPropertyInvalidOtherFails() {
        PojoName.property("abc.");
    }

    @Test
    public void testProperty() {
        final PojoName name = PojoName.property(PROPERTY);
        assertEquals(PROPERTY, name.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIndexInvalidFails() {
        PojoName.index(-1);
    }

    @Test
    public void testIndex() {
        final PojoName name = PojoName.index(123);
        assertEquals("123", name.value());
    }

    @Override
    protected PojoName createName(final String name) {
        return PojoName.property(name);
    }

    @Override
    protected Class<PojoName> type() {
        return PojoName.class;
    }
}
