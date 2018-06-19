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
 */

package walkingkooka.xml;

import walkingkooka.build.tostring.ToStringBuilder;

/**
 * A {@link DomNode} that holds a comment.
 */
final public class DomComment extends DomTextNode {

  public final static String OPEN = "<!--";
  public final static String CLOSE = "-->";
  final static String ILLEGAL_CONTENT = "--";

  DomComment(final org.w3c.dom.Node node) {
    super(node);
  }

  @Override
  public DomComment setText(final String text) {
    checkCommentText(text);
    return this.setText0(text).asComment();
  }

  // DomNode.......................................................................................................

  @Override
  DomNodeKind kind() {
    return DomNodeKind.COMMENT;
  }

  @Override
  DomComment wrap0(final org.w3c.dom.Node node) {
    return new DomComment(node);
  }

  @Override
  public DomComment asComment() {
    return this;
  }

  @Override
  public boolean isComment() {
    return true;
  }

  // Object...........................................................................................

  @Override boolean canBeEqual(final Object other) {
    return other instanceof DomComment;
  }

  // UsesToStringBuilder...........................................................................................

  /**
   * Returns the text without escaping as it is not required
   */
  @Override
  void buildDomNodeToString(final ToStringBuilder builder) {
    builder.surroundValues(DomComment.OPEN, DomComment.CLOSE);
    builder.value(new Object[] { this.text() });// array required so surroundValues works.
  }
}
