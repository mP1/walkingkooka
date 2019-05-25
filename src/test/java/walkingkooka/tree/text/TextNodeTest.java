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
import walkingkooka.collect.list.Lists;
import walkingkooka.test.ToStringTesting;
import walkingkooka.type.MemberVisibility;

public final class TextNodeTest extends TextNodeTestCase<TextNode> implements ToStringTesting<TextNode> {

    @Test
    public void testBuildAndCheckToString() {
        this.toStringAndCheck(TextNode.properties(Lists.of(
                TextNode.styled(TextStyleName.with("style123"))
                        .setChildren(Lists.of(TextNode.text("text123"))))),
                "[style123[\"text123\"]]");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<TextNode> type() {
        return TextNode.class;
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }

    // TypeNameTesting...................................................................................................

    @Override
    public String typeNamePrefix() {
        return "TextNode";
    }

    @Override
    public String typeNameSuffix() {
        return "";
    }
}
