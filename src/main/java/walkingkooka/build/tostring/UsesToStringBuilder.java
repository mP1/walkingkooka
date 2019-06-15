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

package walkingkooka.build.tostring;

/**
 * Notes that an {@link Object#toString()} uses a {@link ToStringBuilder} to build its result. This
 * method allows a class to share a {@link ToStringBuilder} rather than building lots of small
 * {@link String strings} etc.
 */
public interface UsesToStringBuilder {

    /**
     * Accepts and adds its {@link Object#toString()} form to the given {@link ToStringBuilder
     * builder}.
     */
    void buildToString(ToStringBuilder builder);
}
