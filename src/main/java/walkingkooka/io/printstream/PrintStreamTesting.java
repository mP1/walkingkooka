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

package walkingkooka.io.printstream;

import walkingkooka.test.ToStringTesting;
import walkingkooka.test.TypeNameTesting;

import java.io.PrintStream;

/**
 * Interface with default methods which can be mixed in to assist testing of an {@link PrintStream}.
 */
public interface PrintStreamTesting<P extends PrintStream> extends ToStringTesting<P>,
        TypeNameTesting<P> {

    P createPrintStream();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return PrintStream.class.getSimpleName();
    }
}
