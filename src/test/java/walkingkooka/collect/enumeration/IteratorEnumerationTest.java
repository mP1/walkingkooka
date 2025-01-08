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

package walkingkooka.collect.enumeration;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;
import java.util.List;

public final class IteratorEnumerationTest implements EnumerationTesting,
    ClassTesting<IteratorEnumeration<String>>,
    ToStringTesting<IteratorEnumeration<String>> {

    public void testConsume() {
        final List<String> values = Lists.of(
            "1a",
            "2b",
            "3c",
            "4d"
        );

        this.enumerateAndCheck(
            IteratorEnumeration.with(
                values.iterator()
            ),
            values.toArray(new String[0])
        );
    }

    public void testConsumeUsingHasMoreElements() {
        final List<String> values = Lists.of(
            "1a",
            "2b",
            "3c",
            "4d"
        );

        this.enumerateUsingHasMoreElementsAndCheck(
            IteratorEnumeration.with(
                values.iterator()
            ),
            values.toArray(new String[0])
        );
    }

    @Test
    public void testToString() {
        final Iterator<String> values = Lists.of(
            "1a",
            "2b",
            "3c",
            "4d"
        ).iterator();

        this.toStringAndCheck(
            IteratorEnumeration.with(values),
            values.toString()
        );
    }

    @Override
    public Class<IteratorEnumeration<String>> type() {
        return Cast.to(IteratorEnumeration.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
