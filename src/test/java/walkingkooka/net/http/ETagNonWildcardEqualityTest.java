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
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class ETagNonWildcardEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<ETagNonWildcard> {

    private final static String VALUE = "0123456789ABCDEF";

    @Test
    public void testDifferentValue() {
        this.checkNotEquals(ETagNonWildcard.with0("different", ETagValidator.STRONG));
    }

    @Test
    public void testDifferentWeak() {
        this.checkNotEquals(ETagNonWildcard.with0(VALUE, ETagValidator.STRONG));
    }

    @Override
    protected ETagNonWildcard createObject() {
        return ETagNonWildcard.with0(VALUE, ETagValidator.WEAK);
    }
}
