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

package walkingkooka;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;

public final class ToStringBuilderSaveStateRunnableTest extends ToStringBuilderTestCase<ToStringBuilderSaveStateRunnable>
    implements ToStringTesting<ToStringBuilderSaveStateRunnable>,
    TypeNameTesting<ToStringBuilderSaveStateRunnable> {

    @Test
    public void testSaveAndRestoreState() {
        final ToStringBuilder builder = ToStringBuilder.empty()
            .labelSeparator("label1")
            .separator("sep1")
            .valueSeparator("val1")
            .disable(ToStringBuilderOption.QUOTE);

        final Runnable state = builder.saveState();
        final String toString = builder.toString();

        builder.labelSeparator("label2")
            .separator("sep2")
            .valueSeparator("val2")
            .enable(ToStringBuilderOption.QUOTE);

        this.checkNotEquals(toString, builder.toString(), "builder.toString");

        state.run();
        this.checkEquals(toString, builder.toString(), "builder.toString");

        builder.labelSeparator("label2")
            .separator("sep2")
            .valueSeparator("val2")
            .enable(ToStringBuilderOption.QUOTE);

        state.run();
        this.checkEquals(toString, builder.toString(), "builder.toString");
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(ToStringBuilder.empty()
                .labelSeparator("label1")
                .separator("sep1")
                .disable(ToStringBuilderOption.QUOTE).saveState(),
            "labelSeparator=\"label1\" options=INLINE_ELEMENTS, SKIP_IF_DEFAULT_VALUE separator=\"sep1\" valueSeparator=\", \"");
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ToStringBuilderSaveStateRunnable> type() {
        return ToStringBuilderSaveStateRunnable.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    // TypeNameTesting..................................................................................................

    @Override
    public String typeNameSuffix() {
        return Runnable.class.getSimpleName();
    }
}
