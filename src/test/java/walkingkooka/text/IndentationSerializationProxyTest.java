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

package walkingkooka.text;

import org.junit.Test;
import walkingkooka.io.serialize.SerializationProxyTestCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

final public class IndentationSerializationProxyTest
        extends SerializationProxyTestCase<IndentationConstantSerializationProxy> {

    @Test
    public void testToString() {
        final Indentation indentation = Indentation.with('\t', 10);
        assertNotSame("indentation instance is a constant", indentation, Indentation.with('\t', 10));

        assertEquals(indentation.toString(), indentation.writeReplace().toString());
    }

    @Override
    protected Class<IndentationConstantSerializationProxy> type() {
        return IndentationConstantSerializationProxy.class;
    }
}
