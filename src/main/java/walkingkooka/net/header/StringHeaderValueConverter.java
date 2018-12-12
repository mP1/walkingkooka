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

import walkingkooka.collect.set.Sets;
import walkingkooka.predicate.character.CharPredicate;

import java.util.Objects;
import java.util.Set;

/**
 * A {@link HeaderValueConverter} that handles string values.
 */
abstract class StringHeaderValueConverter extends HeaderValueConverter2<String> {

    /**
     * Factory that creates a new {@link StringHeaderValueConverter}.
     */
    final static StringHeaderValueConverter with(final CharPredicate predicate,
                                                 final StringHeaderValueConverterFeature...features) {
        Objects.requireNonNull(predicate, "predicate");
        Objects.requireNonNull(features, "features");

        final Set<StringHeaderValueConverterFeature> featuresSet = Sets.of(features);
        return featuresSet.contains(StringHeaderValueConverterFeature.DOUBLE_QUOTES) ?
                QuotedStringHeaderValueConverter.quoted(predicate, featuresSet.contains(StringHeaderValueConverterFeature.BACKSLASH_ESCAPING)) :
                UnquotedStringHeaderValueConverter.unquoted(predicate);
    }

    /**
     * Package private to limit sub classing.
     */
    StringHeaderValueConverter(final CharPredicate predicate) {
        super();
        this.predicate = predicate;
    }

    final CharPredicate predicate;

    @Override
    public final String toString() {
        return this.predicate.toString();
    }
}
