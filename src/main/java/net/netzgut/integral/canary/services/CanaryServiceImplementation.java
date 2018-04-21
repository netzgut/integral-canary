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
package net.netzgut.integral.canary.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import net.netzgut.integral.canary.CanaryCheck;
import net.netzgut.integral.canary.CanaryModule.Symbols;
import net.netzgut.integral.canary.beans.CanaryResult;
import net.netzgut.integral.canary.beans.CanarySystemState;
import net.netzgut.integral.canary.beans.State;

public class CanaryServiceImplementation implements CanaryService {

    private static final Logger          log = LoggerFactory.getLogger(CanaryServiceImplementation.class);

    private final List<CanaryCheck>      checks;
    private final CanaryConfig           config;
    private final Consumer<CanaryResult> logFn;

    private final boolean                enabled;

    public CanaryServiceImplementation(List<CanaryCheck> checks,
                                       CanaryConfig config,
                                       @Inject @Symbol(Symbols.ENABLED) boolean enabled) {
        super();
        this.checks = checks;
        this.config = config;
        this.enabled = enabled;

        this.logFn = buildLogFn();

        if (enabled == false) {
            CanaryServiceImplementation.log.info("Canary is disabled");
        }
    }

    @Override
    public CanarySystemState check() {
        if (this.enabled == false) {
            return CanarySystemState.OK;
        }

        State worstState = State.OK;
        List<CanaryResult> results = new ArrayList<>();

        for (CanaryCheck check : this.checks) {
            CanaryResult result = null;
            try {
                result = check.run();
            }
            catch (Exception e) {
                CanaryServiceImplementation.log.error("Check '{}' failed with an exception.", check.getIdentifier(), e);
            }
            finally {
                if (result == null) {
                    continue;
                }
            }

            results.add(result);

            if (result.getState() != State.OK) {
                this.logFn.accept(result);
                if (State.COMPARATOR.compare(result.getState(), worstState) > 0) {
                    worstState = result.getState();
                }
            }

            if (check.stopOtherChecks(result.getState())) {
                break;
            }
        }
        return new CanarySystemState(worstState, results);
    }

    private Consumer<CanaryResult> buildLogFn() {
        BiConsumer<String, Object[]> logger;
        Level level = this.config.getLogLevel();
        if (level == Level.WARN) {
            logger = log::warn;
        }
        else if (level == Level.INFO) {
            logger = log::info;
        }
        else if (level == Level.DEBUG) {
            logger = log::debug;
        }
        else if (level == Level.TRACE) {
            logger = log::trace;
        }
        else {
            log.warn("Invalid log level: {}. Fallback to Level.ERROR", level);
            logger = log::error;
        }

        return (result) -> {
            logger.accept("Canary check failed: {} -> {} - {}",
                          new String[] { result.getIdentifier(), result.getState().name(), result.getDescription() });
        };

    }
}
