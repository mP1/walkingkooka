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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

import java.util.Optional;

public final class CacheControlDirectiveEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<CacheControlDirective<Long>> {

    private final static Optional<Long> PARAMETER = Optional.of(123L);

    @Test
    public void testDifferentParameter() {
        this.checkNotEquals(this.directiveName().setParameter(Optional.of(456L)));
    }

    @Test
    public void testDifferentName() {
        this.checkNotEquals(CacheControlDirectiveName.S_MAXAGE.setParameter(PARAMETER));
    }

    @Override
    protected CacheControlDirective<Long> createObject() {
        return this.directiveName().setParameter(PARAMETER);
    }

    private CacheControlDirectiveName<Long> directiveName() {
        return CacheControlDirectiveName.MAX_AGE;
    }
}
