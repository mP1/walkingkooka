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
import walkingkooka.net.http.HttpHeaderName;

import java.time.LocalDateTime;

import static org.junit.Assert.assertSame;

public final class IfRangeETagTest extends IfRangeTestCase<IfRangeETag, ETag, LocalDateTime> {

    @Test
    public void testETag() {
        final IfRangeETag ifRange = this.createHeaderValue();
        assertSame(ifRange, ifRange.etag());
    }

    @Test(expected = HeaderValueException.class)
    public void testLastModified() {
        this.createHeaderValue().lastModified();
    }

    @Override
    IfRangeETag createHeaderValue(final ETag value) {
        return IfRangeETag.etag(value);
    }

    @Override
    ETag value() {
        return this.etag();
    }

    @Override
    String headerText() {
        return HeaderValueConverters.eTag().toText(this.value(), HttpHeaderName.E_TAG);
    }

    @Override
    ETag differentValue() {
        return ETagValidator.STRONG.setValue("different");
    }

    @Override
    LocalDateTime otherValue() {
        return this.lastModified();
    }

    @Override
    boolean isETag() {
        return true;
    }

    @Override
    protected Class<IfRangeETag> type() {
        return IfRangeETag.class;
    }
}
