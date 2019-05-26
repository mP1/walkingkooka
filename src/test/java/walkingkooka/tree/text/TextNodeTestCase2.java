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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.test.IsMethodTesting;
import walkingkooka.text.HasTextLengthTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.tree.HasTextOffsetTesting;
import walkingkooka.tree.Node;
import walkingkooka.tree.NodeTesting;
import walkingkooka.type.MemberVisibility;

import java.util.function.Predicate;

public abstract class TextNodeTestCase2<N extends TextNode> extends TextNodeTestCase<TextNode>
        implements NodeTesting<TextNode, TextNodeName, TextPropertyName<?>, Object>,
        HasTextLengthTesting,
        HasTextOffsetTesting,
        HasTextTesting,
        IsMethodTesting<N> {

    TextNodeTestCase2() {
        super();
    }

    // HasTextOffset .....................................................................................................

    @Test
    public final void testTextOffset() {
        this.textOffsetAndCheck(this.createTextNode(), 0);
    }

    // helpers .........................................................................................................

    @Override
    public final TextNode createNode() {
        return this.createTextNode();
    }

    abstract N createTextNode();

    // ClassTesting.....................................................................................................

    @Override
    public final Class<TextNode> type() {
        return Cast.to(this.textNodeType());
    }

    abstract Class<N> textNodeType();

    @Override
    public final MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public final String typeNamePrefix() {
        return "Text";
    }

    // IsMethodTesting...................................................................................................

    @Override
    public N createIsMethodObject() {
        return this.createTextNode();
    }

    @Override
    public String isMethodTypeNamePrefix() {
        return "Text";
    }

    @Override
    public String isMethodTypeNameSuffix() {
        return Node.class.getSimpleName();
    }

    @Override
    public Predicate<String> isMethodIgnoreMethodFilter() {
        return (n) -> n.equals("isText") || n.equals("isRoot");
    }
}