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
import walkingkooka.collect.map.Maps;
import walkingkooka.naming.Name;
import walkingkooka.naming.Names;
import walkingkooka.naming.StringName;
import walkingkooka.net.UrlPathName;
import walkingkooka.test.PublicClassTestCase;

import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public final class RouteMasterTest extends PublicClassTestCase<RouteMaster> {

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

        final Route<String> route = Route.with(ONE)
                .equalsValue(PATH_0, file1);

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, file1);
        parameters.put(Names.string("different"), "different-value");

        this.routeAndCheck(master, parameters, ONE);
    }

    @Test
    public void testOneRouteOneParameterMismatch() {
        final Route<String> route = Route.with(ONE)
                .equalsValue(PATH_0, this.file1());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.file2());
        parameters.put(PATH_1, this.differentFile());

        this.routeFails(master, parameters);
    }

    @Test
    public void testTwoDifferentRoutesNothingShared() {
        final UrlPathName file1 = this.file1();

        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, file1);

        final UrlPathName file2 = this.file2();

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, file2);

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();

        this.routeAndCheck(master, Maps.one(PATH_0, file1), ONE);
        this.routeFails(master, Maps.one(PATH_0, this.differentFile()));
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, dir)
                .equalsValue(PATH_1, file1);

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, dir)
                .equalsValue(PATH_1, this.file2());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(master, parameters);
    }

    @Test
    public void testTwoDifferentRoutesShareOneCondition2() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, dir)
                .equalsValue(PATH_1, file1);

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_1, this.file2())
                .equalsValue(PATH_0, dir);

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(master, parameters);
    }

    @Test
    public void testDifferentTopLevel() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, dir)
                .equalsValue(PATH_1, file1);

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, this.file2());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_1, this.differentFile());
        this.routeFails(master, parameters);
    }

    @Test
    public void testDifferentTopLevel2() {
        final UrlPathName file1 = this.file1();
        final UrlPathName dir = this.dir1();

        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, dir)
                .equalsValue(PATH_1, file1);

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, file1);

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, dir);
        parameters.put(PATH_1, file1);

        this.routeAndCheck(master, parameters, ONE);
    }

    //ONE=/dir-1/file-1
    //TWO=/dir-1/dir-2/file-2
    //THREE=/dir1/-dir-2/file-3
    @Test
    public void testMultipleRoutes() {
        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.file1());

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, this.file2());

        final Route<String> route3 = Route.with(THREE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.dir3())
                .equalsValue(PATH_2, this.file3());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .add(route3)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(master, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(master, parameters, THREE);
    }

    //ONE=/dir-1/file-1
    //TWO=/dir-1/dir-2/file-2
    //THREE=/dir-1/dir-2/file-3
    @Test
    public void testMultipleRoutes2() {
        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.file1());

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, this.file2());

        final Route<String> route3 = Route.with(THREE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.dir3())
                .equalsValue(PATH_2, this.file3());

        final Route<String> route4 = Route.with(FOUR)
                .equalsValue(PATH_0, this.dir4())
                .equalsValue(PATH_1, this.dir5())
                .equalsValue(PATH_2, this.file3());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .add(route3)
                .add(route4)
                .build();

        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(master, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(master, parameters, THREE);

        parameters.put(PATH_0, this.dir4());
        parameters.put(PATH_1, this.dir5());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(master, parameters, FOUR);
    }

    @Test
    public void testBuildOrderUnimportant() {
        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.file1());

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, this.file2());

        final Route<String> route3 = Route.with(THREE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.dir3())
                .equalsValue(PATH_2, this.file3());

        final Route<String> route4 = Route.with(FOUR)
                .equalsValue(PATH_0, this.dir4())
                .equalsValue(PATH_1, this.dir5())
                .equalsValue(PATH_2, this.file3());

        this.buildOrderUnimportantCheck(RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .add(route3)
                .add(route4)
                .build());

        this.buildOrderUnimportantCheck(RouteMasterBuilder.create()
                .add(route4)
                .add(route3)
                .add(route2)
                .add(route1)
                .build());
    }

    private void buildOrderUnimportantCheck(final RouteMaster<String> master) {
        final Map<Name, Object> parameters = Maps.ordered();
        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.file1());

        this.routeAndCheck(master, parameters, ONE);

        parameters.put(PATH_0, this.dir2());
        parameters.put(PATH_1, this.file2());
        this.routeAndCheck(master, parameters, TWO);

        parameters.put(PATH_0, this.dir1());
        parameters.put(PATH_1, this.dir3());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(master, parameters, THREE);

        parameters.put(PATH_0, this.dir4());
        parameters.put(PATH_1, this.dir5());
        parameters.put(PATH_2, this.file3());
        this.routeAndCheck(master, parameters, FOUR);
    }

    @Test
    public void testToString() {
        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.file1());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .build();
        assertEquals("\"path-0\"=dir-1-abc & \"path-1\"=file-1-one.txt ->target-1", master.toString());
    }

    @Test
    public void testToString2() {
        final Route<String> route1 = Route.with(ONE)
                .equalsValue(PATH_0, this.dir1())
                .equalsValue(PATH_1, this.file1());

        final Route<String> route2 = Route.with(TWO)
                .equalsValue(PATH_0, this.dir2())
                .equalsValue(PATH_1, this.file2());

        final RouteMaster<String> master = RouteMasterBuilder.create()
                .add(route1)
                .add(route2)
                .build();
        assertEquals("(\"path-0\"=dir-1-abc & \"path-1\"=file-1-one.txt ->target-1) | (\"path-0\"=dir-2-def & \"path-1\"=file-2-two.txt ->target-2)", master.toString());
    }

    private void routeAndCheck(final RouteMaster<String> master, final Map<Name, Object> parameters, final String target) {
        assertEquals("Routing of parameters=" + parameters + " failed", Optional.of(target), master.route(parameters));
    }

    private void routeFails(final RouteMaster<String> master, final Map<Name, Object> parameters) {
        assertEquals("Routing of parameters=" + parameters + " should have failed", Optional.empty(), master.route(parameters));
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
    protected Class<RouteMaster> type() {
        return RouteMaster.class;
    }
}
