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

package walkingkooka.type;

import org.junit.jupiter.api.Test;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class JavaVisibilityTest implements ClassTesting2<JavaVisibility>,
        ToStringTesting<JavaVisibility> {

    @Test
    public void testClassPublic() {
        check(JavaVisibility.PUBLIC, this.getClass());
    }

    @Test
    public void testClassProtected() {
        check(JavaVisibility.PROTECTED, ProtectedClass.class);
    }

    @Test
    public void testClassPackagePrivate() {
        check(JavaVisibility.PACKAGE_PRIVATE, PackagePrivateClass.class);
    }

    private void check(final JavaVisibility visibility, final Class<?> klass) {
        assertEquals(visibility, JavaVisibility.get(klass), klass.getName());
    }

    protected class ProtectedClass {
    }

    class PackagePrivateClass {
    }

    @Override
    public Class<JavaVisibility> type() {
        return JavaVisibility.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}