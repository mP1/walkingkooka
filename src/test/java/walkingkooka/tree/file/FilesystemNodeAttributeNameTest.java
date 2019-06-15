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

package walkingkooka.tree.file;

import walkingkooka.naming.NameTesting;
import walkingkooka.test.ClassTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.JavaVisibility;

public final class FilesystemNodeAttributeNameTest implements ClassTesting2<FilesystemNodeAttributeName>,
        NameTesting<FilesystemNodeAttributeName, FilesystemNodeAttributeName> {

    @Override
    public void testNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testCompareDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Override
    public FilesystemNodeAttributeName createName(final String name) {
        return FilesystemNodeAttributeName.valueOf(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    public String nameText() {
        return FilesystemNodeAttributeName.TEXT.value();
    }

    @Override
    public String differentNameText() {
        return FilesystemNodeAttributeName.HIDDEN.value();
    }

    @Override
    public String nameTextLess() {
        return FilesystemNodeAttributeName.CREATED.value();
    }

    @Override
    public Class<FilesystemNodeAttributeName> type() {
        return FilesystemNodeAttributeName.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
