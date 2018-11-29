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

package walkingkooka.net.http;

import org.junit.Test;
import walkingkooka.net.header.HeaderValueTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public abstract class HttpETagTestCase<E extends HttpETag> extends HeaderValueTestCase<E> {

    HttpETagTestCase() {
        super();
    }

    // setValue ...........................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetValueNullFails() {
        this.createETag().setValue(null);
    }

    @Test
    public final void testSetValueEmptyFails() {
        this.createETag().setValue("");
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testSetValueInvalidCharactersFails() {
        this.createETag().setValue(" ");
    }

    @Test
    public final void testSetValueSame() {
        final HttpETag etag = this.createETag();
        assertSame(etag, etag.setValue(this.value()));
    }

    @Test
    public final void testSetValueDifferent() {
        final HttpETag etag = this.createETag();
        final String value = "different";
        this.check(etag.setValue(value), value, validator());
        this.check(etag);
    }

    // setValidator ...........................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetValidatorNullFails() {
        this.createETag().setValidator(null);
    }

    @Test
    public final void testSetValidatorSame() {
        final HttpETag etag = this.createETag();
        assertSame(etag, etag.setValidator(this.validator()));
    }

    // isWildcard ...........................................................................................

    @Test
    public final void testIsWildcard() {
        assertEquals(this.isWildcard(), this.createETag().isWildcard());
    }

    abstract boolean isWildcard();

    // helper ...........................................................................................................

    abstract E createETag();

    final void check(final HttpETag etag) {
        check(etag, value(), this.validator());
    }

    final void check(final HttpETag etag, final String value, final HttpETagValidator validator) {
        assertEquals("value", value, etag.value());
        assertEquals("validator", validator, etag.validator());
    }

    abstract String value();

    abstract HttpETagValidator validator();

    @Override
    protected final boolean typeMustBePublic() {
        return false;
    }
}
