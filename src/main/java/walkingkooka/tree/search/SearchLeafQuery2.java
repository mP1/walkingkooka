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
 *
 */

package walkingkooka.tree.search;

abstract class SearchLeafQuery2 extends SearchLeafQuery {

    SearchLeafQuery2(final SearchQueryValue value, final SearchQueryTester tester){
        super(value);
        this.tester = tester;
    }

    @Override
    final void visit(final SearchBigDecimalNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchBigIntegerNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchDoubleNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchLocalDateNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchLocalDateTimeNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchLocalTimeNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchLongNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    @Override
    final void visit(final SearchTextNode node, final SearchQueryContext context) {
        if(this.tester.test(node)) {
            context.success(node);
        } else {
            context.failure(node);
        }
    }

    final SearchQueryTester tester;
}
