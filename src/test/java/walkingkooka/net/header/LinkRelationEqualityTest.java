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

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

public final class LinkRelationEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<LinkRelation<?>> {

    @Test
    public void testDifferentText() {
        this.checkNotEquals(LinkRelation.with("different"));
    }

    @Test
    public void testUpperCaseText() {
        this.checkNotEquals(LinkRelation.with("ABC123"));
    }

    @Test
    public void testUrl() {
        this.checkNotEquals(LinkRelation.with("http://example.com"));
    }

    @Test
    public void testDifferentUrl() {
        this.checkNotEquals(LinkRelation.with("http://example.com"),
                LinkRelation.with("http://example2.com"));
    }

    @Override
    protected LinkRelation<?> createObject() {
        return LinkRelation.with("abc123");
    }
}
