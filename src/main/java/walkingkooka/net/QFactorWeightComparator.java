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

package walkingkooka.net;

import walkingkooka.Cast;

import java.util.Comparator;

/**
 * A {@link Comparator} that may be used to compare or sort two {@link HasQFactorWeight} highest to lowest.
 */
final class QFactorWeightComparator<T extends HasQFactorWeight> implements Comparator<T> {

    /**
     * Type safe singleton getter
     */
    static <T extends HasQFactorWeight> QFactorWeightComparator<T> instance() {
        return Cast.to(INSTANCE);
    }

    @SuppressWarnings("rawtypes")
    private final static QFactorWeightComparator INSTANCE = new QFactorWeightComparator();

    /**
     * Private ctor
     */
    private QFactorWeightComparator() {
        super();
    }

    @Override
    public int compare(final T weight, final T weight2) {
        return -Float.compare(qFactor(weight), qFactor(weight2));
    }

    private float qFactor(final T weight) {
        return weight.qFactorWeightOrDefault();
    }

    @Override
    public String toString() {
        return "QFactor";
    }
}
