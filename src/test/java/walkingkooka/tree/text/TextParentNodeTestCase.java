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

import walkingkooka.tree.NodeTesting2;

public abstract class TextParentNodeTestCase<T extends TextParentNode> extends TextNodeTestCase2<T> implements NodeTesting2<TextNode, TextNodeName, TextPropertyName<?>, Object> {

    TextParentNodeTestCase() {
        super();
    }

    final Text text1() {
        return Text.with("text-1a");
    }

    final Text text2() {
        return Text.with("text-2b");
    }

    final Text text3() {
        return Text.with("text-3c");
    }

    final Text different() {
        return Text.with("text-different");
    }
}
