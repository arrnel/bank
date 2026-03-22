package com.arrnel.tests.tests;

import com.arrnel.tests.config.di.ServiceConfig;
import com.arrnel.tests.service.kafka.PaymentKafkaService;
import com.arrnel.tests.service.rest.PaymentApiService;

abstract class BaseTest {

    protected final PaymentKafkaService paymentKafkaService = ServiceConfig.getInstance().getPaymentKafkaService();
    protected final PaymentApiService paymentApiService = ServiceConfig.getInstance().getPaymentApiService();

}
