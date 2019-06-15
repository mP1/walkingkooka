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

public class FakeTextStyleVisitor extends TextStyleVisitor {

    protected FakeTextStyleVisitor() {
        super();
    }

    @Override
    protected Visiting startVisit(final TextStyle textStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextStyle textStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected Visiting startVisit(final TextStylePropertyName<?> property, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void endVisit(final TextStylePropertyName<?> property, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBackgroundColor(final Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderBottomColor(final ColorHslOrHsv color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderBottomStyle(final BorderStyle borderStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderBottomWidth(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderCollapse(final BorderCollapse borderCollapse) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderLeftColor(final ColorHslOrHsv color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderLeftStyle(final BorderStyle borderStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderLeftWidth(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderRightColor(final ColorHslOrHsv color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderRightStyle(final BorderStyle borderStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderRightWidth(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderSpacing(final BorderSpacing borderSpacing) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderTopColor(final ColorHslOrHsv color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderTopStyle(final BorderStyle borderStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderTopWidth(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontFamilyName(final FontFamilyName fontFamilyName) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontKerning(final FontKerning fontKerning) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontSize(final FontSize fontSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontStretch(final FontStretch fontStretch) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontStyle(final FontStyle fontStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontVariant(final FontVariant fontVariant) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitFontWeight(final FontWeight fontWeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHangingPunctuation(final HangingPunctuation hangingPunctuation) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHorizontalAlignment(final HorizontalAlignment horizontalAlignment) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHeight(final Length<?> height) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitHyphens(final Hyphens hyphens) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitLetterSpacing(final LetterSpacing letterSpacing) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitLineHeight(final Length<?> lineHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitListStylePosition(final ListStylePosition position) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitListStyleType(final ListStyleType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginBottom(final Length<?> marginBottom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginLeft(final Length<?> marginLeft) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginRight(final Length<?> marginRight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginTop(final Length<?> marginTop) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMaxHeight(final Length<?> maxHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMaxWidth(final Length<?> maxWidth) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMinHeight(final Length<?> minHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMinWidth(final Length<?> minWidth) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void visitOpacity(final Opacity opacity) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOutlineColor(final Color color) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOutlineOffset(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOutlineStyle(final OutlineStyle outlineStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOutlineWidth(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOverflowX(final Overflow overflow) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitOverflowY(final Overflow overflow) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingBottom(final Length<?> paddingBottom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingLeft(final Length<?> paddingLeft) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingRight(final Length<?> paddingRight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingTop(final Length<?> paddingTop) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTabSize(final Length<?> tabSize) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitText(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextAlignment(final TextAlignment textAlignment) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextColor(final Color textColor) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextDecoration(final TextDecoration textDecoration) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextDecorationColor(final Color textDecorationColor) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextDecorationStyle(final TextDecorationStyle textDecorationStyle) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextDirection(final TextDirection textDirection) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextIndent(final Length<?> length) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextJustify(final TextJustify textJustify) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextOverflow(final TextOverflow textOverflow) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextTransform(final TextTransform textTransform) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTextWrapping(final TextWrapping textWrapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitVerticalAlignment(final VerticalAlignment verticalAlignment) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitVisibility(final Visibility visibility) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWhitespace(final TextWhitespace whitespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWidth(final Length<?> width) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWordBreak(final WordBreak wordBreak) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWordSpacing(final WordSpacing wordSpacing) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWordWrap(final WordWrap wordWrap) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWritingMode(final WritingMode writingMode) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitUnknown(final Object value) {
        throw new UnsupportedOperationException();
    }
}
