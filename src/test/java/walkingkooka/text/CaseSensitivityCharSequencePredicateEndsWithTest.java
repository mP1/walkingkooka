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

final public class CaseSensitivityCharSequencePredicateEndsWithTest extends
    CaseSensitivityCharSequencePredicateTestCase<CaseSensitivityCharSequencePredicateEndsWith<String>> {

    @Test
    public void testTestSameCaseCaseSensitive() {
        this.testTrueCaseSensitive("ghi", "abc def ghi");
    }

    @Test
    public void testTestEqualsDifferentCaseCaseSensitive() {
        this.testFalseCaseSensitive(
            "GHI",
            "abc def ghi"
        );
    }

    @Test
    public void testTestMissingCaseSensitive() {
        this.testFalseCaseSensitive(
            "xyz",
            "abc def ghi"
        );
    }

    @Test
    public void testTestSameCaseCaseInsensitive() {
        this.testTrueCaseInsensitive(
            "ghi",
            "abc def ghi"
        );
    }

    @Test
    public void testTestEqualsDifferentCaseCaseInsensitive() {
        this.testTrueCaseInsensitive(
            "GHI",
            "abc def ghi"
        );
    }

    @Test
    public void testTestMissingCaseInsensitive() {
        this.testFalseCaseInsensitive(
            "xyz",
            "abc def ghi"
        );
    }

    @Override
    CaseSensitivityCharSequencePredicateEndsWith<String> createPredicate(final String chars,
                                                                         final CaseSensitivity sensitivity) {
        return CaseSensitivityCharSequencePredicateEndsWith.with(chars, sensitivity);
    }

    @Override
    String prefix() {
        return "ends with ";
    }

    // class............................................................................................................

    @Override
    public Class<CaseSensitivityCharSequencePredicateEndsWith<String>> type() {
        return Cast.to(CaseSensitivityCharSequencePredicateEndsWith.class);
    }
}
