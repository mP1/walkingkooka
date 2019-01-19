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

import walkingkooka.naming.NameTestCase;
import walkingkooka.text.CaseSensitivity;

public final class XmlNameTest extends NameTestCase<XmlName, XmlName> {

    @Override
    protected XmlName createName(final String name) {
        return XmlName.element(name);
    }

    @Override
    protected CaseSensitivity caseSensitivity() {
        return CaseSensitivity.SENSITIVE;
    }

    @Override
    protected String nameText() {
        return "element-22";
    }

    @Override
    protected String differentNameText() {
        return "different";
    }

    @Override
    protected String nameTextLess() {
        return "element-1";
    }

    @Override
    protected Class<XmlName> type() {
        return XmlName.class;
    }
}