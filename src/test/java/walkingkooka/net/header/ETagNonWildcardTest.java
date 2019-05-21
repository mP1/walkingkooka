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

public final class ETagNonWildcardTest extends ETagTestCase<ETagNonWildcard> {

    private final static String VALUE = "0123456789ABCDEF";

    @Test
    public void testWith() {
        this.check(this.createETag());
    }

    @Test
    public void testWithEmpty() {
        this.check(ETag.with("", ETagValidator.STRONG), "", ETagValidator.STRONG);
    }

    @Test
    public void testSetValidatorDifferent() {
        final ETag etag = this.createETag();
        final ETagValidator validator = ETagValidator.WEAK;
        this.check(etag.setValidator(validator), this.value(), validator);
        this.check(etag);
    }

    // test...........................................................................................................

    private final static String DIFFERENT_VALUE = "FEDCBA9876543210";

    @Test
    public void testTestStrongStrongSameValue() {
        this.testTrue(ETagValidator.STRONG.setValue(VALUE),
                ETagValidator.STRONG.setValue(VALUE));
    }

    @Test
    public void testTestStrongStrongDifferentValue() {
        this.testFalse(ETagValidator.STRONG.setValue(VALUE),
                ETagValidator.STRONG.setValue(DIFFERENT_VALUE));
    }

    @Test
    public void testTestStrongWeakSameValue() {
        this.testTrue(ETagValidator.STRONG.setValue(VALUE),
                ETagValidator.WEAK.setValue(VALUE));
    }

    @Test
    public void testTestStrongWeakDifferentValue() {
        this.testFalse(ETagValidator.STRONG.setValue(VALUE),
                ETagValidator.WEAK.setValue(DIFFERENT_VALUE));
    }

    @Test
    public void testTestWeakStrongSameValue() {
        this.testTrue(ETagValidator.WEAK.setValue(VALUE),
                ETagValidator.STRONG.setValue(VALUE));
    }

    @Test
    public void testTestWeakStrongDifferentValue() {
        this.testFalse(ETagValidator.WEAK.setValue(VALUE),
                ETagValidator.STRONG.setValue(DIFFERENT_VALUE));
    }

    @Test
    public void testTestWeakWeakSameValue() {
        this.testTrue(ETagValidator.WEAK.setValue(VALUE),
                ETagValidator.WEAK.setValue(VALUE));
    }

    @Test
    public void testTestWeakWeakDifferentValue() {
        this.testFalse(ETagValidator.WEAK.setValue(VALUE),
                ETagValidator.WEAK.setValue(DIFFERENT_VALUE));
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(ETagNonWildcard.with0("different", ETagValidator.STRONG));
    }

    @Test
    public void testEqualsDifferentWeak() {
        this.checkNotEquals(ETagNonWildcard.with0(VALUE, ETagValidator.WEAK));
    }

    @Override
    ETagNonWildcard createETag() {
        return ETagNonWildcard.with0(VALUE, ETagValidator.STRONG);
    }

    @Override
    String value() {
        return VALUE;
    }

    @Override
    ETagValidator validator() {
        return ETagValidator.STRONG;
    }

    @Override
    boolean isWildcard() {
        return false;
    }

    @Override
    public Class<ETagNonWildcard> type() {
        return ETagNonWildcard.class;
    }
}
