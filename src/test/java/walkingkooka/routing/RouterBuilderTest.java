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

package walkingkooka.routing;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTesting;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class RouterBuilderTest implements ClassTesting2<RouterBuilder<StringName, String>>,
        BuilderTesting<RouterBuilder<StringName, String>, Router<StringName, String>> {

    @Test
    public void testAddNullRouteFails() {
        assertThrows(NullPointerException.class, () -> {
            this.createBuilder().add(null);
        });
    }

    @Test
    public void testEmptyBuilderFails() {
        assertThrows(BuilderException.class, () -> {
            this.createBuilder().build();
        });
    }

    @Test
    public void testToString() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, "one")
                .andValueEquals(Names.string("path-0"), "dir-1")
                .andValueEquals(Names.string("path-1"), "file-2.txt");

        final RouterBuilder<StringName, String> builder = RouterBuilder.<StringName, String>empty()
                .add(routing1);
        this.toStringAndCheck(builder,
                "path-0=\"dir-1\" & path-1=\"file-2.txt\" ->one");
    }

    @Override
    public RouterBuilder<StringName, String> createBuilder() {
        return RouterBuilder.empty();
    }

    @Override
    public Class<RouterBuilder<StringName, String>> type() {
        return Cast.to(RouterBuilder.class);
    }

    @Override
    public Class<Router<StringName, String>> builderProductType() {
        return Cast.to(Router.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
