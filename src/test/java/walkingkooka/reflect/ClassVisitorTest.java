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
import walkingkooka.visit.Visiting;

public final class ClassVisitorTest implements ClassVisitorTesting<ClassVisitor> {

    // Class............................................................................................................

    @Test
    public void testJavaLangObject() {
        new ClassVisitor() {
            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected Visiting startVisitImplementedInterface(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void endVisitImplementedInterface(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected Visiting startVisitArrayComponent(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }

            @Override
            protected void endVisitArrayComponent(final Class<?> classs) {
                throw new UnsupportedOperationException();
            }
        }.accept(Object.class);
    }

    @Test
    public void testJavaLangObject2() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }
        }.accept(Object.class);

        this.checkEquals(",start class:Object,end class:Object",
            b.toString());
    }

    @Test
    public void testJavaLangObject3() {
        new ClassVisitor() {
        }.accept(Object.class);
    }

    // extends super....................................................................................................

    @Test
    public void testSuperClass() {
        final StringBuilder b = new StringBuilder();

        new ClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start: ").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end: ").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super: ").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super: ").append(name(classs));
            }
        }.accept(TestSub.class);

        this.checkEquals(",start: Sub,start super: Object,start: Object,end: Object,end super: Object,end: Sub",
            b.toString());
    }

    @Test
    public void testSuperClass2() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }
        }.accept(TestSub.class);

        this.checkEquals(",start class:Sub,start super:Object,start class:Object,end class:Object,end super:Object,end class:Sub",
            b.toString());
    }

    static class TestSub {
    }

    @Test
    public void testSuperClass3() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }
        }.accept(TestSub2.class);

        this.checkEquals(",start class:Sub2,start super:Sub,start class:Sub,start super:Object,start class:Object,end class:Object,end super:Object,end class:Sub,end super:Sub,end class:Sub2",
            b.toString());
    }

    @Test
    public void testSuperClass4() {
        new ClassVisitor() {
        }.accept(TestSub2.class);
    }

    static class TestSub2 extends TestSub {
    }

    // implements interface.............................................................................................

    @Test
    public void testImplementsInterface() {
        final StringBuilder b = new StringBuilder();

        new ClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }

            @Override
            protected Visiting startVisitImplementedInterface(final Class<?> classs) {
                b.append(",start impl:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitImplementedInterface(final Class<?> classs) {
                b.append(",end impl:").append(name(classs));
            }
        }.accept(TestImplIntf.class);

        this.checkEquals(",start class:ImplIntf,start super:Object,start class:Object,end class:Object,end super:Object,start impl:Intf,start class:Intf,end class:Intf,end impl:Intf,end class:ImplIntf",
            b.toString());
    }

    @Test
    public void testImplementsInterface2() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }

            @Override
            protected Visiting startVisitImplementedInterface(final Class<?> classs) {
                b.append(",start intf:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitImplementedInterface(final Class<?> classs) {
                b.append(",end intf:").append(name(classs));
            }
        }.accept(TestImplIntf.class);

        this.checkEquals(",start class:ImplIntf,start super:Object,start class:Object,end class:Object,end super:Object,start intf:Intf,start class:Intf,end class:Intf,end intf:Intf,end class:ImplIntf",
            b.toString());
    }

    @Test
    public void testImplementsInterface3() {
        new ClassVisitor() {
        }.accept(TestImplIntf.class);
    }

    static class TestImplIntf implements TestIntf {
    }

    interface TestIntf {
    }

    @Test
    public void testImplementsInterface4() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }

            @Override
            protected Visiting startVisitImplementedInterface(final Class<?> classs) {
                b.append(",start intf:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitImplementedInterface(final Class<?> classs) {
                b.append(",end intf:").append(name(classs));
            }
        }.accept(TestImplIntf2.class);

        this.checkEquals(",start class:ImplIntf2,start super:ImplIntf,start class:ImplIntf,start super:Object,start class:Object,end class:Object,end super:Object,start intf:Intf,start class:Intf,end class:Intf,end intf:Intf,end class:ImplIntf,end super:ImplIntf,start intf:Intf2,start class:Intf2,end class:Intf2,end intf:Intf2,end class:ImplIntf2",
            b.toString());
    }

    static class TestImplIntf2 extends TestImplIntf implements TestIntf2 {
    }

    interface TestIntf2 {
    }

    // array.............................................................................................................

    @Test
    public void testArray() {
        final StringBuilder b = new StringBuilder();

        new FakeClassVisitor() {

            @Override
            protected Visiting startVisitClass(final Class<?> classs) {
                b.append(",start class:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitClass(final Class<?> classs) {
                b.append(",end class:").append(name(classs));
            }

            @Override
            protected Visiting startVisitArrayComponent(final Class<?> classs) {
                b.append(",start array-comp:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitArrayComponent(final Class<?> classs) {
                b.append(",end array-comp:").append(name(classs));
            }

            @Override
            protected Visiting startVisitSuperClass(final Class<?> classs) {
                b.append(",start super:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitSuperClass(final Class<?> classs) {
                b.append(",end super:").append(name(classs));
            }

            @Override
            protected Visiting startVisitImplementedInterface(final Class<?> classs) {
                b.append(",start intf:").append(name(classs));
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisitImplementedInterface(final Class<?> classs) {
                b.append(",end intf:").append(name(classs));
            }
        }.accept(Object[].class);

        this.checkEquals(",start class:Object[],start super:Object,start class:Object,end class:Object,end super:Object,start intf:Cloneable,start class:Cloneable,end class:Cloneable,end intf:Cloneable,start intf:Serializable,start class:Serializable,end class:Serializable,end intf:Serializable,start array-comp:Object,start class:Object,end class:Object,end array-comp:Object,end class:Object[]",
            b.toString());
    }

    @Test
    public void testArray2() {
        new ClassVisitor() {
        }.accept(Object[].class);
    }

    // ToString.........................................................................................................

    public void testCheckToStringOverridden() {
    }

    // ClassVisitorTesting...............................................................................................

    @Override
    public ClassVisitor createVisitor() {
        return new FakeClassVisitor();
    }


    private String name(final Class type) {
        final String typeName = type.getSimpleName();
        return typeName.substring(1 + Math.max(typeName.lastIndexOf('.'), typeName.lastIndexOf('$'))).replace("Test", "");
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNamePrefix() {
        return "";
    }

    // ClassTesting......................................................................................................

    @Override
    public Class<ClassVisitor> type() {
        return ClassVisitor.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
