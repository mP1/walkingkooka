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
package walkingkooka.tree.pojo;

import org.junit.jupiter.api.Test;
import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.JavaVisibility;

public final class PojoNodeAttributeNameTest implements ClassTesting2<PojoNodeAttributeName>,
        NameTesting<PojoNodeAttributeName, PojoNodeAttributeName> {

    @Override
    public void testWith() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCompareLess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCompareDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testDifferentText() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Override
    public void testToString() {
        this.toStringAndCheck(PojoNodeAttributeName.CLASS, "class");
    }

    @Override
    public PojoNodeAttributeName createName(final String name) {
        return PojoNodeAttributeName.valueOf(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return PojoNodeAttributeName.CLASS.name();
    }

    @Override
    public String differentNameText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String nameTextLess() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<PojoNodeAttributeName> type() {
        return PojoNodeAttributeName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
