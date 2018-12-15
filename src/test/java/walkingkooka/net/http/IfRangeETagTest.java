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

import java.time.LocalDateTime;

public final class IfRangeETagTest extends IfRangeTestCase<IfRangeETag, HttpETag, LocalDateTime> {

    @Override
    IfRangeETag createHeaderValue(final HttpETag value) {
        return IfRangeETag.etag(value);
    }

    @Override
    HttpETag value() {
        return this.etag();
    }

    @Override
    String headerText() {
        return HttpHeaderValueConverter.httpETag().toText(this.value(), HttpHeaderName.E_TAG);
    }

    @Override
    HttpETag differentValue() {
        return HttpETagValidator.STRONG.setValue("different");
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
