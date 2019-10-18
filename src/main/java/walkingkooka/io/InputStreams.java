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

package walkingkooka.io;

import walkingkooka.reflect.PublicStaticHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Extension methods for {@link InputStream}, required until JDK upgraded to 1.9
 */
public final class InputStreams implements PublicStaticHelper {

    public final static int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Extension method that reads all remaining bytes into a byte[]
     */
    public static byte[] readAllBytes(final InputStream input) throws IOException {
        Objects.requireNonNull(input, "input");

        try (final ByteArrayOutputStream bytes = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];

            for (; ; ) {
                final int read = input.read(buffer);
                if (-1 == read) {
                    break;
                }

                bytes.write(buffer, 0, read);
            }

            bytes.flush();
            return bytes.toByteArray();
        }
    }

    /**
     * Stop creation
     */
    private InputStreams() {
        throw new UnsupportedOperationException();
    }
}
