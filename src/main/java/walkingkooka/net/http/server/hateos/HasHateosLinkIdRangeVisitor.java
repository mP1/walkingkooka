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

import walkingkooka.ToStringBuilder;
import walkingkooka.compare.Range;
import walkingkooka.compare.RangeVisitor;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link RangeVisitor} used by {@link HasHateosLinkId#rangeHateosLinkId(Range, Function)}
 */
final class HasHateosLinkIdRangeVisitor<I extends Comparable<I>> extends RangeVisitor<I> {

    static <I extends Comparable<I>> String hateosLinkId(final Range<I> range,
                                                         final Function<I, String> hateosLinkId) {
        Objects.requireNonNull(range, "range");
        Objects.requireNonNull(hateosLinkId, "hateosLinkId");

        final HasHateosLinkIdRangeVisitor<I> visitor = new HasHateosLinkIdRangeVisitor<>(hateosLinkId);
        visitor.accept(range);
        return visitor.linkText;
    }

    HasHateosLinkIdRangeVisitor(final Function<I, String> hateosLinkId) {
        super();
        this.hateosLinkId = hateosLinkId;
    }

    @Override
    protected void all() {
        this.linkText = "*";
    }

    @Override
    protected void singleton(final I value) {
        this.linkText = hateosLinkId.apply(value);
    }

    @Override
    protected void lowerBoundExclusive(final I value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void lowerBoundInclusive(final I value) {
        this.linkText = hateosLinkId.apply(value) + HasHateosLinkId.HATEOS_LINK_RANGE_SEPARATOR;
    }

    @Override
    protected void upperBoundExclusive(final I value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void upperBoundInclusive(final I value) {
        final String valueText = this.hateosLinkId.apply(value);

        this.linkText = this.linkText.isEmpty() ?
                HasHateosLinkId.HATEOS_LINK_RANGE_SEPARATOR + valueText :
                this.linkText + valueText;
    }

    private final Function<I, String> hateosLinkId;

    private String linkText = "";

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.linkText)
                .build();
    }
}
