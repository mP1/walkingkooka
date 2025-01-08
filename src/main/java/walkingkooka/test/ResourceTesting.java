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

package walkingkooka.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

/**
 * Helpers to assist loading of files using the test.
 */
public interface ResourceTesting extends Testing {

    default byte[] resourceAsBytes(final Class<?> source, final String resource) throws IOException {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[4096];
            try (final InputStream in = source.getResourceAsStream(resource)) {
                this.checkNotEquals(
                    null,
                    in,
                    () -> "Resource " + source.getName() + "/" + resource + " not found"
                );
                for (; ; ) {
                    final int count = in.read(buffer);
                    if (-1 == count) {
                        break;
                    }
                    out.write(buffer, 0, count);
                }
            }
            out.flush();
            return out.toByteArray();
        }
    }

    default Reader resourceAsReader(final Class<?> source, final String resource) throws IOException {
        return new StringReader(this.resourceAsText(source, resource));
    }

    default String resourceAsText(final Class<?> source, final String resource) throws IOException {
        return new String(this.resourceAsBytes(source, resource));
    }
}
