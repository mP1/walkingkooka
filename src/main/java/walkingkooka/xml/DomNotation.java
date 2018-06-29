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

import org.w3c.dom.Node;
import walkingkooka.Cast;
import walkingkooka.build.tostring.ToStringBuilder;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link DomNode} that notation.
 *
 * <pre>
 * <!NOTATION nname PUBLIC std>
 * <!NOTATION nname SYSTEM url>
 * </pre>
 */
final public class DomNotation extends DomLeafNode implements HasDomPublicId, HasDomSystemId {

  private final static String START = "<!NOTATION ";
  private final static String END = ">";

  DomNotation(final org.w3c.dom.Node node) {
    super(node);
  }

  private org.w3c.dom.Notation notationNode() {
    return Cast.to(this.node);
  }

  // HasPublicId ........................................................................................

  @Override
  public Optional<DomPublicId> publicId() {
    if(null==this.publicId){
      this.publicId = DomPublicId.with(this.notationNode().getPublicId());
    }
    return this.publicId;
  }

  Optional<DomPublicId> publicId;

  // HasSystemId ........................................................................................

  @Override
  public Optional<DomSystemId> systemId() {
    if(null==this.systemId) {
      this.systemId= DomSystemId.with(this.notationNode().getSystemId());
    }
    return this.systemId;
  }

  Optional<DomSystemId> systemId;

  // DomNode .................................................................................................

  @Override
  DomNotation wrap0(final Node node) {
    return new DomNotation(node);
  }

  @Override
  DomNodeKind kind() {
    return DomNodeKind.NOTATION;
  }

  /**
   * Always returns this.
   */
  @Override
  public DomNotation asNotation() {
    return this;
  }

  @Override
  public boolean isNotation() {
    return true;
  }

  // Object ...................................................................................................

  @Override
  public int hashCode() {
     return Objects.hash(this.name(), this.publicId, this.systemId);
  }

  @Override
  boolean canBeEqual(final Object other) {
    return other instanceof DomNotation;
  }

  @Override
  boolean equalsIgnoringParentAndChildren(final DomNode other) {
    return equalsIgnoringParentAndChildren0(other.asNotation());
  }

  private boolean equalsIgnoringParentAndChildren0(final DomNotation other) {
    return this.name().equals(other.name()) &&
           this.publicId().equals(other.publicId()) &&
           this.systemId().equals(other.systemId());
  }

  // UsesToStringBuilder...........................................................................................

  /**
   * <pre>
   * <!NOTATION name PUBLIC "publicId" SYSTEM "systemId">
   * </pre>
   */
  @Override
  void buildDomNodeToString(final ToStringBuilder builder) {
    builder.append(DomNotation.START);
    builder.value(this.name);

    buildToString(this.publicId(),this.systemId(), builder);

    builder.append(DomNotation.END);
  }
}
