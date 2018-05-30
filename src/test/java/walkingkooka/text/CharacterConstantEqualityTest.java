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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class CharacterConstantEqualityTest
        extends HashCodeEqualsDefinedEqualityTestCase<CharacterConstant> {

    // constants

    private final static char CHAR = 'a';

    @Override
    @Test
    public void testEqualsObjectAndPossiblyType() {
        // CharacterConstantEquality.equals(char) overload is an exception
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(CharacterConstant.with((char) (CharacterConstantEqualityTest.CHAR
                + 1)));
    }

    @Test
    public void testSameCharacter() {
        Assert.assertTrue(this.createObject().equals(CharacterConstantEqualityTest.CHAR));
    }

    @Test
    public void testSameCharacter2() {
        Assert.assertTrue(this.createObject().equals((Object) CharacterConstantEqualityTest.CHAR));
    }

    @Test
    public void testSameString() {
        Assert.assertTrue(this.createObject().equals("" + CharacterConstantEqualityTest.CHAR));
    }

    @Test
    public void testSameString2() {
        Assert.assertTrue(this.createObject()
                .equals("" + CharacterConstantEqualityTest.CHAR));
    }

    @Override
    protected CharacterConstant createObject() {
        return CharacterConstant.with('a');
    }
}
