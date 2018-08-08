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

import org.junit.Assert;
import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.type.MemberVisibility;

import java.util.Set;

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
        assertEquals("Builder product type " + type.getName() + " is not public", MemberVisibility.PUBLIC, MemberVisibility.get(type));
    }

    @Test
    public final void testNaming() {
        this.checkNaming(Builder.class);
    }

    protected void buildFails() {
        this.buildFails(this.createBuilder());
    }

    protected void buildFails(final Builder<?> builder) {
        this.buildFails(builder, null);
    }

    protected void buildFails(final Builder<?> builder, final String message) {
        Assert.assertNotNull("builder is null", builder);

        try {
            builder.build();
        } catch (final BuilderException expected) {
            if (null != message) {
                Assert.assertEquals("message", message, expected.getMessage());
            }
        }
    }

    final protected void buildFails(final String message) {
        Assert.assertNotNull("expectedMessage", message);
        this.buildFails(this.createBuilder(), message);
    }

    final protected void buildMissingFails(final String firstRequired, final String... requireds) {
        this.buildMissingFails(this.createBuilder(), firstRequired, requireds);
    }

    final protected void buildMissingFails(final Builder<?> builder, final String firstRequired,
                                           final String... requireds) {
        Assert.assertNotNull("builder is null", builder);
        Assert.assertNotNull("required is null", firstRequired);

        try {
            builder.build();
            Assert.fail();
        } catch (final BuilderException expected) {
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
                final Set<String> all = Sets.of(firstRequired);
                for (final String required : requireds) {
                    all.add(required);
                }
                failNotEquals("Builder message missing properties", message, all.toString());
            }
        }
    }

    abstract protected B createBuilder();

    abstract protected Class<T> builderProductType();
}
