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

package walkingkooka.net.header;

import org.junit.Test;
import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.net.Url;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

final public class LinkParameterNameTest extends HeaderParameterNameTestCase<LinkParameterName<?>> {

    @Test(expected = IllegalArgumentException.class)
    public void testWithIncludesWhitespaceFails() {
        LinkParameterName.with("paramet er");
    }

    @Test
    public void testWith() {
        this.createNameAndCheck("abc123");
    }

    @Test
    public void testConstantNameReturnsConstant() {
        assertSame(LinkParameterName.REL,
                LinkParameterName.with(LinkParameterName.REL.value()));
    }

    @Test
    public void testConstantNameCaseInsensitiveReturnsConstant() {
        final String differentCase = LinkParameterName.REL.value().toUpperCase();
        assertNotEquals(differentCase, LinkParameterName.REL.value());
        assertSame(LinkParameterName.REL, LinkParameterName.with(differentCase));
    }

    // parameter value......................................................................................

    @Test
    public void testParameterValueAbsent() {
        this.parameterValueAndCheckAbsent(LinkParameterName.REL,
                this.link());
    }

    @Test
    public void testParameterValuePresent() {
        final LinkParameterName<List<LinkRelation<?>>> parameter = LinkParameterName.REL;
        final List<LinkRelation<?>> value = LinkRelation.parse("prev");

        this.parameterValueAndCheckPresent(parameter,
                this.link().setParameters(Maps.one(parameter, value)),
                value);
    }

    private Link link() {
        return Link.with(Url.parse("http://example.com"));
    }

    // toString...........................................................................

    @Test
    public void testToString() {
        final String text = "abc123";
        assertEquals(text, LinkParameterName.with(text).toString());
    }

    @Override
    protected LinkParameterName<Object> createName(final String name) {
        return Cast.to(LinkParameterName.with(name));
    }

    @Override
    protected String nameText() {
        return "abc123";
    }

    @Override
    protected Class<LinkParameterName<?>> type() {
        return Cast.to(LinkParameterName.class);
    }
}
