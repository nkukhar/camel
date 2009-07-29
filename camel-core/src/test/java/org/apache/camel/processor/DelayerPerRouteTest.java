/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.processor;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;

/**
 * @version $Revision$
 */
public class DelayerPerRouteTest extends ContextTestSupport {

    public void testDelayerPerRoute() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("B", "A", "C");

        template.sendBody("seda:a", "A");
        template.sendBody("seda:b", "B");
        template.sendBody("seda:c", "C");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                context.setDelayer(1000);

                from("seda:a").delayer(500).to("mock:result");

                from("seda:b").noDelayer().to("mock:result");

                from("seda:c").to("mock:result");
            }
        };
    }
}