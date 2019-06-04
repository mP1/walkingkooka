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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.predicate.Predicates;
import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

public abstract class LengthTestCase<L extends Length> implements ClassTesting2<L>,
        HashCodeEqualsDefinedTesting<L>,
        HasJsonNodeTesting<L>,
        IsMethodTesting<L>,
        ParseStringTesting<L>,
        ToStringTesting<L>,
        TypeNameTesting<L> {

    LengthTestCase() {
        super();
    }

    @Test
    public final void testFromJsonBooleanFails() {
        this.fromJsonNodeFails(JsonNode.booleanNode(true));
    }

    @Test
    public final void testFromJsonNullFails() {
        this.fromJsonNodeFails(JsonNode.nullNode());
    }

    @Test
    public final void testFromJsonNumberFails() {
        this.fromJsonNodeFails(JsonNode.number(11));
    }

    @Test
    public final void testFromJsonArrayFails() {
        this.fromJsonNodeFails(JsonNode.array());
    }

    @Test
    public final void testFromJsonObjectFails() {
        this.fromJsonNodeFails(JsonNode.object());
    }

    abstract L createLength();

    // ClassTesting.....................................................................................................

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public final L createObject() {
        return this.createLength();
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public final L createHasJsonNode() {
        return this.createLength();
    }

    // IsMethodTesting..................................................................................................

    @Override
    public final L createIsMethodObject() {
        return this.createLength();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return Length.class.getSimpleName();
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return Predicates.never();
    }

    // ParseStringTesting...............................................................................................

    @Override
    public final Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    @Override
    public final RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Length.class.getSimpleName();
    }
}
