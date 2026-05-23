package com.bank.tests.tests;

import com.bank.tests.config.di.ServiceConfig;
import com.bank.tests.service.kafka.PaymentKafkaService;
import com.bank.tests.service.rest.PaymentApiService;

abstract class BaseTest {

    protected final PaymentKafkaService paymentKafkaService = ServiceConfig.getInstance().getPaymentKafkaService();
    protected final PaymentApiService paymentApiService = ServiceConfig.getInstance().getPaymentApiService();

}
