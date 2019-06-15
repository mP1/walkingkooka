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

import walkingkooka.color.Color;
import walkingkooka.color.ColorHslOrHsv;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.tree.visit.Visitor;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * A {@link Visitor} which dispatches each {@link TextStylePropertyName} to a visit method which accepts the accompanying
 * value.
 */
public abstract class TextStyleVisitor extends Visitor<TextStyle> {

    protected TextStyleVisitor() {
    }

    // Visitor..........................................................................................................

    @Override
    public final void accept(final TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle");

        if (Visiting.CONTINUE == this.startVisit(textStyle)) {
            textStyle.accept(this);
        }
        this.endVisit(textStyle);
    }

    // TextStyle........................................................................................................

    protected Visiting startVisit(final TextStyle textStyle) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final TextStyle textStyle) {
        // nop
    }

    // entries..........................................................................................................

    final void acceptPropertyAndValue(final Entry<TextStylePropertyName<?>, Object> entry) {
        final TextStylePropertyName<?> propertyName = entry.getKey();
        final Object value = entry.getValue();

        if (Visiting.CONTINUE == this.startVisit(propertyName, value)) {
            propertyName.accept(value, this);
        }
        this.endVisit(propertyName, value);
    }

    protected Visiting startVisit(final TextStylePropertyName<?> property, final Object value) {
        return Visiting.CONTINUE;
    }

    protected void endVisit(final TextStylePropertyName<?> property, final Object value) {
    }

    // properties..........................................................................................................

    protected void visitBackgroundColor(final Color color) {
        // nop
    }

    protected void visitBorderBottomColor(final ColorHslOrHsv color) {
        // nop
    }

    protected void visitBorderBottomStyle(final BorderStyle borderStyle) {
        // nop
    }

    protected void visitBorderBottomWidth(final Length<?> length) {
        // nop
    }

    protected void visitBorderCollapse(final BorderCollapse borderCollapse) {
        // nop
    }

    protected void visitBorderLeftColor(final ColorHslOrHsv color) {
        // nop
    }

    protected void visitBorderLeftStyle(final BorderStyle borderStyle) {
        // nop
    }

    protected void visitBorderLeftWidth(final Length<?> length) {
        // nop
    }

    protected void visitBorderRightColor(final ColorHslOrHsv color) {
        // nop
    }

    protected void visitBorderRightStyle(final BorderStyle borderStyle) {
        // nop
    }

    protected void visitBorderRightWidth(final Length<?> length) {
        // nop
    }

    protected void visitBorderSpacing(final BorderSpacing borderSpacing) {
        // nop
    }

    protected void visitBorderTopColor(final ColorHslOrHsv color) {
        // nop
    }

    protected void visitBorderTopStyle(final BorderStyle borderStyle) {
        // nop
    }

    protected void visitBorderTopWidth(final Length<?> length) {
        // nop
    }

    protected void visitFontFamilyName(final FontFamilyName fontFamilyName) {
        // nop
    }

    protected void visitFontKerning(final FontKerning fontKerning) {
        // nop
    }

    protected void visitFontSize(final FontSize fontSize) {
        // nop
    }

    protected void visitFontStretch(final FontStretch fontStretch) {
        // nop
    }

    protected void visitFontStyle(final FontStyle fontStyle) {
        // nop
    }

    protected void visitFontVariant(final FontVariant fontVariant) {
        // nop
    }

    protected void visitFontWeight(final FontWeight fontWeight) {
        // nop
    }

    protected void visitHangingPunctuation(final HangingPunctuation hangingPunctuation) {
        // nop
    }

    protected void visitHeight(final Length<?> height) {
        // nop
    }

    protected void visitHorizontalAlignment(final HorizontalAlignment horizontalAlignment) {
        // nop
    }

    protected void visitHyphens(final Hyphens hyphens) {
        // nop
    }

    protected void visitLetterSpacing(final LetterSpacing letterSpacing) {
        // nop
    }

    protected void visitLineHeight(final Length<?> lineHeight) {
        // nop
    }

    protected void visitListStylePosition(final ListStylePosition position) {
        // nop
    }

    protected void visitListStyleType(final ListStyleType type) {
        // nop
    }

    protected void visitMarginBottom(final Length<?> marginBottom) {
        // nop
    }

    protected void visitMarginLeft(final Length<?> marginLeft) {
        // nop
    }

    protected void visitMarginRight(final Length<?> marginRight) {
        // nop
    }

    protected void visitMarginTop(final Length<?> marginTop) {
        // nop
    }

    protected void visitMaxHeight(final Length<?> maxHeight) {
        // nop
    }

    protected void visitMaxWidth(final Length<?> maxWidth) {
        // nop
    }

    protected void visitMinHeight(final Length<?> minHeight) {
        // nop
    }

    protected void visitMinWidth(final Length<?> minWidth) {
        // nop
    }
    
    protected void visitOpacity(final Opacity opacity) {
        // nop
    }

    protected void visitOutlineColor(final Color color) {
        // nop
    }

    protected void visitOutlineOffset(final Length<?> length) {
        // nop
    }

    protected void visitOutlineStyle(final OutlineStyle outlineStyle) {
        // nop
    }

    protected void visitOutlineWidth(final Length<?> length) {
        // nop
    }

    protected void visitOverflowX(final Overflow overflow) {
        // nop
    }

    protected void visitOverflowY(final Overflow overflow) {
        // nop
    }

    protected void visitPaddingBottom(final Length<?> paddingBottom) {
        // nop
    }

    protected void visitPaddingLeft(final Length<?> paddingLeft) {
        // nop
    }

    protected void visitPaddingRight(final Length<?> paddingRight) {
        // nop
    }

    protected void visitPaddingTop(final Length<?> paddingTop) {
        // nop
    }

    protected void visitTabSize(final Length<?> tabSize) {
        // nop
    }

    protected void visitText(final String text) {
        // nop
    }

    protected void visitTextAlignment(final TextAlignment textAlignment) {
        // nop
    }

    protected void visitTextColor(final Color textColor) {
        // nop
    }

    protected void visitTextDecoration(final TextDecoration textDecoration) {
        // nop
    }

    protected void visitTextDecorationColor(final Color textDecorationColor) {
        // nop
    }

    protected void visitTextDecorationStyle(final TextDecorationStyle textDecorationStyle) {
        // nop
    }

    protected void visitTextDirection(final TextDirection textDirection) {
        // nop
    }

    protected void visitTextIndent(final Length<?> length) {
        // nop
    }

    protected void visitTextJustify(final TextJustify textJustify) {
        // nop
    }

    protected void visitTextOverflow(final TextOverflow textOverflow) {
        // nop
    }

    protected void visitTextTransform(final TextTransform textTransform) {
        // nop
    }

    protected void visitTextWrapping(final TextWrapping textWrapping) {
        // nop
    }

    protected void visitVerticalAlignment(final VerticalAlignment verticalAlignment) {
        // nop
    }

    protected void visitVisibility(final Visibility visibility) {
        // nop
    }

    protected void visitWhitespace(final TextWhitespace whitespace) {
        // nop
    }

    protected void visitWidth(final Length<?> width) {
        // nop
    }

    protected void visitWordBreak(final WordBreak wordBreak) {
        // nop
    }

    protected void visitWordSpacing(final WordSpacing wordSpacing) {
        // nop
    }

    protected void visitWordWrap(final WordWrap wordWrap) {
        // nop
    }

    protected void visitWritingMode(final WritingMode writingMode) {
        // nop
    }

    // unknown..........................................................................................................

    protected void visitUnknown(final Object value) {
        // nop
    }
}
