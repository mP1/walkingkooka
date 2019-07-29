/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.type;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JavaVisibilityTest implements ClassTesting2<JavaVisibility>,
        ToStringTesting<JavaVisibility> {

    // get(Class).......................................................................................................

    @Test
    public void testClassPublic() {
        checkClass(this.getClass(), JavaVisibility.PUBLIC);
    }

    @Test
    public void testClassProtected() {
        checkClass(ProtectedClass.class, JavaVisibility.PROTECTED);
    }

    @Test
    public void testClassPackagePrivate() {
        checkClass(PackagePrivateClass.class, JavaVisibility.PACKAGE_PRIVATE);
    }

    private void checkClass(final Class<?> klass,
                            final JavaVisibility visibility) {
        assertEquals(visibility, JavaVisibility.get(klass), klass.getName());
    }

    static protected class ProtectedClass {
        protected ProtectedClass() {
        }
    }

    static class PackagePrivateClass {
        PackagePrivateClass() {
        }
    }

    // get(Constructor)..................................................................................................

    @Test
    public void testConstructorPublic() throws Exception {
        checkConstructor(JavaVisibilityTest.class, JavaVisibility.PUBLIC);
    }

    @Test
    public void testConstructorProtected() throws Exception {
        checkConstructor(ProtectedClass.class, JavaVisibility.PROTECTED);
    }

    @Test
    public void testConstructorPackagePrivate() throws Exception {
        checkConstructor(PackagePrivateClass.class, JavaVisibility.PACKAGE_PRIVATE);
    }

    private void checkConstructor(final Class<?> classs,
                                  final JavaVisibility visibility) throws Exception {
        final Constructor<?> constructor = classs.getDeclaredConstructor();
        assertEquals(visibility,
                JavaVisibility.get(constructor),
                () -> constructor.toGenericString());
    }

    // get(Method)......................................................................................................

    @Test
    public void testMethodPublic() throws Exception {
        checkMethod("publicMethod", JavaVisibility.PUBLIC);
    }

    public void publicMethod() {
    }

    @Test
    public void testMethodProtected() throws Exception {
        checkMethod("protectedMethod", JavaVisibility.PROTECTED);
    }

    protected void protectedMethod() {
    }

    @Test
    public void testMethodPackagePrivate() throws Exception {
        checkMethod("packagePrivateMethod", JavaVisibility.PACKAGE_PRIVATE);
    }

    void packagePrivateMethod() {
    }

    @Test
    public void testMethodPrivate() throws Exception {
        checkMethod(("privateMethod"), JavaVisibility.PRIVATE);
    }

    private void privateMethod() {
    }

    private void checkMethod(final String methodName,
                             final JavaVisibility visibility) throws Exception {
        final Method method = JavaVisibilityTest.class.getDeclaredMethod(methodName);
        assertEquals(visibility,
                JavaVisibility.get(method),
                () -> method.toGenericString());
    }

    // get(Field)......................................................................................................

    @Test
    public void testFieldPublic() throws Exception {
        checkField("publicField", JavaVisibility.PUBLIC);
    }

    public Object publicField;

    @Test
    public void testFieldProtected() throws Exception {
        checkField("protectedField", JavaVisibility.PROTECTED);
    }

    protected Object protectedField;

    @Test
    public void testFieldPackagePrivate() throws Exception {
        checkField("packagePrivateField", JavaVisibility.PACKAGE_PRIVATE);
    }

    Object packagePrivateField;

    @Test
    public void testFieldPrivate() throws Exception {
        checkField("privateField", JavaVisibility.PRIVATE);
    }

    private Object privateField;

    private void checkField(final String field,
                            final JavaVisibility visibility) throws Exception {
        assertEquals(visibility,
                JavaVisibility.get(JavaVisibilityTest.class.getDeclaredField(field)),
                field);
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<JavaVisibility> type() {
        return JavaVisibility.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
