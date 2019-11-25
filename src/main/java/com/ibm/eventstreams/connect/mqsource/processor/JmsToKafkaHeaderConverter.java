/**
 * Copyright 2017, 2018, 2019 IBM Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ibm.eventstreams.connect.mqsource.processor;

import org.apache.kafka.connect.header.ConnectHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.Collections;

/*
 * Single responsibility class to copy JMS properties to Kafka headers.
 *
 * */

public class JmsToKafkaHeaderConverter {

    private static final Logger log = LoggerFactory.getLogger(JmsToKafkaHeaderConverter.class);

	/**
	 * Copies the JMS properties to Kafka headers.
	 *
	 * @param message        JMS message.
	 *
	 * @return Kafka connect headers.
	 */
    public ConnectHeaders convertJmsPropertiesToKafkaHeaders(Message message) {

        ConnectHeaders connectHeaders = new ConnectHeaders();

            ArrayList jmsPropertyKeys = null;
            try {
                jmsPropertyKeys = Collections.list(message.getPropertyNames());
                jmsPropertyKeys.forEach(key -> {
                try {
                    connectHeaders.addString(key.toString(), message.getObjectProperty(key.toString()).toString());
                } catch (JMSException e) {
                //Not failing the message processing if JMS properties cannot be read for some reason.
                    log.error("JMS message properties could not be read", e);
                }
                });
            } catch (JMSException e) {
                //Not failing the message processing if JMS properties cannot be read for some reason.
                log.error("JMS message properties could not be read", e);
            }

            return connectHeaders;

    }
}
