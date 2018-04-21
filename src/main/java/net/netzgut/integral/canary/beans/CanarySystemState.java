/**
 * Copyright 2018 Netzgut GmbH <info@netzgut.net>
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
 */
package net.netzgut.integral.canary.beans;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class CanarySystemState {

    private final State                   state;
    private final List<CanaryResult>      results;

    public static final CanarySystemState OK = new CanarySystemState(State.OK);

    public CanarySystemState(List<CanaryResult> results) {
        this(results.stream() //
                    .map(CanaryResult::getState) //
                    .max(Comparator.naturalOrder()) //
                    .orElse(State.OK),
             results);
    }

    public CanarySystemState(State state) {
        this(state, Collections.emptyList());
    }

    public CanarySystemState(State state, List<CanaryResult> results) {
        this.state = state;
        this.results = results;
    }

    public State getState() {
        return this.state;
    }

    public List<CanaryResult> getResults() {
        return this.results;
    }

    private static final String                  RESULT_LINE_FORMAT = "%-8s  %-32s  %s%s";

    private final Function<CanaryResult, String> resultLine         =
        (result) -> String.format(RESULT_LINE_FORMAT,
                                  result.getState().name(),
                                  result.getIdentifier(),
                                  result.getDescription(),
                                  System.lineSeparator());

    public String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append(System.lineSeparator());
        builder.append("System State: " + getState().name());
        builder.append(System.lineSeparator());
        builder.append(System.lineSeparator());
        builder.append(String.format(RESULT_LINE_FORMAT, "STATE", "IDENTIFIER", "DESCRIPTION", System.lineSeparator()));
        this.results.forEach((result) -> builder.append(this.resultLine.apply(result)));
        return builder.toString();
    }
}
