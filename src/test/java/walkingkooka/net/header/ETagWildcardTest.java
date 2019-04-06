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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ETagWildcardTest extends ETagTestCase<ETagWildcard> {

    @Test
    public void testSetValidatorWeakFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            ETagWildcard.instance().setValidator(ETagValidator.WEAK);
        });
    }

    // test ...........................................................................................

    @Test
    public void testTestStrongValue() {
        this.testTrue(ETagValidator.STRONG.setValue("abc"));
    }

    @Test
    public void testTestWeakValue() {
        this.testTrue(ETagValidator.WEAK.setValue("abc"));
    }

    @Test
    public void testNonWildcard() {
        this.checkNotEquals(ETag.with("0123456789", ETagValidator.STRONG));
    }

    @Override
    ETagWildcard createETag() {
        return ETagWildcard.instance();
    }

    @Override
    String value() {
        return ETag.WILDCARD_VALUE.string();
    }

    @Override
    ETagValidator validator() {
        return ETagValidator.STRONG;
    }

    @Override
    boolean isWildcard() {
        return true;
    }

    @Override
    public Class<ETagWildcard> type() {
        return ETagWildcard.class;
    }
}
