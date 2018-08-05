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

import org.junit.Test;
import walkingkooka.naming.NameTestCase;

public final class FileNodeNameTest extends NameTestCase<FileNodeName> {

    @Test(expected = NullPointerException.class)
    public void testWithNullFails() {
        this.createName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyFails() {
        this.createName("");
    }

    @Test
    public void testWith() {
        final String value = "abc";
        final FileNodeName name = FileNodeName.with(value);
        assertEquals("value", value, name.value());
    }

    @Test
    public void testToString() {
        assertEquals("abc", this.createName("abc").toString());
    }

    @Override
    protected FileNodeName createName(final String name) {
        return FileNodeName.with(name);
    }

    @Override protected Class<FileNodeName> type() {
        return FileNodeName.class;
    }
}
