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
 * A {@link RangeVisitor} used by {@link HateosResource#rangeIdForHateosLink(Range, Function)}
 */
final class HateosResourceRangeIdForHateosLinkRangeVisitor<I extends Comparable<I>> extends RangeVisitor<I> {

    static <I extends Comparable<I>> String idForHateosLink(final Range<I> range,
                                                            final Function<I, String> idForHateosLink) {
        Objects.requireNonNull(range, "range");
        Objects.requireNonNull(idForHateosLink, "idForHateosLink");

        final HateosResourceRangeIdForHateosLinkRangeVisitor<I> visitor = new HateosResourceRangeIdForHateosLinkRangeVisitor<>(idForHateosLink);
        visitor.accept(range);
        return visitor.linkText;
    }

    HateosResourceRangeIdForHateosLinkRangeVisitor(final Function<I, String> idForHateosLink) {
        super();
        this.idForHateosLink = idForHateosLink;
    }

    @Override
    protected void all() {
        this.linkText = "*";
    }

    @Override
    protected void singleton(final I value) {
        this.linkText = idForHateosLink.apply(value);
    }

    @Override
    protected void lowerBoundExclusive(final I value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void lowerBoundInclusive(final I value) {
        this.linkText = idForHateosLink.apply(value) + HateosResource.HATEOS_LINK_RANGE_SEPARATOR;
    }

    @Override
    protected void upperBoundExclusive(final I value) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void upperBoundInclusive(final I value) {
        final String valueText = this.idForHateosLink.apply(value);

        this.linkText = this.linkText.isEmpty() ?
                HateosResource.HATEOS_LINK_RANGE_SEPARATOR + valueText :
                this.linkText + valueText;
    }

    private final Function<I, String> idForHateosLink;

    private String linkText = "";

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .value(this.linkText)
                .build();
    }
}
