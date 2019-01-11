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

package walkingkooka.net.http.server.hateos;

import org.junit.Test;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import static org.junit.Assert.assertEquals;

public final class HateosHandlerBuilderRouterKeyTest extends ClassTestCase<HateosHandlerBuilderRouterKey> implements ComparableTesting<HateosHandlerBuilderRouterKey> {

    @Test
    public void testDifferentResourceName() {
        this.checkNotEquals(HateosHandlerBuilderRouterKey.with(HateosResourceName.with("different"), this.linkRelation()));
    }

    @Test
    public void testDifferentLinkRelation() {
        this.checkNotEquals(HateosHandlerBuilderRouterKey.with(this.resourceName(), LinkRelation.with("different")));
    }

    @Test
    public void testToString() {
        assertEquals("resource1 self", this.createComparable().toString());
    }

    @Override
    public HateosHandlerBuilderRouterKey createComparable() {
        return HateosHandlerBuilderRouterKey.with(this.resourceName(), this.linkRelation());
    }

    private HateosResourceName resourceName() {
        return HateosResourceName.with("resource1");
    }

    private LinkRelation<?> linkRelation() {
        return LinkRelation.SELF;
    }

    @Override
    protected Class<HateosHandlerBuilderRouterKey> type() {
        return HateosHandlerBuilderRouterKey.class;
    }

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
