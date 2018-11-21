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

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.net.UrlPathName;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public final class RouterBuilderRouterTest extends RouterTestCase<RouterBuilderRouter<String>, String> {

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
        final UrlPathName file1 = this.file1();

        final Routing<String> route = Routing.with(ONE)
                .andValueEquals(PATH_0, file1);

        final Router<String> routers = RouterBuilder.<String>create()
                .add(route)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, file1);
        parameters.put(Names.string("different"), "different-value");

        this.routeAndCheck(routers, parameters, ONE);
    }

    @Test
    public void testOneRouteOneParameterMismatch() {
        final Routing<String> route = Routing.with(ONE)
                .andValueEquals(PATH_0, this.file1());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(route)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.file2());
        parameters.put(PATH_1, this.differentFile());

        this.routeFails(routers, parameters);
    }

    @Test
    public void testTwoDifferentRoutesNothingShared() {
        final UrlPathName file1 = this.file1();

        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, file1);

        final UrlPathName file2 = this.file2();

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, file2);

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();

        this.routeAndCheck(routers, Maps.one(PATH_0, file1), ONE);
        this.routeFails(routers, Maps.one(PATH_0, this.differentFile()));
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, this.file2());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition2() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_1, this.file2())
                .andValueEquals(PATH_0, dir);

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testDifferentTopLevel() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(routers, parameters);
    }

    @Test
    public void testDifferentTopLevel2() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, dir)
                .andValueEquals(PATH_1, file1);

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, file1);

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(routers, parameters, ONE);
    }

    //ONE=/dir-1/file-1
    //TWO=/dir-1/dir-2/file-2
    //THREE=/dir1/-dir-2/file-3
    @Test
    public void testMultipleRoutes() {
        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<String> routing3 = Routing.with(THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
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
        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<String> routing3 = Routing.with(THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Routing<String> routing4 = Routing.with(FOUR)
                .andValueEquals(PATH_0, this.dir4())
                .andValueEquals(PATH_1, this.dir5())
                .andValueEquals(PATH_2, this.file3());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .add(routing4)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
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
        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Routing<String> routing3 = Routing.with(THREE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.dir3())
                .andValueEquals(PATH_2, this.file3());

        final Routing<String> routing4 = Routing.with(FOUR)
                .andValueEquals(PATH_0, this.dir4())
                .andValueEquals(PATH_1, this.dir5())
                .andValueEquals(PATH_2, this.file3());

        this.buildOrderUnimportantCheck(RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .add(routing3)
                .add(routing4)
                .build());

        this.buildOrderUnimportantCheck(RouterBuilder.<String>create()
                .add(routing4)
                .add(routing3)
                .add(routing2)
                .add(routing1)
                .build());
    }

    private void buildOrderUnimportantCheck(final Router<String> routers) {
        final Map<Name, Object> parameters = Maps.ordered();
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
        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .build();
        assertEquals("\"path-0\"=dir-1-abc & \"path-1\"=file-1-one.txt ->target-1", routers.toString());
    }

    @Test
    public void testToString2() {
        final Routing<String> routing1 = Routing.with(ONE)
                .andValueEquals(PATH_0, this.dir1())
                .andValueEquals(PATH_1, this.file1());

        final Routing<String> routing2 = Routing.with(TWO)
                .andValueEquals(PATH_0, this.dir2())
                .andValueEquals(PATH_1, this.file2());

        final Router<String> routers = RouterBuilder.<String>create()
                .add(routing1)
                .add(routing2)
                .build();
        assertEquals("(\"path-0\"=dir-1-abc & \"path-1\"=file-1-one.txt ->target-1) | (\"path-0\"=dir-2-def & \"path-1\"=file-2-two.txt ->target-2)", routers.toString());
    }

    private UrlPathName dir1() {
        return UrlPathName.with("dir-1-abc");
    }

    private UrlPathName dir2() {
        return UrlPathName.with("dir-2-def");
    }

    private UrlPathName dir3() {
        return UrlPathName.with("dir-3-ghi");
    }

    private UrlPathName dir4() {
        return UrlPathName.with("dir-4-jkl");
    }

    private UrlPathName dir5() {
        return UrlPathName.with("dir-5-mno");
    }

    private UrlPathName file1() {
        return UrlPathName.with("file-1-one.txt");
    }

    private UrlPathName file2() {
        return UrlPathName.with("file-2-two.txt");
    }

    private UrlPathName file3() {
        return UrlPathName.with("file-3-three.txt");
    }

    private UrlPathName differentFile() {
        return UrlPathName.with("different-file.txt");
    }

    @Override
    protected RouterBuilderRouter<String> createRouter() {
        return RouterBuilderRouterNull.get();
    }

    @Override
    protected Class<RouterBuilderRouter<String>> type() {
        return Cast.to(RouterBuilderRouter.class);
    }
}
