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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.predicate.PredicateTestCase;

import static org.junit.Assert.assertEquals;

abstract public class CaseSensitivityCharSequencePredicateTemplateTestCase<P extends CaseSensitivityCharSequencePredicateTemplate<String>>
        extends PredicateTestCase<P, String> {
    private static final String CHARS = "#$%";

    CaseSensitivityCharSequencePredicateTemplateTestCase() {
        super();
    }

    @Test final public void testWithNullCharacterSequenceFails() {
        this.withFails(null);
    }

    @Test
    public void testWithEmptyCharacterSequenceFails() {
        this.withFails("");
    }

    private void withFails(final String chars) {
        try {
            this.createPredicate(chars, CaseSensitivity.SENSITIVE);
            Assert.fail();
        } catch (final RuntimeException expected) {
        }
    }

    @Test final public void testToStringCaseSensitive() {
        assertEquals(this.prefix() + CharSequences.quoteAndEscape(
                CaseSensitivityCharSequencePredicateTemplateTestCase.CHARS),
                this.createPredicate().toString());
    }

    @Test final public void testToStringCaseInsensitive() {
        assertEquals(this.prefix() + CharSequences.quoteAndEscape(
                CaseSensitivityCharSequencePredicateTemplateTestCase.CHARS) + " (CaseInsensitive)",
                this.createPredicateCaseInsensitivity(
                        CaseSensitivityCharSequencePredicateTemplateTestCase.CHARS).toString());
    }

    @Override final protected P createPredicate() {
        return this.createPredicateCaseSensitivity(
                CaseSensitivityCharSequencePredicateTemplateTestCase.CHARS);
    }

    abstract P createPredicate(final String chars, final CaseSensitivity sensitivity);

    abstract String prefix();

    // Sensitive

    final void testTrueCaseSensitive(final String must, final String chars) {
        this.testTrue(this.createPredicateCaseSensitivity(must), chars);
    }

    final void testFalseCaseSensitive(final String must, final String chars) {
        this.testFalse(this.createPredicateCaseSensitivity(must), chars);
    }

    final P createPredicateCaseSensitivity(final String chars) {
        return this.createPredicate(chars, CaseSensitivity.SENSITIVE);
    }

    // Insensitive

    final void testTrueCaseInsensitive(final String must, final String chars) {
        this.testTrue(this.createPredicateCaseInsensitivity(must), chars);
    }

    final void testFalseCaseInsensitive(final String must, final String chars) {
        this.testFalse(this.createPredicateCaseInsensitivity(must), chars);
    }

    final P createPredicateCaseInsensitivity(final String chars) {
        return this.createPredicate(chars, CaseSensitivity.INSENSITIVE);
    }
}
