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
import walkingkooka.InvalidCharacterException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PackageNameTest extends PackageNameOrTypeNameJavaNameTestCase<PackageName> {

    @Override
    public void testEmptyFails() {
    }

    @Test
    public void testFromPackageNullFails() {
        assertThrows(NullPointerException.class, () -> PackageName.from(null));
    }

    @Test
    public void testFromPackage() {
        final PackageName p = PackageName.from(this.getClass().getPackage());
        this.checkValue(p, "walkingkooka.reflect");
    }

    @Test
    public void testWithEmptyPackageFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before..after"));
    }

    @Test
    public void testWithTrailingDotFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg1.pkg2."));
    }

    @Test
    public void testWithEmptyPackageFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.."));
    }

    @Test
    public void testWithEmptyPackageFails3() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("..after"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.1abc.after"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("1abc.after"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails3() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.1"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails4() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.1"));
    }

    @Test
    public void testWithInvalidPartFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.a\nb.after"));
    }

    @Test
    public void testWithInvalidPartFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before.a\n"));
    }

    @Test
    public void testWithEmpty() {
        assertSame(PackageName.UNNAMED, PackageName.with(""));
    }

    @Test
    public void testWithJavaLang() {
        this.createNameAndCheck("java.lang");
    }

    @Test
    public void testWithJavaLangReflect() {
        this.createNameAndCheck("java.lang.reflect");
    }

    @Test
    public void testWithJavaUtil() {
        this.createNameAndCheck("java.util");
    }

    @Test
    public void testAllBeginInvalidCharsFails() {
        for (char c : this.possibleInvalidChars(0).toCharArray()) {
            assertThrows(InvalidCharacterException.class, () -> this.createName("before." + c));
        }
    }

    @Test
    public void testAllNonInitialInvalidCharsFails() {
        for (char c : this.possibleInvalidChars(3).toCharArray()) {
            assertThrows(InvalidCharacterException.class, () -> this.createName("before." + c));
        }
    }

    @Test
    public void testAllBeginValidChars() {
        for (char c : this.possibleValidChars(0).toCharArray()) {
            if (c == '.') {
                continue;
            }
            this.createName("before." + c);
        }
    }

    @Test
    public void testAllNonInitialValidChars() {
        for (char c : this.possibleValidChars(3).toCharArray()) {
            if (c == '.') {
                continue;
            }
            this.createName("before.pkg" + c);
        }
    }

    @Test
    public void testAppendNullFails() {
        assertThrows(NullPointerException.class, () -> this.createName("before.pkg").append(null));
    }

    @Test
    public void testAppend() {
        final PackageName p = PackageName.with("a1.b2");
        final PackageName p2 = PackageName.with("c3");
        final PackageName p3 = p.append(p2);
        this.checkValue(p3, "a1.b2.c3");

        this.checkEquals(p, p3.parent(), "parent");
    }

    @Override
    public PackageName createName(final String name) {
        return PackageName.with(name);
    }

    @Override
    public String nameText() {
        return "walkingkooka.reflect";
    }

    @Override
    public String differentNameText() {
        return "walkingkooka";
    }

    @Override
    public String nameTextLess() {
        return "before.before2.before3";
    }

    // filename.........................................................................................................

    @Test
    public void testFilenameWithJavaLangObject() {
        this.filenameAndCheck(
            "java.lang.Object",
            "java/lang"
        );
    }

    @Test
    public void testFilenameWithJavaUtilMapEntry() {
        this.filenameAndCheck(
            Map.Entry.class.getName(),
            "java/util"
        );
    }

    private void filenameAndCheck(final String name,
                                  final String expected) {
        this.filenameAndCheck(
            ClassName.with(name).parentPackage(),
            expected
        );
    }

    class Xyz {

    }

    private void filenameAndCheck(final PackageName name,
                                  final String expected) {
        this.checkEquals(
            expected,
            name.filename(),
            () -> name + " filename"
        );
    }

    // Class............................................................................................................

    @Override
    public Class<PackageName> type() {
        return PackageName.class;
    }
}
