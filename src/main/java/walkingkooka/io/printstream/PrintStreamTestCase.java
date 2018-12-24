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

package walkingkooka.io.printstream;

import org.junit.Test;
import walkingkooka.test.ClassTestCase;
import walkingkooka.type.MemberVisibility;

import java.io.PrintStream;

/**
 * Base class for testing a {@link PrintStream}
 */
abstract public class PrintStreamTestCase<P extends PrintStream>
        extends ClassTestCase<P> {

    protected PrintStreamTestCase() {
        super();
    }

    @Test
    public void testNaming() {
        this.checkNaming(PrintStream.class);
    }

    @Test
    final public void testCheckToStringOverridden() {
        this.checkToStringOverridden(this.type());
    }

    abstract protected P createPrintStream();

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PACKAGE_PRIVATE;
    }
}
