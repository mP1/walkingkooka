/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;

final public class CaseSensitivityCharSequencePredicateEqualsTest extends
    CaseSensitivityCharSequencePredicateTestCase<CaseSensitivityCharSequencePredicateEquals<String>> {

    @Override
    @Test
    public void testWithEmptyCharacterSequenceFails() {
        // ok
    }

    @Test
    public void testTestSameCaseCaseSensitive() {
        this.testTrueCaseSensitive(
            "abc",
            "abc"
        );
    }

    @Test
    public void testTestEqualsDifferentCaseCaseSensitive() {
        this.testFalseCaseSensitive(
            "ABC",
            "abc"
        );
    }

    @Test
    public void testTestMissingCaseSensitive() {
        this.testFalseCaseSensitive(
            "xyz",
            "abc"
        );
    }

    @Test
    public void testTestSameCaseCaseInsensitive() {
        this.testTrueCaseInsensitive(
            "abc",
            "abc"
        );
    }

    @Test
    public void testTestEqualsDifferentCaseCaseInsensitive() {
        this.testTrueCaseInsensitive(
            "ABC",
            "abc"
        );
    }

    @Test
    public void testTestMissingCaseInsensitive() {
        this.testFalseCaseInsensitive(
            "xyz",
            "abc"
        );
    }

    @Test
    public void testTestMustMatchEmpty() {
        this.testTrueCaseInsensitive(
            "",
            ""
        );
    }

    @Override
    CaseSensitivityCharSequencePredicateEquals<String> createPredicate(final String chars,
                                                                       final CaseSensitivity sensitivity) {
        return CaseSensitivityCharSequencePredicateEquals.with(chars, sensitivity);
    }

    @Override
    String prefix() {
        return "";
    }

    // class............................................................................................................

    @Override
    public Class<CaseSensitivityCharSequencePredicateEquals<String>> type() {
        return Cast.to(CaseSensitivityCharSequencePredicateEquals.class);
    }
}
