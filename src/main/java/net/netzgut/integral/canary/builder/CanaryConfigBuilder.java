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
package net.netzgut.integral.canary.builder;

import java.util.Optional;

import org.slf4j.event.Level;

import net.netzgut.integral.canary.services.CanaryConfig;

public class CanaryConfigBuilder {

    private Level logLevel = Level.ERROR;

    public CanaryConfigBuilder() {
        // NOOP
    }

    public CanaryConfigBuilder logLevel(Level logLevel) {

        this.logLevel = Optional.ofNullable(logLevel).orElse(Level.ERROR);
        return this;
    }

    public static CanaryConfig build(Level logLevel) {
        CanaryConfigBuilder builder = new CanaryConfigBuilder();
        builder.logLevel(logLevel);
        return builder.build();
    }

    public static CanaryConfig defaultValues() {
        return new CanaryConfig() {
            // NOOP
        };
    }

    public CanaryConfig build() {
        return new CanaryConfig() {

            @Override
            public Level getLogLevel() {
                return CanaryConfigBuilder.this.logLevel;
            }
        };
    }
}
