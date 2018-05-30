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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.type.ClassAttributes;

public final class ClassAttributesTest extends EnumTestCase<ClassAttributes> {

    @Test
    public void testFinal() {
        Assert.assertEquals(Sets.of(ClassAttributes.FINAL), ClassAttributes.get(this.getClass()));
    }

    @Test
    public void testStatic() {
        Assert.assertEquals(Sets.of(ClassAttributes.STATIC), ClassAttributes.get(TestStaticClass.class));
    }

    static class TestStaticClass{}


    @Test
    public void testAbstract() {
        Assert.assertEquals(Sets.of(ClassAttributes.ABSTRACT), ClassAttributes.get(TestAbstractClass.class));
    }

    abstract class TestAbstractClass{}

    @Test
    public void testAbstractStatic() {
        Assert.assertEquals(Sets.of(ClassAttributes.ABSTRACT, ClassAttributes.STATIC), ClassAttributes.get(TestAbstractStaticClass.class));
    }

    abstract static class TestAbstractStaticClass{}

    @Override protected Class<ClassAttributes> type() {
        return ClassAttributes.class;
    }
}
