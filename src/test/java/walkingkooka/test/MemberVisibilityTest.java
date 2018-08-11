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

import org.junit.Test;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class MemberVisibilityTest extends EnumTestCase<MemberVisibility> {

    @Test
    public void testClassPublic() {
        check(MemberVisibility.PUBLIC, this.getClass());
    }

    @Test
    public void testClassProtected() {
        check(MemberVisibility.PROTECTED, ProtectedClass.class);
    }

    @Test
    public void testClassPackagePrivate() {
        check(MemberVisibility.PACKAGE_PRIVATE, PackagePrivateClass.class);
    }

    private void check(final MemberVisibility visibility, final Class<?> classs) {
        assertEquals(classs + "", visibility, MemberVisibility.get(classs));
    }

    protected class ProtectedClass {
    }

    class PackagePrivateClass {
    }

    @Override protected Class<MemberVisibility> type() {
        return MemberVisibility.class;
    }
}
