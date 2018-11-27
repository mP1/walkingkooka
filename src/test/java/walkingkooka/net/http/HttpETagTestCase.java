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
import walkingkooka.test.PackagePrivateClassTestCase;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public abstract class HttpETagTestCase<E extends HttpETag> extends PackagePrivateClassTestCase<E> {

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
        this.check(etag.setValue(value), value, weak());
        this.check(etag);
    }

    // setWeak ...........................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetWeakNullFails() {
        this.createETag().setWeak(null);
    }

    @Test
    public final void testSetWeakSame() {
        final HttpETag etag = this.createETag();
        assertSame(etag, etag.setWeak(this.weak()));
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
        check(etag, value(), this.weak());
    }

    final void check(final HttpETag etag, final String value, final Optional<HttpETagWeak> weak) {
        assertEquals("value", value, etag.value());
        assertEquals("weak", weak, etag.weak());
    }

    abstract String value();

    abstract Optional<HttpETagWeak> weak();
}
