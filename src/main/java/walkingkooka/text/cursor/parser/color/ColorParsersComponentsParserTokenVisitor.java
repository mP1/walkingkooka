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

package walkingkooka.text.cursor.parser.color;

import walkingkooka.build.tostring.ToStringBuilder;
import walkingkooka.build.tostring.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.ParserTokenVisitor;

import java.util.List;

/**
 * Collects all double values from a {@link ParserToken}.
 */
final class ColorParsersComponentsParserTokenVisitor extends ParserTokenVisitor {

    static List<Float> transform(final ParserToken token) {
        final ColorParsersComponentsParserTokenVisitor visitor = new ColorParsersComponentsParserTokenVisitor();
        visitor.accept(token);
        return visitor.values;
    }

    ColorParsersComponentsParserTokenVisitor() {
        super();
    }

    @Override
    protected void visit(final DoubleParserToken token) {
        this.values.add(token.value().floatValue());
    }

    private final List<Float> values = Lists.array();


    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .label("values")
                .value(this.values)
                .build();
    }
}
