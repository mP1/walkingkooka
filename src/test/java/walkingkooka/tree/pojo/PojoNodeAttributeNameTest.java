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
package walkingkooka.tree.pojo;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CaseSensitivity;

public final class PojoNodeAttributeNameTest extends NameTestCase<PojoNodeAttributeName, PojoNodeAttributeName> {

    @Test
    @Ignore
    @Override
    public void testWith() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testCompareLess() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testCompareDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    @Override
    public void testDifferentText() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected PojoNodeAttributeName createName(final String name) {
        return PojoNodeAttributeName.valueOf(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return PojoNodeAttributeName.CLASS.name();
    }

    @Override
    protected String differentNameText() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String nameTextLess() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Class<PojoNodeAttributeName> type() {
        return PojoNodeAttributeName.class;
    }
}
