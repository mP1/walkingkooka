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

import walkingkooka.test.ClassTesting2;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.test.HashCodeEqualsDefinedTesting;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.JavaVisibility;

import java.util.function.Predicate;

public abstract class CookieDeletionTestCase<D extends CookieDeletion & HashCodeEqualsDefined>
        implements ClassTesting2<D>,
        HashCodeEqualsDefinedTesting<D>,
        IsMethodTesting<D>,
        ToStringTesting<D> {

    CookieDeletionTestCase() {
        super();
    }

    abstract D createDeletion();

    @Override
    public final D createObject() {
        return this.createDeletion();
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // IsMethodTesting.................................................................................................

    @Override
    public final D createIsMethodObject() {
        return this.createDeletion();
    }

    @Override
    public final String isMethodTypeNamePrefix() {
        return "Cookie";
    }

    @Override
    public final String isMethodTypeNameSuffix() {
        return "";
    }

    @Override
    public final Predicate<String> isMethodIgnoreMethodFilter() {
        return (m) -> false;
    }
}
