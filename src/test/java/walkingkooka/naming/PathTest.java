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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;

public class PathTest implements ClassTesting<Path<?, ?>> {

    @Test
    public void testNamesListWithRoot() {
        this.namesListAndCheck(
            Paths.string("/"),
            StringName.ROOT
        );
    }

    @Test
    public void testNamesList() {
        this.namesListAndCheck(
            Paths.string("/path1/path2/path3"),
            StringName.ROOT,
            Names.string("path1"),
            Names.string("path2"),
            Names.string("path3")
        );
    }

    private void namesListAndCheck(final StringPath path,
                                   final StringName... names) {
        this.namesListAndCheck(
            path,
            Lists.of(names)
        );
    }

    private void namesListAndCheck(final StringPath path,
                                   final List<StringName> names) {
        this.checkEquals(
            names,
            path.namesList()
        );
    }

    // class............................................................................................................

    @Override
    public Class<Path<?, ?>> type() {
        return Cast.to(Path.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
