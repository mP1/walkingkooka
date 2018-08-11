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

package walkingkooka.net;

import walkingkooka.text.CharSequences;

import static org.junit.Assert.assertEquals;

/**
 * Collection of helpers used by various host address tests.
 */
final class HostAddressTesting {

  /**
   * Accepts some bytes in big endian form and builds a hex string representation.
   */
  static String toHexString(final byte[] bytes) {
    final StringBuilder builder = new StringBuilder();
    for (final byte value : bytes) {
      builder.append(CharSequences.padLeft(Integer.toHexString(0xff & value), 2, '0'));
    }
    return builder.toString();
  }

  /**
   * Parses the {@link String} of hex values assuming that it has hex digits in big endian form.
   */
  static byte[] toByteArray(final String hexDigits) {
    assertEquals("hexValues string has wrong number of characters=" + hexDigits,
        HostAddress.IP6_OCTET_COUNT * 2,
        hexDigits.length());
    return CharSequences.bigEndianHexDigits(hexDigits);
  }

  /**
   * Stop creation
   */
  private HostAddressTesting() {
    throw new UnsupportedOperationException();
  }
}
