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

import walkingkooka.HasId;
import walkingkooka.compare.Range;
import walkingkooka.tree.json.HasJsonNode;
import walkingkooka.tree.xml.HasXmlNode;
import walkingkooka.tree.xml.XmlNode;

/**
 * Interface to be implemened by all values/entites that are registered with a {@link HateosResourceName}.
 */
public interface HateosResource<I> extends HasId<I>, HasJsonNode, HasXmlNode {

    default XmlNode toXmlNode() {
        throw new UnsupportedOperationException(); // #1092
    }

    /**
     * This character should be used to separate values within a {@link Range}.
     */
    char HATEOS_LINK_RANGE_SEPARATOR = '-';

    /**
     * Formats the {@link #id()} ready to appear within a hateos link. If a {@link Range} it should use the separator
     * character given. This also assumes that special characters are escaped as necessary.
     */
    String idForHateosLink();
}
