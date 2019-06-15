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

package walkingkooka.net.header;


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ContentDispositionParameterNameTest extends HeaderParameterNameTestCase<ContentDispositionParameterName<?>,
        ContentDispositionParameterName<?>> {

    @Test
    public void testControlCharacterFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionParameterName.with("parameter\u0001;");
        });
    }

    @Test
    public void testSpaceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionParameterName.with("parameter ");
        });
    }

    @Test
    public void testTabFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionParameterName.with("parameter\t");
        });
    }

    @Test
    public void testNonAsciiFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ContentDispositionParameterName.with("parameter\u0100;");
        });
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
        final ContentDispositionFileName filename = ContentDispositionFileName.notEncoded("readme.txt");

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
                "\"Wed, 12 Feb 1997 16:29:51 -0500\"",
                OffsetDateTime.of(1997, 2, 12, 16, 29, 51, 0, ZoneOffset.ofHours(-5)));
    }

    @Test
    public void testToValueString() {
        this.toValueAndCheck(Cast.to(ContentDispositionParameterName.with("xyz")),
                "abc",
                "abc");
    }

    @Override
    public ContentDispositionParameterName<Object> createName(final String name) {
        return Cast.to(ContentDispositionParameterName.with(name));
    }

    @Override
    public Class<ContentDispositionParameterName<?>> type() {
        return Cast.to(ContentDispositionParameterName.class);
    }
}
