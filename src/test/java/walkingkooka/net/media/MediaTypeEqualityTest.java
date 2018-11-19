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

package walkingkooka.net.media;

import org.junit.Test;
import walkingkooka.test.HashCodeEqualsDefinedEqualityTestCase;

final public class MediaTypeEqualityTest extends HashCodeEqualsDefinedEqualityTestCase<MediaType> {

    @Test
    public void testDifferentMimeType() {
        this.checkNotEquals(MediaType.parseOne("major/different"));
    }

    @Test
    public void testDifferentParameter() {
        this.checkNotEquals(MediaType.parseOne("major/minor;parameter=value"));
    }

    @Test
    public void testDifferentParameter2() {
        checkNotEquals(MediaType.parseOne("major/minor;parameter=value"),
                MediaType.parseOne("major/minor;different=value"));
    }

    @Test
    public void testDifferentParamterOrderStillEqual() {
        checkEqualsAndHashCode(MediaType.parseOne("a/b;x=1;y=2"),
                MediaType.parseOne("a/b;y=2;x=1"));
    }

    @Test
    public void testParsedAndBuild() {
        checkEquals(this.createObject(), MediaType.parseOne("major/minor"));
    }

    @Test
    public void testParameterOrderIrrelevant() {
        checkEquals(MediaType.parseOne("type/subtype;a=1;b=2;c=3"), MediaType.parseOne("type/subtype;c=3;b=2;a=1"));
    }

    @Test
    public void testDifferentWhitespaceSameParametersStillEqual() {
        checkEqualsAndHashCode(MediaType.parseOne("a/b;   x=1"), MediaType.parseOne("a/b;x=1"));
    }

    @Override
    protected MediaType createObject() {
        return MediaType.parseOne("major/minor");
    }
}
