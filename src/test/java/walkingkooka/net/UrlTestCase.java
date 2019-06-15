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

package walkingkooka.net;

import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.test.SerializationTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.json.HasJsonNodeTesting;
import walkingkooka.type.JavaVisibility;

import java.util.function.Predicate;

/**
 * Base class for testing a {@link Url} with mostly parameter checking tests.
 */
abstract public class UrlTestCase<U extends Url> implements ClassTesting2<U>,
        HashCodeEqualsDefinedTesting<U>,
        HasJsonNodeTesting<U>,
        IsMethodTesting<U>,
        ParseStringTesting<U>,
        SerializationTesting<U>,
        ToStringTesting<U>,
        TypeNameTesting<U> {

    UrlTestCase() {
        super();
    }

    // factory

    abstract U createUrl();

    // ClassTesting....................................................................................................

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // HashCodeEqualsDefinedTesting.....................................................................................

    @Override
    public final U createObject() {
        return this.createUrl();
    }

    // HasJsonNodeTesting...............................................................................................

    @Override
    public final U createHasJsonNode() {
        return this.createUrl();
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final U createIsMethodObject() {
        return this.createUrl();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return "Url";
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }

    // ParseStringTesting ..............................................................................................

    @Override
    public final RuntimeException parseFailedExpected(final RuntimeException expected) {
        return expected;
    }

    @Override
    public final Class<? extends RuntimeException> parseFailedExpected(final Class<? extends RuntimeException> expected) {
        return expected;
    }

    // SerializationTesting.............................................................................................

    @Override
    public final boolean serializableInstanceIsSingleton() {
        return false;
    }

    // TypeNameTesting .................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "";
    }

    @Override
    public final String typeNameSuffix() {
        return Url.class.getSimpleName();
    }
}
