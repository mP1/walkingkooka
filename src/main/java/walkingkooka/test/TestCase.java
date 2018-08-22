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

package walkingkooka.test;

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

/**
 * Base class that includes many useful overloaded assert messages.
 */
abstract public class TestCase {

    protected TestCase() {
        super();
    }

    protected void checkToStringOverridden(final Class<?> type) {
        if (false == Fake.class.isAssignableFrom(type)) {
            this.checkToStringOverridden0(type);
        }
    }

    private void checkToStringOverridden0(final Class<?> type) {
        boolean notOverridden = true;

        try {
            final Method method = type.getMethod("toString");
            if (method.getDeclaringClass() != Object.class) {
                notOverridden = false;
            }
        } catch (final NoSuchMethodException cause) {
        }

        if (notOverridden) {
            Assert.fail(type.getName() + " did not override Object.toString");
        }
    }

    protected byte[] resourceAsBytes(final Class<?> source, final String resource) throws IOException {
        try(final ByteArrayOutputStream out = new ByteArrayOutputStream()){
            final byte[] buffer = new byte[4096];
            try(final InputStream in = source.getResourceAsStream(resource)){
                assertNotNull("Resource " + source.getName() + "/" + resource + " not found", in);
                for(;;){
                    final int count = in.read(buffer);
                    if(-1 == count){
                        break;
                    }
                    out.write(buffer, 0, count);
                }
            }
            out.flush();
            return out.toByteArray();
        }
    }

    protected Reader resourceAsReader(final Class<?> source, final String resource) throws IOException {
        return new StringReader(this.resourceAsText(source, resource));
    }

    protected String resourceAsText(final Class<?> source, final String resource) throws IOException {
        return new String(this.resourceAsBytes(source, resource));
    }
}
