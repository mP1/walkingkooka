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

import walkingkooka.net.header.HeaderValueConverters;

import java.time.LocalDateTime;

public final class IfRangeLastModifiedTest extends IfRangeTestCase<IfRangeLastModified, LocalDateTime, HttpETag> {

    @Override
    IfRangeLastModified createHeaderValue(final LocalDateTime value) {
        return IfRangeLastModified.lastModified(value);
    }

    @Override
    LocalDateTime value() {
        return this.lastModified();
    }

    @Override
    String headerText() {
        return HeaderValueConverters.localDateTime().toText(this.value(), HttpHeaderName.LAST_MODIFIED);
    }

    @Override
    LocalDateTime differentValue() {
        return this.value().plusYears(1);
    }

    @Override
    HttpETag otherValue() {
        return this.etag();
    }

    @Override
    boolean isETag() {
        return false;
    }

    @Override
    protected Class<IfRangeLastModified> type() {
        return IfRangeLastModified.class;
    }
}
