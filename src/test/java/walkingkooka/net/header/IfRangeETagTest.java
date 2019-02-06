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

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class IfRangeETagTest extends IfRangeTestCase<IfRangeETag, ETag, LocalDateTime> {

    @Test
    public void testETag() {
        final IfRangeETag ifRange = this.createHeaderValue();
        assertSame(ifRange, ifRange.etag());
    }

    @Test
    public void testLastModified() {
        assertThrows(HeaderValueException.class, () -> {
            this.createHeaderValue().lastModified();
        });
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
        return HeaderValueConverter.eTag().toText(this.value(), HttpHeaderName.E_TAG);
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
    public Class<IfRangeETag> type() {
        return IfRangeETag.class;
    }
}
