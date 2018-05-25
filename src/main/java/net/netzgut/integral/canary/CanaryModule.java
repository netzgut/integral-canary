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
package net.netzgut.integral.canary;

import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

import net.netzgut.integral.canary.services.CanaryConfig;
import net.netzgut.integral.canary.services.CanaryService;
import net.netzgut.integral.canary.services.CanaryServiceImplementation;

public class CanaryModule {

    public interface Symbols {

        public static final String ENABLED = "net.netzgut.integral.canary.enabled";
    }

    @FactoryDefaults
    @Contribute(SymbolProvider.class)
    public static void contributeFactoryDefaults(MappedConfiguration<String, String> conf) {
        conf.add(Symbols.ENABLED, Boolean.TRUE.toString());
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(CanaryService.class, CanaryServiceImplementation.class);
    }

    public static CanaryConfig buildCanaryConfig() {
        return new CanaryConfig() {
            // default method
        };
    }

}
