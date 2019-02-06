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


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.NameTesting2;
import walkingkooka.test.ClassTestCase;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.type.MemberVisibility;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ContentDispositionTypeTest extends ClassTestCase<ContentDispositionType>
        implements NameTesting2<ContentDispositionType, ContentDispositionType> {

    @Override
    public void testNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(ContentDispositionType.INLINE, ContentDispositionType.with(ContentDispositionType.INLINE.value()));
    }

    @Test
    public void testConstantNameReturnsConstantCaseIgnored() {
        assertSame(ContentDispositionType.INLINE, ContentDispositionType.with(ContentDispositionType.INLINE.value().toUpperCase()));
    }

    @Test
    public void testSetFilenameNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentDispositionType.ATTACHMENT.setFilename(null);
        });
    }

    @Test
    public void testSetFilename() {
        final ContentDispositionFileName filename = ContentDispositionFileName.notEncoded("readme.txt");
        final ContentDispositionType type = ContentDispositionType.ATTACHMENT;

        final ContentDisposition disposition = type.setFilename(filename);
        assertEquals(type, disposition.type(),"type");
        assertEquals(Maps.one(ContentDispositionParameterName.FILENAME, filename),
                disposition.parameters(),
                "parameters");
    }

    @Test
    public void testSetParametersNullFails() {
        assertThrows(NullPointerException.class, () -> {
            ContentDispositionType.ATTACHMENT.setParameters(null);
        });
    }

    @Test
    public void testSetParameters() {
        final ContentDispositionFileName filename = ContentDispositionFileName.notEncoded("readme.txt");
        final ContentDispositionType type = ContentDispositionType.ATTACHMENT;
        final Map<ContentDispositionParameterName<?>, Object> parameters = Maps.one(ContentDispositionParameterName.FILENAME, filename);
        final ContentDisposition disposition = type.setParameters(parameters);
        assertEquals(type, disposition.type(), "type");
        assertEquals(parameters,
                disposition.parameters(),
                "parameters");
    }

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(ContentDispositionType.ATTACHMENT);
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEquals(ContentDispositionType.with(ContentDispositionType.INLINE.value().toUpperCase()));
    }

    @Test
    public void testToString() {
        final String value = "abc123";
        this.toStringAndCheck(ContentDispositionType.with(value), value);
    }

    @Override
    public ContentDispositionType createName(final String name) {
        return ContentDispositionType.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return "inline";
    }

    @Override
    public String differentNameText() {
        return "form-data";
    }

    @Override
    public String nameTextLess() {
        return "attachment";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String possibleValidChars(final int position) {
        return RFC2045;
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return CONTROL + RFC2045_TSPECIAL + BYTE_NON_ASCII;
    }

    @Override
    public Class<ContentDispositionType> type() {
        return Cast.to(ContentDispositionType.class);
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
