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

package walkingkooka.naming;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

/**
 * Base class for testing a {@link Name} with mostly helpers to assert construction failure.
 */
abstract public class NameTestCase<N extends Name> extends ClassTestCase<N> {

    protected NameTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(Name.class);
    }

    @Test(expected = NullPointerException.class)
    public void testNullFails() {
        this.createName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFails() {
        this.createName("");
    }

    @Test
    public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected N createName(String name);

    protected void createNameAndCheck(final String value) {
        final N name = this.createName(value);
        this.checkValue(name, value);
    }

    protected void checkValue(final Name name, final String value) {
        assertEquals("value", value, name.value());
    }

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
