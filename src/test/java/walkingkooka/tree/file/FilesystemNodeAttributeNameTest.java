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

package walkingkooka.tree.file;

import org.junit.Ignore;
import org.junit.Test;
import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CaseSensitivity;

public final class FilesystemNodeAttributeNameTest extends NameTestCase<FilesystemNodeAttributeName, FilesystemNodeAttributeName> {

    @Test
    @Ignore
    public void testNullFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void testEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Ignore
    public void testCompareDifferentCase() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected FilesystemNodeAttributeName createName(final String name) {
        return FilesystemNodeAttributeName.valueOf(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return FilesystemNodeAttributeName.TEXT.value();
    }

    @Override
    protected String differentNameText() {
        return FilesystemNodeAttributeName.HIDDEN.value();
    }

    @Override
    protected String nameTextLess() {
        return FilesystemNodeAttributeName.CREATED.value();
    }

    @Override
    protected Class<FilesystemNodeAttributeName> type() {
        return FilesystemNodeAttributeName.class;
    }
}
