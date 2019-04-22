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

package walkingkooka.test;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Tag interface for all test interfaces that contain tests and related helpers as default/guard methods.
 */
public interface Testing {

    /**
     * Returns the name of the currently executing test.
     */
    default String currentTestName() {
        String testName = null;

        for (StackTraceElement stackElement : Thread.currentThread().getStackTrace()) {
            final String className = stackElement.getClassName();
            final String methodName = stackElement.getMethodName();
            try {
                final Class<?> klass = Class.forName(className);
                final Optional<Method> possibleMethod = Arrays.stream(klass.getMethods())
                        .filter(m -> m.getName().equals(methodName) && m.getParameterTypes().length == 0 && m.getReturnType() == Void.TYPE)
                        .findFirst();
                if (possibleMethod.isPresent()) {
                    final Method method = possibleMethod.get();
                    if (method.isAnnotationPresent(Test.class)) {
                        testName = method.getName();
                        break;
                    }
                }
            } catch (final Exception cause) {
                throw new Error(cause);
            }
        }

        if (null == testName) {
            throw new Error("Unable to determine test name");
        }

        return testName;
    }
}
