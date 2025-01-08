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

package walkingkooka.reflect;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class JavaVisibilityTest implements ClassTesting2<JavaVisibility>,
    ToStringTesting<JavaVisibility> {

    // of(Class).......................................................................................................

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
        this.checkEquals(visibility, JavaVisibility.of(klass), klass.getName());
    }

    static protected class ProtectedClass {
        protected ProtectedClass() {
        }
    }

    static class PackagePrivateClass {
        PackagePrivateClass() {
        }
    }

    // of(Constructor)..................................................................................................

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
        this.checkEquals(visibility,
            JavaVisibility.of(constructor),
            constructor::toGenericString);
    }

    // of(Method)......................................................................................................

    @Test
    public void testMethodPublic() throws Exception {
        checkMethod("publicMethod", JavaVisibility.PUBLIC);
    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    void packagePrivateMethod() {
    }

    @Test
    public void testMethodPrivate() throws Exception {
        checkMethod(("privateMethod"), JavaVisibility.PRIVATE);
    }

    @SuppressWarnings("unused")
    private void privateMethod() {
    }

    private void checkMethod(final String methodName,
                             final JavaVisibility visibility) throws Exception {
        final Method method = JavaVisibilityTest.class.getDeclaredMethod(methodName);
        this.checkEquals(visibility,
            JavaVisibility.of(method),
            method::toGenericString);
    }

    // of(Field)......................................................................................................

    @Test
    public void testFieldPublic() throws Exception {
        checkField("publicField", JavaVisibility.PUBLIC);
    }

    @SuppressWarnings("unused")
    public Object publicField;

    @Test
    public void testFieldProtected() throws Exception {
        checkField("protectedField", JavaVisibility.PROTECTED);
    }

    @SuppressWarnings("unused")
    protected Object protectedField;

    @Test
    public void testFieldPackagePrivate() throws Exception {
        checkField("packagePrivateField", JavaVisibility.PACKAGE_PRIVATE);
    }

    @SuppressWarnings("unused")
    Object packagePrivateField;

    @Test
    public void testFieldPrivate() throws Exception {
        checkField("privateField", JavaVisibility.PRIVATE);
    }

    @SuppressWarnings("unused")
    private Object privateField;

    private void checkField(final String field,
                            final JavaVisibility visibility) throws Exception {
        this.checkEquals(visibility,
            JavaVisibility.of(JavaVisibilityTest.class.getDeclaredField(field)),
            field);
    }

    // isOrLess.........................................................................................................

    @Test
    public void testIsOrLessPublicPublic() {
        this.isOrLessCheck(JavaVisibility.PUBLIC, JavaVisibility.PUBLIC, true);
    }

    @Test
    public void testIsOrLessPublicProtected() {
        this.isOrLessCheck(JavaVisibility.PUBLIC, JavaVisibility.PROTECTED, false);
    }

    @Test
    public void testIsOrLessPublicPackagePrivate() {
        this.isOrLessCheck(JavaVisibility.PUBLIC, JavaVisibility.PACKAGE_PRIVATE, false);
    }

    @Test
    public void testIsOrLessPublicPrivate() {
        this.isOrLessCheck(JavaVisibility.PUBLIC, JavaVisibility.PRIVATE, false);
    }

    @Test
    public void testIsOrLessProtectedPublic() {
        this.isOrLessCheck(JavaVisibility.PROTECTED, JavaVisibility.PUBLIC, true);
    }

    @Test
    public void testIsOrLessProtectedProtected() {
        this.isOrLessCheck(JavaVisibility.PROTECTED, JavaVisibility.PROTECTED, true);
    }

    @Test
    public void testIsOrLessProtectedPackagePrivate() {
        this.isOrLessCheck(JavaVisibility.PROTECTED, JavaVisibility.PACKAGE_PRIVATE, false);
    }

    @Test
    public void testIsOrLessProtectedPrivate() {
        this.isOrLessCheck(JavaVisibility.PROTECTED, JavaVisibility.PRIVATE, false);
    }

    @Test
    public void testIsOrLessPackagePrivatePublic() {
        this.isOrLessCheck(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.PUBLIC, true);
    }

    @Test
    public void testIsOrLessPackagePrivateProtected() {
        this.isOrLessCheck(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.PROTECTED, true);
    }

    @Test
    public void testIsOrLessPackagePrivatePackagePrivate() {
        this.isOrLessCheck(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.PACKAGE_PRIVATE, true);
    }

    @Test
    public void testIsOrLessPackagePrivatePrivate() {
        this.isOrLessCheck(JavaVisibility.PACKAGE_PRIVATE, JavaVisibility.PRIVATE, false);
    }

    @Test
    public void testIsOrLessPrivatePublic() {
        this.isOrLessCheck(JavaVisibility.PRIVATE, JavaVisibility.PUBLIC, true);
    }

    @Test
    public void testIsOrLessPrivateProtected() {
        this.isOrLessCheck(JavaVisibility.PRIVATE, JavaVisibility.PROTECTED, true);
    }

    @Test
    public void testIsOrLessPrivatePackagePrivate() {
        this.isOrLessCheck(JavaVisibility.PRIVATE, JavaVisibility.PACKAGE_PRIVATE, true);
    }

    @Test
    public void testIsOrLessPrivatePrivate() {
        this.isOrLessCheck(JavaVisibility.PRIVATE, JavaVisibility.PRIVATE, true);
    }

    private void isOrLessCheck(final JavaVisibility visibility,
                               final JavaVisibility other,
                               final boolean expected) {
        this.checkEquals(expected,
            visibility.isOrLess(other),
            () -> visibility + " is less than " + other);
    }

    // javaKeyword......................................................................................................

    @Test
    public void testJavaKeywordPublic() {
        this.javaKeywordCheck(
            JavaVisibility.PUBLIC,
            "public"
        );
    }

    @Test
    public void testJavaKeywordProtected() {
        this.javaKeywordCheck(
            JavaVisibility.PROTECTED,
            "protected"
        );
    }

    @Test
    public void testJavaKeywordPackagePrivate() {
        this.javaKeywordCheck(
            JavaVisibility.PACKAGE_PRIVATE,
            ""
        );
    }

    @Test
    public void testJavaKeywordPrivate() {
        this.javaKeywordCheck(
            JavaVisibility.PRIVATE,
            "private"
        );
    }

    private void javaKeywordCheck(final JavaVisibility visibility,
                                  final String expected) {
        this.checkEquals(
            expected,
            visibility.javaKeyword(),
            () -> visibility.toString()
        );
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
