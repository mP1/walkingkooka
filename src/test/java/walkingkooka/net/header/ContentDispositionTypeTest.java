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

package walkingkooka.net.header;


import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.test.PublicClassTestCase;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ContentDispositionTypeTest extends PublicClassTestCase<ContentDispositionType> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        ContentDispositionType.with("parameter\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        ContentDispositionType.with("parameter ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        ContentDispositionType.with("parameter\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        ContentDispositionType.with("parameter\u0100;");
    }

    @Test
    public void testWith() {
        final String value = "abc123";
        final ContentDispositionType type = ContentDispositionType.with(value);
        assertEquals("value", value, type.value());
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(ContentDispositionType.INLINE, ContentDispositionType.with(ContentDispositionType.INLINE.value()));
    }

    @Test
    public void testConstantNameReturnsConstantCaseIgnored() {
        assertSame(ContentDispositionType.INLINE, ContentDispositionType.with(ContentDispositionType.INLINE.value().toUpperCase()));
    }

    @Test(expected = NullPointerException.class)
    public void testSetFilenameNullFails() {
        ContentDispositionType.ATTACHMENT.setFilename(null);
    }

    @Test
    public void testSetFilename() {
        final ContentDispositionFilename filename = ContentDispositionFilename.with("readme.txt");
        final ContentDispositionType type = ContentDispositionType.ATTACHMENT;

        final ContentDisposition disposition = type.setFilename(filename);
        assertEquals("type", type, disposition.type());
        assertEquals("parameters",
                Maps.one(ContentDispositionParameterName.FILENAME, filename),
                disposition.parameters());
    }

    @Test(expected = NullPointerException.class)
    public void testSetParametersNullFails() {
        ContentDispositionType.ATTACHMENT.setParameters(null);
    }

    @Test
    public void testSetParameters() {
        final ContentDispositionFilename filename = ContentDispositionFilename.with("readme.txt");
        final ContentDispositionType type = ContentDispositionType.ATTACHMENT;
        final Map<ContentDispositionParameterName<?>, Object> parameters = Maps.one(ContentDispositionParameterName.FILENAME, filename);
        final ContentDisposition disposition = type.setParameters(parameters);
        assertEquals("type", type, disposition.type());
        assertEquals("parameters",
                parameters,
                disposition.parameters());
    }

    @Test
    public void testToString() {
        final String value = "abc123";
        assertEquals(value, ContentDispositionType.with(value).toString());
    }

    @Override
    protected Class<ContentDispositionType> type() {
        return Cast.to(ContentDispositionType.class);
    }
}
