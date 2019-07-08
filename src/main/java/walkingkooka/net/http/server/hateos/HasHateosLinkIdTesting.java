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

package walkingkooka.net.http.server.hateos;

import walkingkooka.test.ClassTesting2;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Mixin interface for testing {@link HasHateosLinkId}
 */
public interface HasHateosLinkIdTesting<H extends HasHateosLinkId> extends ClassTesting2<H> {

    H createHasHateosLinkId();

    default void hateosLinkIdAndCheck(final String expected) {
        this.hateosLinkIdAndCheck(this.createHasHateosLinkId(),
                expected);
    }

    default void hateosLinkIdAndCheck(final HasHateosLinkId has, final String expected) {
        assertEquals(expected,
                has.hateosLinkId(),
                () -> has + " hateosLinkId");
    }
}
