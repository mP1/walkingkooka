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

package walkingkooka.collect.map;

import walkingkooka.test.ClassTesting2;
import walkingkooka.type.MemberVisibility;

public abstract class ImmutableMapTestCase<T> implements ClassTesting2<T> {

    ImmutableMapTestCase() {
        super();
    }

    final static String KEY1 = "a1";
    final static Integer VALUE1 = 111;

    final static String KEY2 = "b1";
    final static Integer VALUE2 = 222;

    // ClassTesting.....................................................................................................

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
