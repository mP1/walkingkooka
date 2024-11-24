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
import walkingkooka.predicate.character.CharPredicate;

import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ClassNameTest extends PackageNameOrTypeNameJavaNameTestCase<ClassName> {

    @Test
    public void testCharacterIsIdentifierStartCount() {
        int i = 0;
        for(int j = 0; j <= Character.MAX_VALUE; j++) {
            if(Character.isJavaIdentifierStart((char)j)) {
                i++;
            }
        }

        this.checkEquals(
                48846,
                i
        );
    }

    @Test
    public void testCharacterIsIdentifierPartCount() {
        int i = 0;
        for(int j = 0; j <= Character.MAX_VALUE; j++) {
            if(Character.isJavaIdentifierPart((char)j)) {
                i++;
            }
        }

        this.checkEquals(
                50559,
                i
        );
    }

    @Test
    public void testCharacterIsIdentifierStart() {
        base64(Character::isJavaIdentifierStart);
    }

    @Test
    public void testCharacterIsIdentifierPart() {
        base64(Character::isJavaIdentifierPart);
    }

    private void base64(final CharPredicate startOrPart) {
        int i = 0;
        int j = 0;
        final StringBuilder b = new StringBuilder();


        for (int k = 0; k <= Character.MAX_VALUE; k++) {
            i = i * 2 + (startOrPart.test((char) k) ? 1 : 0);
            j++;

            if (6 == j) {
                b.append(
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(i)
                );
                j = 0;
                i = 0;
            }
        }
        System.out.println(b);
    }

    @Test
    public void testFromClassNullFails() {
        assertThrows(NullPointerException.class, () -> ClassName.fromClass(null));
    }

    @Test
    public void testFromClass() {
        final ClassName p = ClassName.fromClass(this.getClass());
        this.checkValue(p, "walkingkooka.reflect.ClassNameTest");
    }

    @Test
    public void testWithTrailingDotFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg1.ClassName."));
    }

    @Test
    public void testWithEmptyPackageFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("before..after"));
    }

    @Test
    public void testWithEmptyPackageFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.."));
    }

    @Test
    public void testWithEmptyPackageFails3() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("..after"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.1ClassName"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("1abc.ClassName"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails3() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.1"));
    }

    @Test
    public void testWithPartInsteadOfInitialFails4() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.1"));
    }

    @Test
    public void testWithInvalidPartFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.a\nb.ClassName"));
    }

    @Test
    public void testWithInvalidPartFails2() {
        assertThrows(InvalidCharacterException.class, () -> this.createName("pkg123.A\n"));
    }

    @Test
    public void testWithArrayClassNameFails() {
        assertThrows(InvalidCharacterException.class, () -> this.createName(Object[].class.getName()));
    }

    @Test
    public void testWithLambdaClassNameFails() {
        final Predicate<?> lambda = (t) -> true;
        assertThrows(InvalidCharacterException.class, () -> this.createName(lambda.getClass().getName()));
    }

    @Test
    public void testWithClassWithoutPackage() {
        this.createNameAndCheck("A");
    }


    @Test
    public void testWithJavaLangString() {
        this.createNameAndCheck("java.lang.String");
    }

    @Test
    public void testWithJavaLangReflectMethod() {
        this.createNameAndCheck("java.lang.reflect.Method");
    }

    @Test
    public void testWithJavaUtilHashMap() {
        this.createNameAndCheck("java.util.HashMap");
    }

    @Test
    public void testNameWithoutPackage() {
        this.withoutPackageNameAndCheck(String.class);
    }

    @Test
    public void testNameWithoutPackage2() {
        final Predicate<?> lambda = (t) -> true;
        this.withoutPackageNameAndCheck(lambda.getClass());
    }

    private void withoutPackageNameAndCheck(final Class<?> type) {
        this.withoutPackageNameAndCheck(ClassName.fromClass(type), type.getSimpleName());
    }

    private void withoutPackageNameAndCheck(final ClassName typeName, final String without) {
        this.checkEquals(without, typeName.nameWithoutPackage(), "nameWithoutPackage");
    }

    @Test
    public void testPackage() {
        this.packageAndCheck(this.getClass());
    }

    private void packageAndCheck(final Class<?> type) {
        this.packageAndCheck(type.getName(), type.getPackage().getName());

        this.checkEquals(PackageName.from(type.getPackage()), ClassName.with(type.getName()).parentPackage(), "parentPackage");
    }

    private void packageAndCheck(final String name, final String pkg) {
        final ClassName typeName = ClassName.with(name);
        this.checkEquals(PackageName.with(pkg), typeName.parentPackage(), "parentPackage");

    }

    // filename.........................................................................................................

    @Test
    public void testFilenameWithJavaLangObject() {
        this.filenameAndCheck(
                "java.lang.Object",
                "java/lang/Object.class"
        );
    }

    @Test
    public void testFilenameWithJavaUtilMapEntry() {
        this.filenameAndCheck(
                Map.Entry.class.getName(),
                "java/util/Map$Entry.class"
        );
    }

    @Test
    public void testFilenameWithUnnamedPackageClass() {
        this.filenameAndCheck(
                "UnnamedPackageClass",
                "UnnamedPackageClass.class"
        );
    }

    private void filenameAndCheck(final String name,
                                  final String expected) {
        this.filenameAndCheck(
                ClassName.with(name),
                expected
        );
    }

    private void filenameAndCheck(final ClassName name,
                                  final String expected) {
        this.checkEquals(
                expected,
                name.filename(),
                () -> name + " filename"
        );
    }

    // name.............................................................................................................

    @Override
    public ClassName createName(final String name) {
        return ClassName.with(name);
    }

    @Override
    public String nameText() {
        return this.getClass().getName();
    }

    @Override
    public String differentNameText() {
        return "walkingkooka";
    }

    @Override
    public String nameTextLess() {
        return "before.before2.ClassName123";
    }

    @Override
    public Class<ClassName> type() {
        return ClassName.class;
    }
}
