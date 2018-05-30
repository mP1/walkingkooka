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

package walkingkooka.test;

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.type.PackagePrivateStaticHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

/**
 * Base class for testing a {@link PackagePrivateStaticHelper} with tests mostly concerned with
 * visibility of members.
 */
abstract public class PackagePrivateStaticHelperTestCase<H extends PackagePrivateStaticHelper>
        extends StaticHelperTestCase<H> {

    protected PackagePrivateStaticHelperTestCase() {
        super();
    }

    @Test final public void testClassIsPackagePrivate() {
        final Class<?> type = this.type();

        final int visibility = type.getModifiers();
        if (Modifier.isPublic(visibility) || Modifier.isProtected(visibility)) {
            Assert.fail(type.getName() + " is not package private=" + type);
        }
    }

    /**
     * Verifies that all static methods are public, package private or private.
     */
    @Test final public void testVisibilityOfAllStaticMethods() {
        final Class<H> type = this.getTypeAndCheck();

        final Set<String> invalid = Sets.sorted();

        for (final Method method : type.getDeclaredMethods()) {
            final int modifier = method.getModifiers();

            // skip if private
            if (Modifier.isPrivate(modifier)) {
                continue;
            }
            // if package private
            if ((false == Modifier.isPublic(modifier)) && (false == Modifier.isProtected(modifier))) {
                continue;
            }
            invalid.add(method.toGenericString());
        }

        if (false == invalid.isEmpty()) {
            Assert.fail("Not all static methods are be package private/private=" + invalid);
        }
    }

    @Override final boolean canHavePublicTypes(final Method method) {
        return true;
    }
}
