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

package walkingkooka.tree.xml;

import org.junit.Test;
import walkingkooka.naming.NameTestCase;

public final class XmlAttributeNameTest extends NameTestCase<XmlAttributeName> {

    @Test(expected = NullPointerException.class)
    public void testWithNoPrefixNullFails() {
        XmlAttributeName.with("a", null);
    }

    @Override
    protected XmlAttributeName createName(final String name) {
        return XmlAttributeName.with(name, XmlAttributeName.NO_PREFIX);
    }

    @Override
    protected Class<XmlAttributeName> type() {
        return XmlAttributeName.class;
    }
}
