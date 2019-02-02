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
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RangeHeaderValueUnitTest extends HeaderValueTestCase<RangeHeaderValueUnit> {

    // isWildcard ..................................................................................................

    @Test
    public void testIsWildcard() {
        this.isWildcardAndCheck(false);
    }

    // parse ..................................................................................................

    @Test
    public void testParseNullFails() {
        assertThrows(NullPointerException.class, () -> {
            RangeHeaderValueUnit.parse(null);
        });
    }

    @Test
    public void testParseEmptyFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RangeHeaderValueUnit.parse("");
        });
    }

    @Test
    public void testParseUnknownFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            RangeHeaderValueUnit.parse("unknown");
        });
    }

    @Test
    public void testParseBytes() {
        assertSame(RangeHeaderValueUnit.BYTES, RangeHeaderValueUnit.parse("bytes"));
    }

    @Test
    public void testParseBytesCaseUnimportant() {
        assertSame(RangeHeaderValueUnit.BYTES, RangeHeaderValueUnit.parse("BYtes"));
    }

    @Test
    public void testParseNone() {
        assertSame(RangeHeaderValueUnit.NONE, RangeHeaderValueUnit.parse("none"));
    }

    @Override
    protected RangeHeaderValueUnit createHeaderValue() {
        return RangeHeaderValueUnit.BYTES;
    }

    @Override
    protected boolean isMultipart() {
        return false;
    }

    @Override
    protected boolean isRequest() {
        return true;
    }

    @Override
    protected boolean isResponse() {
        return true;
    }

    @Override
    protected Class<RangeHeaderValueUnit> type() {
        return RangeHeaderValueUnit.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
