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
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Enumeration;

import static org.junit.jupiter.api.Assertions.assertThrows;

final public class ArrayEnumerationTest implements ClassTesting2<ArrayEnumeration<String>>,
        EnumerationTesting<ArrayEnumeration<String>, String> {

    @Test
    public void testWithNullArrayFails() {
        assertThrows(NullPointerException.class, () -> {
            ArrayEnumeration.with(null);
        });
    }

    @Test
    public void testConsumedUsingHasNextNext() {
        this.enumerateUsingHasMoreElementsAndCheck(this.createEnumeration(), "A", "B", "C");
    }

    @Test
    public void testConsumedUsingNext() {
        this.enumerateAndCheck(this.createEnumeration(), "A", "B", "C");
    }

    @Test
    public void testArrayCopied() {
        final String[] items = new String[]{"A", "B", "C"};
        final Enumeration<String> enumerator = ArrayEnumeration.with(items);
        items[0] = "D";
        items[1] = "E";
        items[2] = "F";

        this.enumerateUsingHasMoreElementsAndCheck(enumerator, "A", "B", "C");
    }

    // ToString....................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(this.createEnumeration(), Lists.of("A", "B", "C").toString());
    }

    @Test
    public void testToStringPartiallyConsumed() {
        final ArrayEnumeration<String> enumerator = this.createEnumeration();
        enumerator.nextElement();

        this.toStringAndCheck(enumerator, Lists.of("B", "C").toString());
    }

    @Test
    public void testToStringWhenEmpty() {
        final ArrayEnumeration<String> enumerator = this.createEnumeration();
        enumerator.nextElement();
        enumerator.nextElement();
        enumerator.nextElement();

        this.toStringAndCheck(enumerator, Lists.of().toString());
    }

    @Override
    public ArrayEnumeration<String> createEnumeration() {
        return ArrayEnumeration.with(new Object[]{"A", "B", "C"});
    }

    @Override
    public Class<ArrayEnumeration<String>> type() {
        return Cast.to(ArrayEnumeration.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
