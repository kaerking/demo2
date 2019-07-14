package com.example.demo.camledemo;

/*
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

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.apache.camel.support.jsse.*;

/**
 * Netty server which returns back an echo of the incoming request.
 */
public final class MyServer {

    private MyServer() {
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.addRouteBuilder(new MyRouteBuilder());

        //=================================
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("***************************");
        ksp.setPassword("****************");

        KeyManagersParameters kmp = new KeyManagersParameters();
        kmp.setKeyPassword("*********************");
        kmp.setKeyStore(ksp);

        //TrustManagersParameters tmp = new TrustManagersParameters();
        //tmp.setKeyStore(ksp);

        // NOTE: Needed since the client uses a loose trust configuration when no ssl context
        // is provided.  We turn on WANT client-auth to prefer using authentication
        SSLContextServerParameters scsp = new SSLContextServerParameters();
        scsp.setClientAuthentication(ClientAuthentication.NONE.name());

        SSLContextParameters sslContextParameters = new SSLContextParameters();
        sslContextParameters.setKeyManagers(kmp);
        //sslContextParameters.setTrustManagers(tmp);
        sslContextParameters.setServerParameters(scsp);

        main.bind("sslContextParameters", sslContextParameters);


        /*HL7MLLPCodec codec = new HL7MLLPCodec();
        codec.setCharset("iso-8859-1");

        main.bind("hl7codec", codec);*/
        //==================================

        //main.bind("myEncoder", new HL7MLLPCodec());
        //main.bind("myDecoder", new MyCodecDecoderFactory());
        main.run(args);
    }
//&encoders=#myEncoder&decoders=#myDecoder
    private static class MyRouteBuilder extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("netty4:tcp://localhost:8889?sync=true&ssl=true&sslContextParameters=#sslContextParameters")
                    .log("Request:  ${id}:${body}")
                    //.filter(simple("${body} contains 'beer'"))
                    // use some delay when its beer to make responses interleaved
                    // and make the delay asynchronous
                    //.delay(simple("${random(1000,9000)}")).asyncDelayed().end()
                    .end()
                    .transform(simple("${body}-Echo"))
                    .log("Response: ${id}:${body}");
        }
    }
}
