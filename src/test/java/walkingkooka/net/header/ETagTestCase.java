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
import walkingkooka.InvalidCharacterException;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ETagTestCase<E extends ETag> extends HeaderValueTestCase<E>
        implements PredicateTesting2<E, ETag> {

    ETagTestCase() {
        super();
    }

    // setValue ...........................................................................................

    @Test
    public final void testSetValueNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createETag().setValue(null);
        });
    }

    @Test
    public final void testSetValueEmptyFails() {
        this.createETag().setValue("");
    }

    @Test
    public final void testSetValueInvalidCharactersFails() {
        assertThrows(InvalidCharacterException.class, () -> {
            this.createETag().setValue(" ");
        });
    }

    @Test
    public final void testSetValueSame() {
        final ETag etag = this.createETag();
        assertSame(etag, etag.setValue(this.value()));
    }

    @Test
    public final void testSetValueDifferent() {
        final ETag etag = this.createETag();
        final String value = "different";
        this.check(etag.setValue(value), value, validator());
        this.check(etag);
    }

    // setValidator ...........................................................................................

    @Test
    public final void testSetValidatorNullFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createETag().setValidator(null);
        });
    }

    @Test
    public final void testSetValidatorSame() {
        final ETag etag = this.createETag();
        assertSame(etag, etag.setValidator(this.validator()));
    }

    // isWildcard ...........................................................................................

    @Test
    public final void testIsWildcard() {
        this.isWildcardAndCheck(this.isWildcard());
    }

    abstract boolean isWildcard();

    // test................ ...........................................................................................

    @Test
    public final void testTestWildcard() {
        this.testFalse(ETag.wildcard());
    }

    // helper ...........................................................................................................

    @Override
    public final E createHeaderValue() {
        return this.createETag();
    }

    @Override
    public final E createPredicate() {
        return this.createETag();
    }

    abstract E createETag();

    final void check(final ETag etag) {
        check(etag, value(), this.validator());
    }

    final void check(final ETag etag, final String value, final ETagValidator validator) {
        assertEquals(value, etag.value(), "value");
        assertEquals(validator, etag.validator(), "validator");
    }

    abstract String value();

    abstract ETagValidator validator();

    @Override
    public boolean isMultipart() {
        return false;
    }

    @Override
    public boolean isRequest() {
        return true;
    }

    @Override
    public boolean isResponse() {
        return true;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return ETag.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }
}
