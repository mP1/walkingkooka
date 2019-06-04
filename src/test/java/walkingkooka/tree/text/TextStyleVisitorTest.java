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
import walkingkooka.collect.map.Maps;
import walkingkooka.color.Color;
import walkingkooka.tree.visit.Visiting;
import walkingkooka.type.MemberVisibility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public final class TextStyleVisitorTest implements TextStyleVisitorTesting<TextStyleVisitor> {

    @Override
    public void testCheckToStringOverridden() {
    }

    @Override
    public void testClassVisibility() {
    }

    @Override
    public void testStartVisitMethodsSingleParameter() {
    }

    @Override
    public void testEndVisitMethodsSingleParameter() {
    }

    @Test
    public void testVisitTextStyleSkip() {
        final TextStyle textStyle = TextStyle.EMPTY;

        new FakeTextStyleVisitor() {
            @Override
            protected Visiting startVisit(final TextStyle t) {
                assertSame(textStyle, t);
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final TextStyle t) {
                assertSame(textStyle, t);
            }
        }.accept(textStyle);
    }

    @Test
    public void testVisitTextStylePropertyNameSkip() {
        final TextStylePropertyName<Color> propertyName = TextStylePropertyName.TEXT_COLOR;
        final Color value = Color.BLACK;
        final TextStyle textStyle = textStyle(propertyName, value);

        new FakeTextStyleVisitor() {
            @Override
            protected Visiting startVisit(final TextStyle t) {
                return Visiting.CONTINUE;
            }

            @Override
            protected void endVisit(final TextStyle t) {
            }

            @Override
            protected Visiting startVisit(final TextStylePropertyName<?> p, final Object v) {
                assertSame(propertyName, p, "propertyName");
                assertSame(value, v, "value");
                return Visiting.SKIP;
            }

            @Override
            protected void endVisit(final TextStylePropertyName<?> p, final Object v) {
                assertSame(propertyName, p, "propertyName");
                assertSame(value, v, "value");
            }
        }.accept(textStyle);
    }

    @Test
    public void testVisitBackgroundColor() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitBackgroundColor(final Color c) {
                this.visited = c;
            }
        }.accept(TextStylePropertyName.BACKGROUND_COLOR, Color.BLACK);
    }

    @Test
    public void testVisitDirection() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitDirection(final Direction d) {
                this.visited = d;
            }
        }.accept(TextStylePropertyName.DIRECTION, Direction.LTR);
    }

    @Test
    public void testVisitFontFamilyName() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontFamilyName(final FontFamilyName f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_FAMILY_NAME, FontFamilyName.with("Times"));
    }

    @Test
    public void testVisitFontKerning() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontKerning(final FontKerning f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_KERNING, FontKerning.NORMAL);
    }

    @Test
    public void testVisitFontSize() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontSize(final FontSize f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_SIZE, FontSize.with(1));
    }

    @Test
    public void testVisitFontStretch() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontStretch(final FontStretch f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_STRETCH, FontStretch.CONDENSED);
    }
    
    @Test
    public void testVisitFontStyle() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontStyle(final FontStyle f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_STYLE, FontStyle.ITALIC);
    }

    @Test
    public void testVisitFontVariant() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontVariant(final FontVariant f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_VARIANT, FontVariant.SMALL_CAPS);
    }

    @Test
    public void testVisitFontWeight() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitFontWeight(final FontWeight f) {
                this.visited = f;
            }
        }.accept(TextStylePropertyName.FONT_WEIGHT, FontWeight.BOLD);
    }
    
    @Test
    public void testVisitHangingPunctuation() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitHangingPunctuation(final HangingPunctuation h) {
                this.visited = h;
            }
        }.accept(TextStylePropertyName.HANGING_PUNCTUATION, HangingPunctuation.LAST);
    }

    @Test
    public void testVisitHeight() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitHeight(final Height h) {
                this.visited = h;
            }
        }.accept(TextStylePropertyName.HEIGHT, Height.parse("1px"));
    }

    @Test
    public void testVisitHorizontalAlignment() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitHorizontalAlignment(final HorizontalAlignment h) {
                this.visited = h;
            }
        }.accept(TextStylePropertyName.HORIZONTAL_ALIGNMENT, HorizontalAlignment.LEFT);
    }
    
    @Test
    public void testVisitHyphens() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitHyphens(final Hyphens h) {
                this.visited = h;
            }
        }.accept(TextStylePropertyName.HYPHENS, Hyphens.AUTO);
    }

    @Test
    public void testVisitLetterSpacing() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitLetterSpacing(final LetterSpacing l) {
                this.visited = l;
            }
        }.accept(TextStylePropertyName.LETTER_SPACING, LetterSpacing.parse("1px"));
    }

    @Test
    public void testVisitOpacity() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitOpacity(final Opacity o) {
                this.visited = o;
            }
        }.accept(TextStylePropertyName.OPACITY, Opacity.OPAQUE);
    }

    @Test
    public void testVisitTabSize() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTabSize(final TabSize t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TAB_SIZE, TabSize.parse("1"));
    }

    @Test
    public void testVisitTextAlignment() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextAlignment(final TextAlignment h) {
                this.visited = h;
            }
        }.accept(TextStylePropertyName.TEXT_ALIGNMENT, TextAlignment.RIGHT);
    }
    
    @Test
    public void testVisitTextColor() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextColor(final Color c) {
                this.visited = c;
            }
        }.accept(TextStylePropertyName.TEXT_COLOR, Color.BLACK);
    }

    @Test
    public void testVisitTextDecoration() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextDecoration(final TextDecoration t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_DECORATION, TextDecoration.UNDERLINE);
    }

    @Test
    public void testVisitTextDecorationColor() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextDecorationColor(final Color t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_DECORATION_COLOR, Color.fromRgb(0x123));
    }

    @Test
    public void testVisitTextDecorationStyle() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextDecorationStyle(final TextDecorationStyle t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_DECORATION_STYLE, TextDecorationStyle.DASHED);
    }

    @Test
    public void testVisitTextJustify() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextJustify(final TextJustify t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_JUSTIFY, TextJustify.INTER_CHARACTER);
    }
    
    @Test
    public void testVisitTextOverflow() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextOverflow(final TextOverflow t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_OVERFLOW, TextOverflow.ELLIPSIS);
    }

    @Test
    public void testVisitTextTransform() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextTransform(final TextTransform t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_TRANSFORM, TextTransform.CAPITALIZE);
    }

    @Test
    public void testVisitTextWrapping() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitTextWrapping(final TextWrapping t) {
                this.visited = t;
            }
        }.accept(TextStylePropertyName.TEXT_WRAPPING, TextWrapping.OVERFLOW);
    }

    @Test
    public void testVisitVerticalAlignment() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitVerticalAlignment(final VerticalAlignment v) {
                this.visited = v;
            }
        }.accept(TextStylePropertyName.VERTICAL_ALIGNMENT, VerticalAlignment.BOTTOM);
    }

    @Test
    public void testVisitWidth() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitWidth(final Width w) {
                this.visited = w;
            }
        }.accept(TextStylePropertyName.WIDTH, Width.parse("1px"));
    }

    @Test
    public void testVisitWordBreak() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitWordBreak(final WordBreak w) {
                this.visited = w;
            }
        }.accept(TextStylePropertyName.WORD_BREAK, WordBreak.BREAK_WORD);
    }

    @Test
    public void testVisitWordSpacing() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitWordSpacing(final WordSpacing w) {
                this.visited = w;
            }
        }.accept(TextStylePropertyName.WORD_SPACING, WordSpacing.parse("1px"));
    }

    @Test
    public void testVisitWordWrap() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitWordWrap(final WordWrap w) {
                this.visited = w;
            }
        }.accept(TextStylePropertyName.WORD_WRAP, WordWrap.BREAK_WORD);
    }

    @Test
    public void testVisitWritingMode() {
        new TestTextStyleVisitor() {
            @Override
            protected void visitWritingMode(final WritingMode w) {
                this.visited = w;
            }
        }.accept(TextStylePropertyName.WRITING_MODE, WritingMode.VERTICAL_LR);
    }

    private static <T> TextStyle textStyle(final TextStylePropertyName<T> propertyName, final T value) {
        return TextStyle.with(Maps.of(propertyName, value));
    }

    @Override
    public TextStyleVisitor createVisitor() {
        return new TestTextStyleVisitor();
    }

    static class TestTextStyleVisitor extends FakeTextStyleVisitor {

        <T> void accept(final TextStylePropertyName<T> propertyName, final T value) {
            this.expected = value;
            this.accept(textStyle(propertyName, value));
            assertEquals(this.expected, this.visited);
        }

        private Object expected;
        Object visited;

        @Override
        protected Visiting startVisit(final TextStyle textStyle) {
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final TextStyle textStyle) {
        }

        @Override
        protected Visiting startVisit(final TextStylePropertyName<?> property, final Object value) {
            return Visiting.CONTINUE;
        }

        @Override
        protected void endVisit(final TextStylePropertyName<?> property, final Object value) {
        }
    }

    @Override
    public MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public String typeNamePrefix() {
        return "";
    }

    @Override
    public Class<TextStyleVisitor> type() {
        return TextStyleVisitor.class;
    }
}
