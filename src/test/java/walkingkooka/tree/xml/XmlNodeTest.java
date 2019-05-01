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

import walkingkooka.test.ClassTesting2;
import walkingkooka.test.TypeNameTesting;
import walkingkooka.tree.Node;
import walkingkooka.type.MemberVisibility;

public final class XmlNodeTest implements ClassTesting2<XmlNode>, TypeNameTesting<XmlNode> {

    @Override
    public Class<XmlNode> type() {
        return XmlNode.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "Xml";
    }

    @Override
    public String typeNameSuffix() {
        return Node.class.getSimpleName();
    }
}