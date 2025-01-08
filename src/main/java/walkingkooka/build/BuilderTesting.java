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

package walkingkooka.build;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.reflect.TypeNameTesting;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Mixin interface for testing {@link Builder}
 */
public interface BuilderTesting<B extends Builder<T>, T> extends ThrowableTesting,
    ToStringTesting<B>,
    TypeNameTesting<B> {

    @Test
    default void testBuilderProductTypePublic() {
        final Class<T> type = this.builderProductType();
        this.checkEquals(JavaVisibility.PUBLIC,
            JavaVisibility.of(type),
            "Builder product type " + type.getName() + " is not public");
    }

    default void buildAndCheck(final Builder<T> builder, final T product) {
        this.checkEquals(product, builder.build(), builder.toString());
    }

    default void buildFails(final Builder<?> builder) {
        this.buildFails(builder, null);
    }

    default void buildFails(final Builder<?> builder, final String message) {
        Objects.requireNonNull(builder, "builder");

        final BuilderException expected = assertThrows(BuilderException.class, builder::build);
        if (null != message) {
            this.checkMessage(expected, message);
        }
    }

    default void buildMissingFails(final Builder<?> builder,
                                   final String firstRequired,
                                   final String... requireds) {
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(firstRequired, "firstRequired");

        final BuilderException expected = assertThrows(BuilderException.class, builder::build);

        final String message = expected.getMessage();
        final Set<String> wrong = Sets.ordered();

        if (false == message.contains(firstRequired)) {
            wrong.add(firstRequired);
        }
        for (final String required : requireds) {
            if (false == message.contains(required)) {
                wrong.add(required);
            }
        }

        if (false == wrong.isEmpty()) {
            final Set<String> all = Sets.ordered();
            all.add(firstRequired);
            all.addAll(Lists.of(requireds));
            this.checkEquals("Builder message missing properties", message, all.toString());
        }
    }

    B createBuilder();

    Class<T> builderProductType();

    // TypeNameTesting .........................................................................................

    @Override
    default String typeNamePrefix() {
        return "";
    }

    @Override
    default String typeNameSuffix() {
        return Builder.class.getSimpleName();
    }
}
