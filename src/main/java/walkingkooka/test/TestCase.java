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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            Assertions.fail(type.getName() + " did not override Object.toString");
        }
    }

    protected byte[] resourceAsBytes(final Class<?> source, final String resource) throws IOException {
        try(final ByteArrayOutputStream out = new ByteArrayOutputStream()){
            final byte[] buffer = new byte[4096];
            try(final InputStream in = source.getResourceAsStream(resource)){
                assertNotNull(in, () -> "Resource " + source.getName() + "/" + resource + " not found");
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

    /**
     * Returns the name of the currently executing test.
     */
    protected String currentTestName() {
        String testName = null;

        for(StackTraceElement stackElement : Thread.currentThread().getStackTrace() ){
            final String className = stackElement.getClassName();
            final String methodName = stackElement.getMethodName();
            try {
                final Class<?> klass = Class.forName(className);
                final Optional<Method> possibleMethod = Arrays.stream(klass.getMethods())
                        .filter(m -> m.getName().equals(methodName) && m.getParameterTypes().length == 0 && m.getReturnType() == Void.TYPE)
                        .findFirst();
                if(possibleMethod.isPresent()) {
                    final Method method = possibleMethod.get();
                    if(method.isAnnotationPresent(Test.class)){
                        testName = method.getName();
                        break;
                    }
                }
            } catch (final Exception cause) {
                throw new Error(cause);
            }
        }

        if(null == testName) {
            throw new Error("Unable to determine test name");
        }

        return testName;
    }
}
