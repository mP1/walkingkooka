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

import static org.junit.Assert.assertEquals;

public final class FilesystemNodeNameTest extends NameTestCase<FilesystemNodeName> {

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
        this.createNameAndCheck("abc");
    }

    @Test
    public void testToString() {
        assertEquals("abc", this.createName("abc").toString());
    }

    @Override
    protected FilesystemNodeName createName(final String name) {
        return FilesystemNodeName.with(name);
    }

    @Override protected Class<FilesystemNodeName> type() {
        return FilesystemNodeName.class;
    }
}
