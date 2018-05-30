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
 */

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class QuotesAroundCharSequenceEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<QuotesAroundCharSequence> {

    // constants

    private final static CharSequence SEQUENCE = "quoted";

    // tests

    @Test
    public void testDifferentSequence() {
        this.checkNotEquals(QuotesAroundCharSequence.with("different"));
    }

    @Override
    protected QuotesAroundCharSequence createObject() {
        return (QuotesAroundCharSequence) QuotesAroundCharSequence.with(
                QuotesAroundCharSequenceEqualityTest.SEQUENCE);
    }
}
