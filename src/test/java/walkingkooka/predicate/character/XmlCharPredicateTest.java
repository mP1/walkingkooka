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

package walkingkooka.predicate.character;

import org.junit.Test;
import walkingkooka.test.SerializationTesting;

import static org.junit.Assert.assertEquals;

final public class XmlCharPredicateTest extends CharPredicateTestCase<XmlCharPredicate>
        implements SerializationTesting<XmlCharPredicate> {

    @Test
    public void testNul() {
        this.testFalse('\0');
    }

    @Test
    public void testEscape() {
        this.testFalse('\u001e');
    }

    @Test
    public void testLessThan0x20() {
        for (char c = 0x0; c < 0x20; c++) {
            switch (c) {
                case '\t':
                case '\r':
                case '\n':
                    break;
                default:
                    this.testFalse(c);
            }
        }
    }

    @Test
    public void testDxxx() {
        for (char c = 0xd800; c < 0xdfff; c++) {
            this.testFalse(c);
        }
    }

    @Test
    public void testBofFFFE() {
        this.testFalse('\uFFFE');
    }

    @Test
    public void testBofFFFF() {
        this.testFalse('\uFFFF');
    }

    @Test
    public void testTab() {
        this.testTrue('\t');
    }

    @Test
    public void testNewline() {
        this.testTrue('\n');
    }

    @Test
    public void testCarriageReturn() {
        this.testTrue('\r');
    }

    @Test
    public void testSpace() {
        this.testTrue(' ');
    }

    @Test
    public void testPuncutation() {
        this.testTrue(',');
    }

    @Test
    public void testDigit() {
        this.testTrue('0');
    }

    @Test
    public void testLowerCaseLetter() {
        this.testTrue('a');
    }

    @Test
    public void testUpperCaseLetter() {
        this.testTrue('A');
    }

    @Test
    public void testToString() {
        assertEquals("XML", XmlCharPredicate.INSTANCE.toString());
    }

    @Override
    protected XmlCharPredicate createCharacterPredicate() {
        return XmlCharPredicate.INSTANCE;
    }

    @Override
    public Class<XmlCharPredicate> type() {
        return XmlCharPredicate.class;
    }

    @Override
    public XmlCharPredicate serializableInstance() {
        return XmlCharPredicate.INSTANCE;
    }

    @Override
    public boolean serializableInstanceIsSingleton() {
        return true;
    }
}
