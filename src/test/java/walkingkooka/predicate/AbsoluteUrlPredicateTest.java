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

public final class AbsoluteUrlPredicateTest extends PredicateTestCase<AbsoluteUrlPredicate, String> {

    @Test
    public void testHttpAbsoluteUrl() {
        this.testTrue("http://example.com");
    }

    @Test
    public void testHttpAbsoluteUrl2() {
        this.testTrue("http://example.com/path123?query456");
    }

    @Test
    public void testHttpsAbsoluteUrl() {
        this.testTrue("https://example.com");
    }

    @Test
    public void testIncomplete() {
        this.testFalse("http://");
    }

    @Test
    public void testRelativeUrl() {
        this.testFalse("/path?query123=456");
    }

    @Test
    public void testInvalid() {
        this.testFalse("123");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createPredicate(), "AbsoluteUrl");
    }

    @Override
    protected AbsoluteUrlPredicate createPredicate() {
        return AbsoluteUrlPredicate.INSTANCE;
    }

    @Override
    public Class<AbsoluteUrlPredicate> type() {
        return AbsoluteUrlPredicate.class;
    }
}
