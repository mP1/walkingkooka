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

import org.junit.jupiter.api.Test;
import walkingkooka.compare.ComparableTesting;
import walkingkooka.net.header.LinkRelation;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class HateosHandlerRouterKeyTest implements ClassTesting2<HateosHandlerRouterKey>,
        ComparableTesting<HateosHandlerRouterKey>,
        ToStringTesting<HateosHandlerRouterKey> {

    @Test
    public void testDifferentResourceName() {
        this.checkNotEquals(HateosHandlerRouterKey.with(HateosResourceName.with("different"), this.linkRelation()));
    }

    @Test
    public void testDifferentLinkRelation() {
        this.checkNotEquals(HateosHandlerRouterKey.with(this.resourceName(), LinkRelation.with("different")));
    }

    @Test
    public void testToString() {
        assertEquals("resource1 self", this.createComparable().toString());
    }

    @Override
    public HateosHandlerRouterKey createComparable() {
        return HateosHandlerRouterKey.with(this.resourceName(), this.linkRelation());
    }

    private HateosResourceName resourceName() {
        return HateosResourceName.with("resource1");
    }

    private LinkRelation<?> linkRelation() {
        return LinkRelation.SELF;
    }

    @Override
    public Class<HateosHandlerRouterKey> type() {
        return HateosHandlerRouterKey.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
