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

package walkingkooka.tree.pojo;

import org.junit.Assert;

public abstract class PojoCollectionNodeTestCase<N extends PojoArrayOrCollectionNode, V> extends PojoArrayOrCollectionNodeTestCase<N, V> {

    final static String STRING0 = "a0";
    final static String STRING1 = "b1";
    final static String STRING2 = "c2";

    @Override
    final void checkValue(final V expected, final V actual) {
        Assert.assertEquals(expected, actual);
    }
}
