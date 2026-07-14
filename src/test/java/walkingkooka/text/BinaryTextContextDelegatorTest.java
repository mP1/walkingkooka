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

package walkingkooka.text;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.nio.charset.Charset;

public final class BinaryTextContextDelegatorTest implements BinaryTextContextTesting,
    ClassTesting<BinaryTextContextDelegator> {

    private final static Indentation INDENTATION = Indentation.SPACES2;

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    // BinaryTextContextDelegator.................................................................................

    @Test
    public void testCharset() {
        this.charsetAndCheck(
            new TestBinaryTextContextDelegator(),
            CHARSET
        );
    }
    
    @Test
    public void testIndentation() {
        this.indentationAndCheck(
            new TestBinaryTextContextDelegator(),
            INDENTATION
        );
    }

    @Test
    public void testLineEnding() {
        this.lineEndingAndCheck(
            new TestBinaryTextContextDelegator(),
            LINE_ENDING
        );
    }

    // class............................................................................................................

    @Override
    public Class<BinaryTextContextDelegator> type() {
        return BinaryTextContextDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    private final static class TestBinaryTextContextDelegator implements BinaryTextContextDelegator {

        @Override
        public BinaryTextContext binaryTextContext() {
            return new TestBinaryTextContext();
        }
    }

    private final static class TestBinaryTextContext implements BinaryTextContext {

        @Override
        public Charset charset() {
            return CHARSET;
        }

        @Override
        public Indentation indentation() {
            return INDENTATION;
        }

        @Override
        public LineEnding lineEnding() {
            return LINE_ENDING;
        }
    }
}
