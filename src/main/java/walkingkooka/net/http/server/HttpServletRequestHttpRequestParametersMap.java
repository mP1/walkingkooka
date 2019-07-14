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

package walkingkooka.net.http.server;

import walkingkooka.ToStringBuilder;
import walkingkooka.ToStringBuilderOption;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A readonly parameter map view of the parameters from a {@link javax.servlet.http.HttpServletRequest}.
 */
final class HttpServletRequestHttpRequestParametersMap extends AbstractMap<HttpRequestParameterName, List<String>> {

    static {
        Maps.registerImmutableType(HttpServletRequestHttpRequestParametersMap.class);
    }

    static HttpServletRequestHttpRequestParametersMap with(final Map<String, String[]> parameters) {
        return new HttpServletRequestHttpRequestParametersMap(parameters);
    }

    private HttpServletRequestHttpRequestParametersMap(final Map<String, String[]> parameters) {
        super();
        this.parameters = parameters;
    }

    @Override
    public boolean containsKey(final Object key) {
        return key instanceof HttpRequestParameterName &&
                this.containsKey0(HttpRequestParameterName.class.cast(key));
    }

    private boolean containsKey0(final HttpRequestParameterName key) {
        return this.parameters.containsKey(key.value());
    }

    @Override
    public Set<Entry<HttpRequestParameterName, List<String>>> entrySet() {
        if (null == this.entrySet) {
            this.entrySet = HttpServletRequestHttpRequestParametersMapEntrySet.with(this.parameters.entrySet());
        }
        return this.entrySet;
    }

    private HttpServletRequestHttpRequestParametersMapEntrySet entrySet;

    @Override
    public List<String> get(final Object key) {
        return this.getOrDefault(key, NO_PARAMETER_VALUES);
    }

    private final static List<String> NO_PARAMETER_VALUES = Lists.empty();

    @Override
    public List<String> getOrDefault(final Object key, final List<String> defaultValue) {
        return key instanceof HttpRequestParameterName ?
                this.getOrDefault0(HttpRequestParameterName.class.cast(key), defaultValue) :
                defaultValue;
    }

    private List<String> getOrDefault0(final HttpRequestParameterName key, final List<String> defaultValue) {
        final String[] values = this.parameters.get(key.value());
        return null != values ?
                Lists.of(values) :
                defaultValue;
    }

    @Override
    public int size() {
        return this.parameters.size();
    }

    /**
     * The parameters from the original request.
     */
    private final Map<String, String[]> parameters;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .disable(ToStringBuilderOption.SKIP_IF_DEFAULT_VALUE)
                .surroundValues("{", "}")
                .value(this.parameters)
                .build();
    }
}
