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

package walkingkooka.naming;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.iterator.IteratorTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Iterator;

public final class PathIteratorTest implements IteratorTesting, ClassTesting2<PathIterator<StringPath, StringName>>, ToStringTesting<PathIterator<StringPath, StringName>> {

    @Test
    public void testRoot() {
        this.iterateAndCheck(Paths.string("/").iterator(),
            StringName.ROOT);
    }

    @Test
    public void testNonRoot() {
        this.iterateAndCheck(Paths.string("/a1").iterator(),
            StringName.ROOT,
            Names.string("a1"));
    }

    @Test
    public void testNonRoot2() {
        this.iterateAndCheck(Paths.string("/a1/b2/c3").iterator(),
            StringName.ROOT,
            Names.string("a1"),
            Names.string("b2"),
            Names.string("c3"));
    }

    @Test
    public void testNonRootUsingHasNext() {
        this.iterateUsingHasNextAndCheck(Paths.string("/a1/b2/c3").iterator(),
            StringName.ROOT,
            Names.string("a1"),
            Names.string("b2"),
            Names.string("c3"));
    }

    @Test
    public void testRemoveFails() {
        this.removeUnsupportedFails(Paths.string("/a1/b2/c3").iterator());
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(Paths.string("/a1/b2").iterator(), "/a1/b2");
    }

    @Test
    public void testToString2() {
        final Iterator<StringName> iterator = Paths.string("/a1").iterator();
        iterator.next();

        this.toStringAndCheck(iterator, "a1");
    }

    @Test
    public void testToStringConsumed() {
        final Iterator<?> iterator = Paths.string("/a1").iterator();
        iterator.next(); // root
        iterator.next(); // a1

        this.toStringAndCheck(iterator, "");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<PathIterator<StringPath, StringName>> type() {
        return Cast.to(PathIterator.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
