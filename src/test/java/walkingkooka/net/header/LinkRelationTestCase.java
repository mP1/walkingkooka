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

package walkingkooka.net.header;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class LinkRelationTestCase<R extends LinkRelation<T>, T> extends HeaderValue2TestCase<R, T> {

    LinkRelationTestCase() {
        super();
    }

    @Test
    public final void testIsUrl() {
        final R relation = this.createLinkRelation();
        assertEquals(this.url(),
                relation.isUrl(),
                relation + " isUrl");
    }

    abstract boolean url();

    @Test
    public final void testIsWildcard() {
        this.isWildcardAndCheck(this.createLinkRelation(), false);
    }

    final R createLinkRelation() {
        return this.createHeaderValue();
    }

    abstract R createLinkRelation(final T value);

    @Override
    final R createHeaderValue(final T value) {
        return this.createLinkRelation(value);
    }

    @Override
    public final boolean isMultipart() {
        return false;
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    @Override
    public final boolean isResponse() {
        return true;
    }
}
