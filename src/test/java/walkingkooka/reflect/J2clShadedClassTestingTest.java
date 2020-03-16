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
import walkingkooka.collect.map.Maps;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class J2clShadedClassTestingTest implements J2clShadedClassTesting {

    @Test
    public void testMethodSignatureShadeFileNotFound() {
        assertThrows(Throwable.class, () -> {
            this.methodSignaturesCheck(J2clShadedClassTestingTest.class, J2clShadedClassTestingTest.class);
        });
    }

    @Test
    public void testMethodSignaturesCheckSameTypesNoShaded() {
        this.methodSignaturesCheck(J2clShadedClassTestingTest.class,
                J2clShadedClassTestingTest.class,
                Maps.empty());
    }

    @Test
    public void testMethodSignaturesCheckSameTypes() {
        this.methodSignaturesCheck(J2clShadedClassTestingTest.class,
                J2clShadedClassTestingTest.class,
                Maps.of("from", "to"));
    }


    @Test
    public void testMethodSignaturesShaded() {
        this.methodSignaturesCheck2(walkingkooka.reflect.j2clshadedclasstestingtest.package1.Shaded1.class,
                walkingkooka.reflect.j2clshadedclasstestingtest.package2.Shaded1.class);
    }

    @Test
    public void testMethodSignaturesShadedExtraIgnored() {
        this.methodSignaturesCheck2(walkingkooka.reflect.j2clshadedclasstestingtest.package1.ExtraMethod.class,
                walkingkooka.reflect.j2clshadedclasstestingtest.package2.ExtraMethod.class);
    }

    private void methodSignaturesCheck2(final Class<?> class1,
                                        final Class<?> class2) {
        this.methodSignaturesCheck(class1,
                class2,
                Maps.of(class1.getPackageName(), class2.getPackageName()));
    }

    @Test
    public void testMethodSignaturesShadedDifferentMethodParameterFails() {
        this.methodSignaturesCheckFails(walkingkooka.reflect.j2clshadedclasstestingtest.package1.DifferentMethodParameter.class,
                walkingkooka.reflect.j2clshadedclasstestingtest.package2.DifferentMethodParameter.class,
                "public java.lang.Object walkingkooka.reflect.j2clshadedclasstestingtest.package1.DifferentMethodParameter.method1(int,walkingkooka.reflect.j2clshadedclasstestingtest.Different)");
    }

    @Test
    public void testMethodSignaturesShadedDifferentMethodReturnTypeFails() {
        this.methodSignaturesCheckFails(walkingkooka.reflect.j2clshadedclasstestingtest.package1.DifferentMethodReturnType.class,
                walkingkooka.reflect.j2clshadedclasstestingtest.package2.DifferentMethodReturnType.class,
                "public walkingkooka.reflect.j2clshadedclasstestingtest.Different walkingkooka.reflect.j2clshadedclasstestingtest.package1.DifferentMethodReturnType.method1()");
    }

    @Test
    public void testMethodSignaturesShadedMissingMethodFails() {
        this.methodSignaturesCheckFails(walkingkooka.reflect.j2clshadedclasstestingtest.package1.MissingMethod.class,
                walkingkooka.reflect.j2clshadedclasstestingtest.package2.MissingMethod.class,
                "public void walkingkooka.reflect.j2clshadedclasstestingtest.package1.MissingMethod.missingMethod2()");
    }

    private void methodSignaturesCheckFails(final Class<?> class1,
                                            final Class<?> class2,
                                            final String... expected) {
        final Throwable thrown = assertThrows(Throwable.class, () -> {
            this.methodSignaturesCheck(class1,
                    class2,
                    Maps.of(class1.getPackageName(), class2.getPackageName()));
        });
        final String message = thrown.getMessage();
        assertTrue(Arrays.stream(expected).allMatch(c -> message.contains(c)), () -> message + " contains " + Arrays.stream(expected).collect(Collectors.joining(", ")));
    }
}
