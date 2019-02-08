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

import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class PojoPropertyTestCase<P extends PojoProperty> extends ClassTestCase<P>
        implements ToStringTesting<P>,
        TypeNameTesting<P> {

    final protected void getAndCheck(final Object instance, final Object value){
        this.getAndCheck(this.createPojoProperty(), instance, value);
    }

    final protected void getAndCheck(final P property, final Object instance, final Object value){
        assertEquals(value, property.get(instance), ()-> "wrong value returned when calling " + property + " get");
    }

    final protected Object setAndCheck(final Object instance, final Object value){
        return this.setAndCheck(this.createPojoProperty(), instance, value);
    }

    final protected Object setAndCheck(final P property, final Object instance, final Object value){
        return property.set(instance, value);
    }

    abstract protected P createPojoProperty();

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting .........................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return PojoProperty.class.getSimpleName();
    }
}
