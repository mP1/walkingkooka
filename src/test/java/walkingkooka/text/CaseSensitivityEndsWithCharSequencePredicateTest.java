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

final public class CaseSensitivityEndsWithCharSequencePredicateTest extends
    CaseSensitivityCharSequencePredicateTemplateTestCase<CaseSensitivityEndsWithCharSequencePredicate<String>> {

    @Test
    public void testSameCaseCaseSensitive() {
        this.testTrueCaseSensitive("ghi", "abc def ghi");
    }

    @Test
    public void testEqualsDifferentCaseCaseSensitive() {
        this.testFalseCaseSensitive("GHI", "abc def ghi");
    }

    @Test
    public void testMissingCaseSensitive() {
        this.testFalseCaseSensitive("xyz", "abc def ghi");
    }

    @Test
    public void testSameCaseCaseInsensitive() {
        this.testTrueCaseInsensitive("ghi", "abc def ghi");
    }

    @Test
    public void testEqualsDifferentCaseCaseInsensitive() {
        this.testTrueCaseInsensitive("GHI", "abc def ghi");
    }

    @Test
    public void testMissingCaseInsensitive() {
        this.testFalseCaseInsensitive("xyz", "abc def ghi");
    }

    @Override
    CaseSensitivityEndsWithCharSequencePredicate<String> createPredicate(final String chars,
                                                                         final CaseSensitivity sensitivity) {
        return CaseSensitivityEndsWithCharSequencePredicate.with(chars, sensitivity);
    }

    @Override
    String prefix() {
        return "ends with ";
    }

    @Override
    public Class<CaseSensitivityEndsWithCharSequencePredicate<String>> type() {
        return Cast.to(CaseSensitivityEndsWithCharSequencePredicate.class);
    }
}
