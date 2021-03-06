/*
 * Copyright (C)2009 - SSHJ Contributors
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
package com.hierynomus.sshj.transport.kex;

import com.hierynomus.sshj.test.BaseAlgorithmTest;
import net.schmizz.sshj.Config;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.common.Factory;
import net.schmizz.sshj.transport.kex.DHGexSHA1;
import net.schmizz.sshj.transport.kex.DHGexSHA256;
import net.schmizz.sshj.transport.kex.ECDHNistP;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.kex.BuiltinDHFactories;
import org.apache.sshd.common.kex.KeyExchange;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.kex.DHGEXServer;
import org.apache.sshd.server.kex.DHGServer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class KeyExchangeTest extends BaseAlgorithmTest {

    @Parameterized.Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][]{
                {DHGEXServer.newFactory(BuiltinDHFactories.dhgex), new DHGexSHA1.Factory()},
                {DHGEXServer.newFactory(BuiltinDHFactories.dhgex256), new DHGexSHA256.Factory()},
                {DHGServer.newFactory(BuiltinDHFactories.ecdhp256), new ECDHNistP.Factory256()},
                {DHGServer.newFactory(BuiltinDHFactories.ecdhp384), new ECDHNistP.Factory384()},
                {DHGServer.newFactory(BuiltinDHFactories.ecdhp521), new ECDHNistP.Factory521()}
                // Not supported yet by MINA {null, new Curve25519SHA256.Factory()}
        });
    }

    private Factory.Named<net.schmizz.sshj.transport.kex.KeyExchange> clientFactory;
    private NamedFactory<KeyExchange> serverFactory;

    public KeyExchangeTest(NamedFactory<KeyExchange> serverFactory, Factory.Named<net.schmizz.sshj.transport.kex.KeyExchange> clientFactory) {
        this.clientFactory = clientFactory;
        this.serverFactory = serverFactory;
    }

    @Override
    protected Config getClientConfig(DefaultConfig config) {
        config.setKeyExchangeFactories(Collections.singletonList(clientFactory));
        return config;
    }

    @Override
    protected void configureServer(SshServer server) {
        server.setKeyExchangeFactories(Collections.singletonList(serverFactory));
    }
}
