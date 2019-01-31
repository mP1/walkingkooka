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

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.PredicateTestCase;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.SerializationTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract public class CaseSensitivityCharSequencePredicateTemplateTestCase<P extends CaseSensitivityCharSequencePredicateTemplate<String>>
        extends PredicateTestCase<P, String>
        implements HashCodeEqualsDefinedTesting<P>, SerializationTesting<P> {

    private static final String STRING = "#$%";
    private final static CaseSensitivity SENSITIVITY = CaseSensitivity.SENSITIVE;

    CaseSensitivityCharSequencePredicateTemplateTestCase() {
        super();
    }

    @Test
    final public void testWithNullCharacterSequenceFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createPredicate(null, SENSITIVITY);
        });
    }

    @Test
    public void testWithEmptyCharacterSequenceFails() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.createPredicate("", SENSITIVITY);
        });
    }

    @Test
    final public void testToStringCaseSensitive() {
        assertEquals(this.prefix() + CharSequences.quoteAndEscape(
                STRING),
                this.createPredicate().toString());
    }

    @Test
    final public void testToStringCaseInsensitive() {
        assertEquals(this.prefix() + CharSequences.quoteAndEscape(
                STRING) + " (CaseInsensitive)",
                this.createPredicateCaseInsensitivity(
                        STRING).toString());
    }

    @Override
    final protected P createPredicate() {
        return this.createPredicateCaseSensitivity(
                STRING);
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
        return this.createPredicate(chars, SENSITIVITY);
    }

    // Insensitive

    final void testTrueCaseInsensitive(final String must, final String chars) {
        this.testTrue(this.createPredicateCaseInsensitivity(must), chars);
    }

    final void testFalseCaseInsensitive(final String must, final String chars) {
        this.testFalse(this.createPredicateCaseInsensitivity(must), chars);
    }

    final P createPredicateCaseInsensitivity(final String chars) {
        return this.createPredicate(chars, SENSITIVITY.invert());
    }

    @Test
    public void testEqualsDifferentCharSequence() {
        this.checkNotEquals(this.createPredicate("different", SENSITIVITY));
    }

    @Test
    public void testEqualsDifferentCaseSensitivity() {
        this.checkNotEquals(this.createPredicate(STRING, SENSITIVITY.invert()));
    }

    @Override 
    final public P createObject() {
        return this.createPredicate(STRING, SENSITIVITY);
    }

    @Override
    public final P serializableInstance() {
        return this.createPredicate();
    }

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }
}
