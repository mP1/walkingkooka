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

public abstract class ContentDispositionFileNameEqualityTestCase<F extends ContentDispositionFileName> extends HashCodeEqualsDefinedEqualityTestCase<F> {

    final static String FILENAME = "filename123";

    ContentDispositionFileNameEqualityTestCase() {
        super();
    }

    @Test
    public void testDifferentCase() {
        this.checkNotEquals(this.createFileName(FILENAME.toUpperCase()));
    }

    @Test
    public void testDifferent() {
        this.checkNotEquals(this.createFileName("different"));
    }

    @Override
    protected final F createObject() {
        return this.createFileName(FILENAME);
    }

    abstract F createFileName(final String name);
}
