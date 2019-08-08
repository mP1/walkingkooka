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

package walkingkooka.datetime;

import java.util.List;
import java.util.Locale;

public class FakeDateTimeContext implements DateTimeContext {

    @Override
    public List<String> ampms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String ampm(final int hourOfDay) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Locale locale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String monthName(final int month) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> monthNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String monthNameAbbreviation(final int month) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int twoDigitYear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String weekDayName(final int day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> weekDayNameAbbreviations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String weekDayNameAbbreviation(final int day) {
        throw new UnsupportedOperationException();
    }
}
