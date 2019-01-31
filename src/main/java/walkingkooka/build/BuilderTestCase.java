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

package walkingkooka.build;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.type.MemberVisibility;

import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Base class for any {@link Builder} which includes helpers that assert failure if {@link
 * Builder#build()} is called without any additions.
 */
abstract public class BuilderTestCase<B extends Builder<T>, T> extends BuilderLikeTestCase<B> {

    protected BuilderTestCase() {
        super();
    }

    @Test
    public final void testBuilderProductTypePublic() {
        final Class<T> type = this.builderProductType();
        assertEquals(MemberVisibility.PUBLIC,
                MemberVisibility.get(type),
                "Builder product type " + type.getName() + " is not public");
    }

    @Test
    public final void testNaming() {
        final Class<?> type = Cast.to(Builder.class);
        this.checkNaming(type);
    }

    protected void buildAndCheck(final Builder<T> builder, final T product) {
        assertEquals(product, builder.build(), builder.toString());
    }

    protected void buildAndCheck2(final Builder<?> builder, final String productToString) {
        assertEquals(productToString, builder.build().toString(), () -> builder.toString());
    }

    protected void buildFails() {
        this.buildFails(this.createBuilder());
    }

    protected void buildFails(final Builder<?> builder) {
        this.buildFails(builder, null);
    }

    protected void buildFails(final Builder<?> builder, final String message) {
        Objects.requireNonNull(builder, "builder");

        final BuilderException expected = assertThrows(BuilderException.class, () -> {
            builder.build();
        });
        if (null != message) {
            assertEquals("message", message, expected.getMessage());
        }
    }

    final protected void buildFails(final String message) {
        assertNotNull("expectedMessage", message);
        this.buildFails(this.createBuilder(), message);
    }

    final protected void buildMissingFails(final String firstRequired,
                                           final String... requireds) {
        this.buildMissingFails(this.createBuilder(), firstRequired, requireds);
    }

    final protected void buildMissingFails(final Builder<?> builder,
                                           final String firstRequired,
                                           final String... requireds) {
        Objects.requireNonNull(builder, "builder");
        Objects.requireNonNull(firstRequired, "firstRequired");

        final BuilderException expected = assertThrows(BuilderException.class, () -> {
            builder.build();
        });

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
            assertEquals("Builder message missing properties", message, all.toString());
        }
    }

    abstract protected B createBuilder();

    abstract protected Class<T> builderProductType();

    @Override
    protected MemberVisibility typeVisibility() {
        return MemberVisibility.PUBLIC;
    }
}
