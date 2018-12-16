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
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public abstract class IfRangeTestCase<R extends IfRange<V>, V, W> extends HeaderValueTestCase<R> {

    IfRangeTestCase() {
        super();
    }

    @Test
    public final void testWith() {
        final V value = this.value();
        this.check(this.createHeaderValue(value), value);
    }

    @Test
    public final void testWith2() {
        final V value = this.differentValue();
        this.check(this.createHeaderValue(value), value);
    }

    // setValue.................................................................................................

    @Test(expected = NullPointerException.class)
    public final void testSetValueNullFails() {
        this.createHeaderValue().setValue(null);
    }

    @Test
    public final void testSetValueSame() {
        final R range = this.createHeaderValue();
        assertSame(range, range.setValue(this.value()));
    }

    @Test
    public final void testSetValueDifferent() {
        final R range = this.createHeaderValue();
        final V value = this.differentValue();
        final IfRange<V> different = range.setValue(value);
        assertNotSame(range, different);
        this.check(different, value);
    }

    @Test
    public final void testSetValueOtherType() {
        final R range = this.createHeaderValue();
        final W value = this.otherValue();
        final IfRange<W> different = range.setValue(value);
        assertNotSame(range, different);
        this.check(different, value);
    }

    final void check(final IfRange<V> range) {
        this.check(range, this.value());
    }

    final <VV> void check(final IfRange<VV> range, final VV value) {
        assertEquals("value", value, range.value());
    }

    @Test
    public final void testIsETag() {
        assertEquals(this.isETag(), this.createHeaderValue().isETag());
    }

    @Test
    public final void testIsLastModified() {
        assertEquals(!this.isETag(), this.createHeaderValue().isLastModified());
    }

    @Test
    public final void testToHeaderText() {
        this.toHeaderTextAndCheck(this.createHeaderValue(),
                this.headerText());
    }

    @Test
    public final void testToString() {
        assertEquals(this.headerText(),
                this.createHeaderValue().toString());
    }

    @Test
    public final void testParse() {
        final String text = this.headerText();
        assertEquals("Parsing " + CharSequences.quote(text),
                this.createHeaderValue(),
                IfRange.parse(text));
    }

    @Override
    protected final HttpHeaderScope httpHeaderScope() {
        return HttpHeaderScope.REQUEST;
    }

    @Override
    protected final R createHeaderValue() {
        return this.createHeaderValue(this.value());
    }

    abstract R createHeaderValue(final V value);

    abstract V value();

    abstract V differentValue();

    abstract W otherValue();

    abstract boolean isETag();

    abstract String headerText();

    final ETag etag() {
        return ETag.with("abc123", ETagValidator.WEAK);
    }

    final LocalDateTime lastModified() {
        return LocalDateTime.of(2000, 12, 31, 6, 28, 29);
    }

    @Override
    protected boolean typeMustBePublic() {
        return false;
    }
}
