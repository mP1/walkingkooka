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

package walkingkooka.tree.text;

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.type.JavaVisibility;

public final class TextNodeVisitorTest implements TextNodeVisitorTesting<TextNodeVisitor> {

    @Test
    public void testAcceptProperties() {
        this.createVisitor()
                .accept(TextNode.style(Lists.of(TextNode.text("abc123"))));
    }

    @Test
    public void testAcceptStyleName() {
        this.createVisitor()
                .accept(TextNode.styleName(TextStyleName.with("styled123"))
                        .setChildren(Lists.of(TextNode.text("abc123"))));
    }

    @Test
    public void testAcceptText() {
        this.createVisitor().accept(TextNode.text("abc123"));
    }

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public TextNodeVisitor createVisitor() {
        return new TextNodeVisitor() {

            @Override
            public String toString() {
                return TextNodeVisitorTest.class.getSimpleName();
            }
        };
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public Class<TextNodeVisitor> type() {
        return TextNodeVisitor.class;
    }
}
