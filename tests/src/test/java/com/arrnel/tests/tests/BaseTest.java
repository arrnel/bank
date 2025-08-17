package com.arrnel.tests.tests;

import com.arrnel.tests.config.di.ServiceConfig;
import com.arrnel.tests.service.PaymentKafkaService;

abstract class BaseTest {

    protected final PaymentKafkaService paymentKafkaService = ServiceConfig.getInstance().getPaymentKafkaService();

}
