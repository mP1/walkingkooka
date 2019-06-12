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

import java.time.LocalDateTime;

/**
 * An if range holding an etag.
 */
final class IfRangeETag extends IfRange<ETag> {

    static IfRangeETag etag(final ETag value) {
        return new IfRangeETag(value);
    }

    private IfRangeETag(final ETag value) {
        super(value);
    }

    @Override
    public boolean isETag() {
        return true;
    }

    @Override
    public boolean isLastModified() {
        return false;
    }

    @Override
    public IfRange<ETag> etag() {
        return this;
    }

    @Override
    public IfRange<LocalDateTime> lastModified() {
        throw new HeaderValueException(this.toHeaderText());
    }

    @Override
    HeaderValueHandler<ETag> handler() {
        return ETAG;
    }

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof IfRangeETag;
    }
}
