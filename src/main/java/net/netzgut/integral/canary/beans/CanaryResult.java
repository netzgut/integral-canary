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

public class CanaryResult {

    public static CanaryResult Failed(String identifier, String reason) {
        return new CanaryResult(identifier, State.FAILED, reason);
    }

    public static CanaryResult Degraded(String identifier, String reason) {
        return new CanaryResult(identifier, State.DEGRADED, reason);
    }

    public static CanaryResult Ok(String identifier) {
        return new CanaryResult(identifier, State.OK, "");
    }

    private final String identifier;
    private final State  state;
    private final String description;

    public CanaryResult(String identifier, State state, String description) {
        super();
        this.identifier = identifier;
        this.state = state;
        this.description = description;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public State getState() {
        return this.state;
    }

    public String getDescription() {
        return this.description;
    }

}
