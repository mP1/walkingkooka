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

import walkingkooka.net.header.HeaderValueConverter;

import java.time.LocalDateTime;

/**
 * A if-range holding a last modified date.
 */
final class IfRangeLastModified extends IfRange<LocalDateTime> {

    static IfRangeLastModified lastModified(final LocalDateTime value) {
        return new IfRangeLastModified(value);
    }

    private IfRangeLastModified(final LocalDateTime value) {
        super(value);
    }

    @Override
    public boolean isLastModified() {
        return true;
    }

    @Override
    public boolean isETag() {
        return false;
    }

    @Override
    HeaderValueConverter<LocalDateTime> converter() {
        return DATE_TIME;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof IfRangeLastModified;
    }
}
