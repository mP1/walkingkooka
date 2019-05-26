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

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.text.HasText;

import java.util.Objects;

/**
 * Holds plain text which may be empty.
 */
public final class Text extends TextLeafNode<String> implements HasText {

    public final static TextNodeName NAME = TextNodeName.with("Text");

    static Text with(final String value) {
        Objects.requireNonNull(value, "value");
        return new Text(NO_INDEX, value);
    }

    private Text(final int index, final String text) {
        super(index, text);
    }

    @Override
    public TextNodeName name() {
        return NAME;
    }

    @Override
    public String text() {
        return this.value();
    }

    @Override
    public Text removeParent() {
        return this.removeParent0().cast();
    }

    @Override
    Text replace1(final int index, final String value) {
        return new Text(index, value);
    }

    @Override
    public boolean isText() {
        return true;
    }

    // Object .........................................................................................................

    @Override
    boolean canBeEqual(final Object other) {
        return other instanceof Text;
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    void buildToString0(final ToStringBuilder b) {
        b.disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE);
        b.value(this.value);
    }
}