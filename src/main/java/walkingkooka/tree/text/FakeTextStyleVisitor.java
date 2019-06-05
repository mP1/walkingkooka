/*
 * Copyright 2018 Miroslav Pokorny (final github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (final the "License");
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

import walkingkooka.color.Color;
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
    protected void visitBorderCollapse(final BorderCollapse borderCollapse) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitBorderSpacing(final BorderSpacing borderSpacing) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitDirection(final Direction direction) {
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
    protected void visitHeight(final Height height) {
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
    protected void visitLineHeight(final LineHeight lineHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginBottom(final MarginBottom marginBottom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginLeft(final MarginLeft marginLeft) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginRight(final MarginRight marginRight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitMarginTop(final MarginTop marginTop) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected void visitOpacity(final Opacity opacity) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingBottom(final PaddingBottom paddingBottom) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingLeft(final PaddingLeft paddingLeft) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingRight(final PaddingRight paddingRight) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitPaddingTop(final PaddingTop paddingTop) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitTabSize(final TabSize tabSize) {
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
    protected void visitWhitespace(final TextWhitespace whitespace) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void visitWidth(final Width width) {
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
