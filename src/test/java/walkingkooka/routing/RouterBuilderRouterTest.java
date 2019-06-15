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

package walkingkooka.routing;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.test.ClassTesting2;
import walkingkooka.type.JavaVisibility;

import java.util.Map;

public final class RouterBuilderRouterTest implements ClassTesting2<RouterBuilderRouter<StringName, String>>,
        RouterTesting<RouterBuilderRouter<StringName, String>, StringName, String> {

    private final static StringName PATH_0 = Names.string("path-0");
    private final static StringName PATH_1 = Names.string("path-1");
    private final static StringName PATH_2 = Names.string("path-2");

    private final static String ONE = "target-1";
    private final static String TWO = "target-2";
    private final static String THREE = "target-3";
    private final static String FOUR = "target-4";

    // Tests.............................................................................................................

    @Test
    public void testOneRouteOneParameter() {
        final StringName file1 = this.file1();

        final Routing<StringName, String> route = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, file1);

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(route)
                .build();

        this.routeAndCheck(routers,
                Maps.of(PATH_0, file1, Names.string("different"), "different-value"),
                ONE);
    }

    @Test
    public void testOneRouteOneParameterMismatch() {
        final Routing<StringName, String> route = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.file1());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(route)
                .build();

        this.routeFails(routers,
                Maps.of(PATH_0, this.file2(), PATH_1, this.differentFile()));
    }

    @Test
    public void testTwoDifferentRoutesNothingShared() {
        final StringName file1 = this.file1();

        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, file1);

        final StringName file2 = this.file2();

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, file2);

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();

        this.routeAndCheck(routers, Maps.of(PATH_0, file1), ONE);
        this.routeFails(routers, Maps.of(PATH_0, this.differentFile()));
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition() {
        final StringName file1 = this.file1();
        final StringName dir = this.dir1();

        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, this.file2());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition2() {
        final StringName file1 = this.file1();
        final StringName dir = this.dir1();

        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_1, this.file2())
                .andValueEquals(PATH_0, dir);

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testDifferentTopLevel() {
        final StringName file1 = this.file1();
        final StringName dir = this.dir1();

        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testDifferentTopLevel2() {
        final StringName file1 = this.file1();
        final StringName dir = this.dir1();

        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, file1);

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();

        this.routeAndCheck(routers,
                Maps.of(PATH_0, dir, PATH_1, file1),
                ONE);
    }

    //ONE=/dir-1/file-1
    //TWO=/dir-1/dir-2/file-2
    //THREE=/dir1/-dir-2/file-3
    @Test
    public void testMultipleRoutes() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<StringName, String> routing3 = Routing.with(StringName.class, THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .build();

        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(routers, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(routers, parameters, THREE);
    }

    //ONE=/dir-1/file-1
    //TWO=/dir-1/dir-2/file-2
    //THREE=/dir-1/dir-2/file-3
    @Test
    public void testMultipleRoutes2() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<StringName, String> routing3 = Routing.with(StringName.class, THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Routing<StringName, String> routing4 = Routing.with(StringName.class, FOUR)
                .andValueEquals(PATH_0, this.dir4())
                .andValueEquals(PATH_1, this.dir5())
                .andValueEquals(PATH_2, this.file3());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .add(routing4)
                .build();

        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(routers, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(routers, parameters, THREE);

        parameters.put(PATH_0, this.dir4());
        parameters.put(PATH_1, this.dir5());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(routers, parameters, FOUR);
    }

    @Test
    public void testBuildOrderUnimportant() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<StringName, String> routing3 = Routing.with(StringName.class, THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Routing<StringName, String> routing4 = Routing.with(StringName.class, FOUR)
                .andValueEquals(PATH_0, this.dir4())
                .andValueEquals(PATH_1, this.dir5())
                .andValueEquals(PATH_2, this.file3());

        this.buildOrderUnimportantCheck(RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .add(routing4)
                .build());

        this.buildOrderUnimportantCheck(RouterBuilder.<StringName, String>empty()
                .add(routing4)
                .add(routing3)
                .add(routing2)
                .add(routing1)
                .build());
    }

    private void buildOrderUnimportantCheck(final Router<StringName, String> routers) {
        final Map<StringName, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(routers, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(routers, parameters, THREE);

        parameters.put(PATH_0, this.dir4());
        parameters.put(PATH_1, this.dir5());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(routers, parameters, FOUR);
    }

    @Test
    public void testToString() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .build();
        this.toStringAndCheck(routers,
                "path-0=dir-1-abc & path-1=file-1-one.txt ->target-1");
    }

    @Test
    public void testToString2() {
        final Routing<StringName, String> routing1 = Routing.with(StringName.class, ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<StringName, String> routing2 = Routing.with(StringName.class, TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Router<StringName, String> routers = RouterBuilder.<StringName, String>empty()
                .add(routing1)
                .add(routing2)
                .build();
        this.toStringAndCheck(routers,
                "(path-0=dir-1-abc & path-1=file-1-one.txt ->target-1) | (path-0=dir-2-def & path-1=file-2-two.txt ->target-2)");
    }

    private StringName dir1() {
        return Names.string("dir-1-abc");
    }

    private StringName dir2() {
        return Names.string("dir-2-def");
    }

    private StringName dir3() {
        return Names.string("dir-3-ghi");
    }

    private StringName dir4() {
        return Names.string("dir-4-jkl");
    }

    private StringName dir5() {
        return Names.string("dir-5-mno");
    }

    private StringName file1() {
        return Names.string("file-1-one.txt");
    }

    private StringName file2() {
        return Names.string("file-2-two.txt");
    }

    private StringName file3() {
        return Names.string("file-3-three.txt");
    }

    private StringName differentFile() {
        return Names.string("different-file.txt");
    }

    @Override
    public RouterBuilderRouter<StringName, String> createRouter() {
        return RouterBuilderRouterNull.get();
    }

    @Override
    public Class<RouterBuilderRouter<StringName, String>> type() {
        return Cast.to(RouterBuilderRouter.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
