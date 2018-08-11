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

package walkingkooka.util.systemproperty;

import org.junit.Test;
import walkingkooka.collect.set.Sets;
import walkingkooka.test.ConstantsTestCase;
import walkingkooka.type.FieldAttributes;
import walkingkooka.type.MemberVisibility;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.Assert.assertSame;

final public class SystemPropertyConstantsTest extends ConstantsTestCase<SystemProperty> {

    @Test
    public void testConstantsNaming() throws Exception {
        for (final Field field : SystemProperty.class.getDeclaredFields()) {
            if (false == FieldAttributes.STATIC.is(field)) {
                continue;
            }
            if (MemberVisibility.get(field)!= MemberVisibility.PUBLIC) {
                continue;
            }
            if (false == SystemProperty.class.equals(field.getType())) {
                continue;
            }
            final String fieldName = field.getName();
            if(fieldName.equals("FILE_SYSTEM_CASE_SENSITIVITY")){
                continue;
            }
            final String name = fieldName.toLowerCase().replace("_", ".");
            final SystemProperty property = SystemProperty.class.cast(field.get(null));
            assertSame(property, SystemProperty.get(name));
        }
    }

    @Override
    protected Class<SystemProperty> type() {
        return SystemProperty.class;
    }

    @Override
    protected Set<SystemProperty> intentionalDuplicates() {
        return Sets.empty();
    }
}
