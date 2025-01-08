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

package walkingkooka.visit;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A mixin interface with tests and helpers to assist in testing a {@link Visitor}
 */
public interface VisitorTesting<V extends Visitor<T>, T>
    extends ClassTesting<V>,
    ToStringTesting<V>,
    TypeNameTesting<V> {

    /**
     * All visitors must have protected constructors.
     */
    @Test
    default void testAllConstructorsVisibility() {
        this.checkEquals(Lists.empty(),
            Arrays.stream(this.type().getConstructors())
                .filter(c -> JavaVisibility.PROTECTED != JavaVisibility.of(c))
                .collect(Collectors.toList()));
    }

    @Test
    @Override
    default void testAllMethodsVisibility() {
        this.allMethodsVisibilityCheck("visit*");
    }

    @Test
    default void testAcceptNullFails() {
        assertThrows(NullPointerException.class, () -> this.createVisitor().accept(null));
    }

    @Test
    default void testSinglePublicAcceptMethod() {
        final List<Method> defaultAcceptMethods = Arrays.stream(this.type().getMethods())
            .filter(m -> m.getName().startsWith("accept"))
            .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        this.checkEquals(2,
            defaultAcceptMethods.size(),
            () -> "visitor " + this.type().getName() + " should have only 1 default accept method=" + defaultAcceptMethods);
    }

    // startVisit ......................................................................................................

    @Test
    default void testStartVisitMethodsInstance() {
        VisitorTesting2.instanceCheck("startVisit", this.type());
    }

    @Test
    default void testStartVisitMethodsProtected() {
        VisitorTesting2.protectedMethodCheck("startVisit", this.type());
    }

    @Test
    default void testStartVisitMethodsSingleParameter() {
        VisitorTesting2.singleParameterCheck("startVisit", this.type());
    }

    @Test
    default void testStartVisitMethodParameterTypesPublic() {
        VisitorTesting2.methodParameterTypesPublicCheck("startVisit", this.type());
    }

    @Test
    default void testStartVisitReturnTypeVisiting() {
        VisitorTesting2.methodReturnTypeVoidCheck("startVisit", this.type(), Visiting.class);
    }

    // endVisit ......................................................................................................

    @Test
    default void testEndVisitMethodsInstance() {
        VisitorTesting2.instanceCheck("endVisit", this.type());
    }

    @Test
    default void testEndVisitMethodsProtected() {
        VisitorTesting2.protectedMethodCheck("endVisit", this.type());
    }

    @Test
    default void testEndVisitMethodsSingleParameter() {
        VisitorTesting2.singleParameterCheck("endVisit", this.type());
    }

    @Test
    default void testEndVisitMethodParameterTypesPublic() {
        VisitorTesting2.methodParameterTypesPublicCheck("endVisit", this.type());
    }

    @Test
    default void testEndVisitReturnTypeVoid() {
        VisitorTesting2.methodReturnTypeVoidCheck("endVisit", this.type(), Void.TYPE);
    }

    // visit ......................................................................................................

    @Test
    default void testVisitMethodsInstance() {
        VisitorTesting2.instanceCheck("visit", this.type());
    }

    @Test
    default void testVisitMethodsProtected() {
        VisitorTesting2.protectedMethodCheck("visit", this.type());
    }

    @Test
    default void testVisitMethodsSingleParameter() {
        VisitorTesting2.singleParameterCheck("visit", this.type());
    }

    @Test
    default void testVisitMethodParameterTypesPublic() {
        VisitorTesting2.methodParameterTypesPublicCheck("visit", this.type());
    }

    @Test
    default void testVisitReturnTypeVoid() {
        VisitorTesting2.methodReturnTypeVoidCheck("visit", this.type(), Void.TYPE);
    }

    V createVisitor();
}
