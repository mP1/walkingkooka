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

package walkingkooka.test;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Adds some additional tests to verify the visibility of the class and constructors.
 */
abstract public class PublicClassTestCase<T> extends ClassTestCase<T> {

    protected PublicClassTestCase() {
        super();
    }

    @Test final public void testClassIsPublic() {
        final Class<?> type = this.type();
        Assert.assertTrue(type.getName() + " is not public=" + type,
                Modifier.isPublic(type.getModifiers()));
    }

    @Test final public void testClassIsFinalIfAllConstructorsArePrivate() {
        this.classIsFinalIfAllConstructorsArePrivateTest();
    }

    /**
     * Asserts that a field is public static and final.
     */
    protected void checkFieldIsPublicStaticFinal(final Class<?> enclosingType, final String name,
                                                 final Class<?> fieldType) {
        Field field = null;
        try {
            field = enclosingType.getDeclaredField(name);
        } catch (final Exception cause) {
            Assert.fail("Cannot find public constant field of type " + enclosingType + " called "
                    + name);
        }
        Assert.assertEquals("The field " + name + " is wrong the type", fieldType, field.getType());
        Assert.assertTrue("The field " + name + " must be static =" + field, FieldAttributes.STATIC.is(field));
        Assert.assertSame("The field " + name + " must be public =" + field, MemberVisibility.PUBLIC, MemberVisibility.get(field));
        Assert.assertTrue("The field " + name + " must be final=" + field, FieldAttributes.FINAL.is(field));
    }

    /**
     * Constructor is private if this class is final, otherwise they are package private.
     */
    @Test
    public void testAllConstructorsVisibility() throws Throwable {
        this.checkAllConstructorsVisibility();
    }
}
