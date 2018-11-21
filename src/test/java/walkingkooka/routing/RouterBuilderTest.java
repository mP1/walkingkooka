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
import walkingkooka.build.BuilderException;
import walkingkooka.build.BuilderTestCase;
import walkingkooka.naming.Names;

import static org.junit.Assert.assertEquals;

public final class RouterBuilderTest extends BuilderTestCase<RouterBuilder<Void>, Router<Void>> {

    @Test(expected = NullPointerException.class)
    public void testAddNullRouteFails() {
        this.createBuilder().add(null);
    }

    @Test(expected = BuilderException.class)
    public void testEmptyBuilderFails() {
        this.createBuilder().build();
    }

    @Test
    public void testToString() {
        final Routing<String> routing1 = Routing.with("one")
                .andValueEquals(Names.string("path-0"), "dir-1")
                .andValueEquals(Names.string("path-1"), "file-2.txt");

        final RouterBuilder<String> builder = RouterBuilder.<String>create()
                .add(routing1);
        assertEquals("\"path-0\"=\"dir-1\" & \"path-1\"=\"file-2.txt\" ->one", builder.toString());
    }

    @Override
    protected RouterBuilder<Void> createBuilder() {
        return RouterBuilder.create();
    }

    @Override
    protected Class<RouterBuilder<Void>> type() {
        return Cast.to(RouterBuilder.class);
    }

    @Override
    protected Class<Router<Void>> builderProductType() {
        return Cast.to(Router.class);
    }
}
