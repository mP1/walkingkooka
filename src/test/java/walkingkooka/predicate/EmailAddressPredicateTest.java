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

package walkingkooka.predicate;

import org.junit.jupiter.api.Test;

public final class EmailAddressPredicateTest extends PredicateTestCase<EmailAddressPredicate, String> {

    @Test
    public void testValid() {
        this.testTrue("user@example.com");
    }

    @Test
    public void testIncomplete() {
        this.testFalse("user@");
    }

    @Test
    public void testInvalid() {
        this.testFalse("123");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), "EmailAddress");
    }

    @Override
    protected EmailAddressPredicate createPredicate() {
        return EmailAddressPredicate.INSTANCE;
    }

    @Override
    public Class<EmailAddressPredicate> type() {
        return EmailAddressPredicate.class;
    }
}
