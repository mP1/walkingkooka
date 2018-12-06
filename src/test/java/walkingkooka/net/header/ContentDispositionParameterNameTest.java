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

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

final public class ContentDispositionParameterNameTest extends HeaderParameterNameTestCase<ContentDispositionParameterName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testControlCharacterFails() {
        ContentDispositionParameterName.with("parameter\u0001;");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSpaceFails() {
        ContentDispositionParameterName.with("parameter ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTabFails() {
        ContentDispositionParameterName.with("parameter\t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonAsciiFails() {
        ContentDispositionParameterName.with("parameter\u0100;");
    }

    @Test
    public void testValid() {
        this.createNameAndCheck("Custom");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(ContentDispositionParameterName.CREATION_DATE, ContentDispositionParameterName.with(ContentDispositionParameterName.CREATION_DATE.value()));
    }

    // parameter value......................................................................................

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheckAbsent(ContentDispositionParameterName.CREATION_DATE,
                this.contentDisposition());
    }

    @Test
    public void testParameterValuePresent() {
        final ContentDispositionParameterName<ContentDispositionFileName> parameter = ContentDispositionParameterName.FILENAME;
        final ContentDispositionFileName filename = ContentDispositionFileName.with("readme.txt");

        this.parameterValueAndCheckPresent(parameter,
                this.contentDisposition(),
                filename);
    }

    private ContentDisposition contentDisposition() {
        return ContentDisposition.parse("attachment; filename=readme.txt");
    }

    // toValue...........................................................................................

    @Test
    public void testToValueOffsetDateTime() {
        this.toValueAndCheck(ContentDispositionParameterName.CREATION_DATE,
                "Wed, 12 Feb 1997 16:29:51 -0500",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(ContentDispositionParameterName.with("xyz")),
                "abc",
                "abc");
    }

    // toString...........................................................................................

    @Test
    public void testToString() {
        final String name = "parameter123";
        assertEquals(name, ContentDispositionParameterName.with(name).toString());
    }

    @Override
    protected ContentDispositionParameterName<Object> createName(final String name) {
        return Cast.to(ContentDispositionParameterName.with(name));
    }

    @Override
    protected String nameText() {
        return "parameter123";
    }

    @Override
    protected Class<ContentDispositionParameterName<?>> type() {
        return Cast.to(ContentDispositionParameterName.class);
    }
}
