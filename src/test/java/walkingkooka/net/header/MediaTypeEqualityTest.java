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

package walkingkooka.net.header;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class MediaTypeEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<MediaType> {

    @Test
    public void testTypeDifferentCase() {
        this.checkEquals(MediaType.parse("MAJOR/minor"));
    }

    @Test
    public void testSubTypeDifferentCase() {
        this.checkEquals(MediaType.parse("major/MINOR"));
    }

    @Test
    public void testParameterNameDifferentCase() {
        this.checkEquals(MediaType.parse("type/subType; parameter123=value456"),
                MediaType.parse("type/subType; PARAMETER123=value456"));
    }

    @Test
    public void testParameterValueDifferentCase() {
        this.checkNotEquals(MediaType.parse("type/subType; parameter123=value456"),
                MediaType.parse("type/subType; parameter123=VALUE456"));
    }

    @Test
    public void testDifferentMimeType() {
        this.checkNotEquals(MediaType.parse("major/different"));
    }

    @Test
    public void testDifferentParameter() {
        this.checkNotEquals(MediaType.parse("major/minor;parameter=value"));
    }

    @Test
    public void testDifferentParameter2() {
        checkNotEquals(MediaType.parse("major/minor;parameter=value"),
                MediaType.parse("major/minor;different=value"));
    }

    @Test
    public void testDifferentParameterOrderStillEqual() {
        checkEqualsAndHashCode(MediaType.parse("a/b;x=1;y=2"),
                MediaType.parse("a/b;y=2;x=1"));
    }

    @Test
    public void testParsedAndBuild() {
        checkEquals(this.createObject(), MediaType.parse("major/minor"));
    }

    @Test
    public void testParameterOrderIrrelevant() {
        checkEquals(MediaType.parse("type/subtype;a=1;b=2;c=3"), MediaType.parse("type/subtype;c=3;b=2;a=1"));
    }

    @Test
    public void testDifferentWhitespaceSameParametersStillEqual() {
        checkEqualsAndHashCode(MediaType.parse("a/b;   x=1"), MediaType.parse("a/b;x=1"));
    }

    @Override
    protected MediaType createObject() {
        return MediaType.parse("major/minor");
    }
}
