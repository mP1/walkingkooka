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

package walkingkooka.tree.visit;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTestCase;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;
import walkingkooka.type.MethodAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class VisitorTestCase<V extends Visitor<T>, T>
        extends
        ClassTestCase<V>
        implements ToStringTesting<V> {

    protected VisitorTestCase() {
        super();
    }

    /**
     * All visitors must have protected constructors.
     */
    @Test
    public void testAllConstructorsVisibility() {
        this.checkAllConstructorsVisibility(MemberVisibility.PROTECTED);
    }

    @Test
    public final void checkNaming() {
        this.checkNamingStartAndEnd(this.requiredNamePrefix(), this.requiredNameSuffix());
    }

    abstract protected String requiredNamePrefix();

    abstract protected String requiredNameSuffix();

    @Test
    public final void testAcceptNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createVisitor().accept(null);
        });
    }

    @Test
    public final void testSinglePublicAcceptMethod() {
        final List<Method> publicAcceptMethods = Arrays.stream(this.type().getMethods())
               .filter(m -> m.getName().startsWith("accept"))
               .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        assertEquals(2,
                publicAcceptMethods.size(),
                ()-> "visitor " + this.type().getName() + " should have only 1 public accept method=" + publicAcceptMethods);
    }

    // all visit methods are protected..

    @Test
    public final void testVisitMethodsProtected() {
        this.visitMethodsProtectedCheck("visit");
    }

    @Test
    public final void testStartVisitMethodsProtected() {
        this.visitMethodsProtectedCheck("startVisit");
    }

    @Test
    public final void testEndVisitMethodsProtected() {
        this.visitMethodsProtectedCheck("endVisit");
    }

    private void visitMethodsProtectedCheck(final String name) {
        final List<Method> wrong = this.allMethods()
                .stream()
                .filter(m -> !MethodAttributes.STATIC.is(m)) // only interested in instance methods.
                .filter(m -> m.getName().startsWith(name))
                .filter(m -> ! MemberVisibility.PROTECTED.is(m))
                .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        assertEquals(Lists.empty(), wrong, ()-> "all " + name + " methods in " + this.type().getName() + " should be protected=" + wrong);
    }

    // all visit methods have a single parameter.

    @Test
    public final void testVisitMethodsSingleParameter() {
        this.visitMethodsSingleParameterCheck("visit");
    }

    @Test
    public final void testStartVisitMethodsSingleParameter() {
        this.visitMethodsSingleParameterCheck("startVisit");
    }

    @Test
    public final void testEndVisitMethodsSingleParameter() {
        this.visitMethodsSingleParameterCheck("endVisit");
    }

    private void visitMethodsSingleParameterCheck(final String name) {
        final List<Method> wrong = this.allMethods()
                .stream()
                .filter(m -> !MethodAttributes.STATIC.is(m)) // only interested in instance methods.
                .filter(m -> m.getName().startsWith(name))
                .filter(m -> MemberVisibility.PROTECTED.is(m))
                .filter(m -> m.getParameterTypes().length != 1)
                .collect(Collectors.toList());

        // because of generics two accept methods will be present accept(Object) and accept(N)
        assertEquals(Lists.empty(), wrong, ()-> "all " + name + " methods in " + this.type().getName() + " should have 1 parameter=" + wrong);
    }

    private List<Method> allMethods() {
        final List<Method> all = Lists.array();
        Class<?> type = this.type();
        do {
            all.addAll(Lists.of(type.getMethods()));

            type = type.getSuperclass();
        } while(type != Object.class);

        return all;
    }


    abstract protected V createVisitor();

    @Override
    protected final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
