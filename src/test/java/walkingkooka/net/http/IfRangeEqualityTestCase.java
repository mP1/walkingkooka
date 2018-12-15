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
import walkingkooka.Cast;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.time.LocalDateTime;

public abstract class IfRangeEqualityTestCase<R extends IfRange<T>, T> extends HashCodeEqualsDefinedEqualityTestCase<R> {

    IfRangeEqualityTestCase() {
        super();
    }

    @Test
    public final void testDifferentValue() {
        this.checkNotEquals(IfRange.with(this.differentValue()));
    }

    @Test
    public final void testDifferentOtherValue() {
        this.checkNotEquals(IfRange.with(this.otherValue()));
    }

    @Override
    protected final R createObject() {
        return Cast.to(IfRange.with(this.value()));
    }

    abstract T value();

    abstract T differentValue();

    abstract Object otherValue();

    final HttpETag etag() {
        return HttpETag.with("abc123", HttpETagValidator.WEAK);
    }

    final LocalDateTime lastModified() {
        return LocalDateTime.of(2000, 12, 31, 6, 28, 29);
    }
}
