/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.transport.http_undertow.handlers;

import org.apache.cxf.transport.http_undertow.CXFUndertowHttpHandler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.RequestLimitingHandler;

public class CxfRequestLimitingHandler implements CXFUndertowHttpHandler {
    
    private HttpHandler next;
    private RequestLimitingHandler requestLimitingHandler;
    private int maximumConcurrentRequests; 
    private int queueSize;
    
    public CxfRequestLimitingHandler(int maximumConcurrentRequests, int queueSize) {
        this.setMaximumConcurrentRequests(maximumConcurrentRequests);
        this.setQueueSize(queueSize);
    }
    
    public CxfRequestLimitingHandler() {
        this.setMaximumConcurrentRequests(1);
        this.setQueueSize(1);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        
        if (requestLimitingHandler == null) {
            buildLogHandler();
        }
        this.requestLimitingHandler.handleRequest(exchange);
    }

    @Override
    public void setNext(HttpHandler nextHandler) {
        this.next = nextHandler;
        
    }
    
    private void buildLogHandler() {
        HttpHandler handler = this.next;
        this.requestLimitingHandler = 
            new RequestLimitingHandler(getMaximumConcurrentRequests(), getQueueSize(), handler);

    }

    public int getMaximumConcurrentRequests() {
        return maximumConcurrentRequests;
    }

    public void setMaximumConcurrentRequests(int maximumConcurrentRequests) {
        this.maximumConcurrentRequests = maximumConcurrentRequests;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
    
}

