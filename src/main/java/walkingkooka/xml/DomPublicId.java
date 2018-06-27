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

import walkingkooka.Value;
import walkingkooka.test.HashCodeEqualsDefined;
import walkingkooka.text.CharSequences;

import java.util.Optional;

/**
 * A {@link Value} which is a public id
 */
final public class DomPublicId implements Value<String>, HashCodeEqualsDefined {

  /**
   * Constant that may be used when no public id is present.
   */
  public final static Optional<DomPublicId> NO_PUBLIC_ID = Optional.empty();

  /**
   * Factory that creates a {@link DomPublicId}
   */
  static Optional<DomPublicId> with(final String value) {
    return null == value ? NO_PUBLIC_ID : Optional.of(new DomPublicId(value));
  }

  /**
   * package private constructor.
   */
  private DomPublicId(final String value) {
    this.value = value;
  }

  // value

  @Override
  public String value() {
    return this.value;
  }

  private final String value;

  // Object

  @Override
  public int hashCode() {
    return this.value.hashCode();
  }

  @Override
  public boolean equals(final Object other) {
    return (this == other) || ((other instanceof DomPublicId) && this.equals0((DomPublicId) other));
  }

  private boolean equals0(final DomPublicId other) {
    return this.value.equals(other.value);
  }

  @Override
  public String toString() {
    return CharSequences.quote(this.value).toString();
  }
}
