package com.arrnel.tests.tests;

import com.arrnel.tests.model.dto.payment.*;
import com.arrnel.tests.model.enums.AllureTag;
import com.arrnel.tests.service.kafka.KafkaStore;
import com.arrnel.tests.util.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.arrnel.tests.model.enums.OperationStatus.SUCCESS;
import static com.arrnel.tests.util.DataGenerator.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag(AllureTag.GATEWAY_SERVICE_TEST)
@DisplayName("Gateway service kafka tests")
class GatewayKafkaTest extends BaseTest {

    @Tag(AllureTag.BANK_ACCOUNT_TEST)
    @Test
    @DisplayName("Should create bank account and return success response if user_id not exists")
    void shouldProduceBankAccountAndReturnSuccessResponseIfUserIdNotExistsTest() {
        // Data
        var request = generateCreateBankAccountRequest();

        // Steps
        var responseResult = paymentApiService.addNewBankAccount(request);

        assertAll("Check gateway service produce create bank account record and return success response",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );

    }

    @Tag(AllureTag.BANK_ACCOUNT_TEST)
    @Test
    @DisplayName("Should not create bank account and return error if user_id is already taken")
    void shouldNotCreateBankAccountAndReturnErrorIfUserIdIsAlreadyTakenTest() {
        // Data
        var request = generateCreateBankAccountRequest();

        // Steps
        paymentKafkaService.sendCreateBankAccountRequest(request);
        var responseResult = paymentKafkaService.sendCreateBankAccountRequestWithError(request);
        var kafkaRecord = KafkaStore.INSTANCE.containsBankAccountRequest(request.userId());

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "userId",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has SUCCESS status"
                ),
                () -> assertEquals(
                        "[ValidBankAccount] User with ID [%d] already has a bank account".formatted(request.userId()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.CURRENCY_WALLET_TEST)
    @Test
    @DisplayName("Should create currency wallet and return SUCCESS response")
    void shouldCreateCurrencyWalletAndReturnSuccessResponseTest() {

        // Steps
        var bankAccountResponse = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var responseResult = paymentKafkaService.sendCreateCurrencyWalletRequest(
                generateCurrencyWalletRequest(bankAccountResponse.id())
        );

        assertAll("Check payment service produce success operation response message",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.CURRENCY_WALLET_TEST)
    @Test
    @DisplayName("Should not create currency wallet and return error if bank_account_id not exists")
    void shouldNotCreateCurrencyWalletAndReturnErrorIfBankAccountIdNotExistsTest() {
        // Data
        var request = generateCurrencyWalletRequest();

        // Steps
        var responseResult = paymentKafkaService.sendCreateCurrencyWalletRequestWithError(request);

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "bankAccountId",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidCurrencyWallet] Bank account with ID [%d] not found".formatted(request.bankAccountId()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.DEPOSIT_TEST)
    @Test
    @DisplayName("Should add deposit and return SUCCESS response")
    void shouldAddDepositAndReturnSuccessResponseTest() {
        // Steps
        var bankAccountResponse = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var currencyWalletResponse = paymentKafkaService.sendCreateCurrencyWalletRequest(
                generateCurrencyWalletRequest(bankAccountResponse.id())
        );
        var responseResult = paymentKafkaService.sendDepositRequest(
                generateDepositRequest(currencyWalletResponse.id())
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.DEPOSIT_TEST)
    @Test
    @DisplayName("Should not add deposit and return error if currency_wallet_id not exists")
    void shouldNotAddDepositAndReturnErrorIfCurrencyWalletNotExistsTest() {
        // Data
        var depositRequest = generateDepositRequest();

        // Steps
        var responseResult = paymentKafkaService.sendDepositRequestWithError(depositRequest);

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "currencyWalletId",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidDeposit] Currency wallet with ID [%d] not found".formatted(depositRequest.currencyWalletId()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.TRANSFER_TEST)
    @Test
    @DisplayName("Should transfer and return SUCCESS response if currencies the same")
    void shouldTransferAndReturnSuccessResponseIfCurrenciesTheSameTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var transferAmount = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);
        var currency = DataGenerator.randomCurrency();

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                currency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                currency
                        ))
                .id();

        var responseResult = paymentKafkaService.sendTransferRequest(
                new CreateTransferRequestDTO(
                        sourceCwId,
                        destinationCwId,
                        transferAmount,
                        randomText())
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.TRANSFER_TEST)
    @Test
    @DisplayName("Should transfer and return SUCCESS response if currencies are different")
    void shouldTransferAndReturnSuccessResponseIfCurrenciesAreDifferentTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var transferAmount = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency(sourceCurrency);

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var responseResult = paymentKafkaService.sendTransferRequest(
                new CreateTransferRequestDTO(
                        sourceCwId,
                        destinationCwId,
                        transferAmount,
                        randomText())
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.TRANSFER_TEST)
    @Test
    @DisplayName("Should not transfer and return error if source currency wallet not exists")
    void shouldNotTransferAndReturnErrorIfSourceCurrencyWalletNotExistsTest() {
        // Data
        var sourceCwId = DataGenerator.generateLong();
        var amount = DataGenerator.randomBigDecimal();

        // Steps
        var destinationBankAccountId = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest())
                .id();
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccountId,
                                DataGenerator.randomCurrency()
                        ))
                .id();

        var responseResult = paymentKafkaService.sendTransferRequestWithError(
                new CreateTransferRequestDTO(
                        sourceCwId,
                        destinationCwId,
                        amount,
                        randomText())
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "from",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidTransfer] Source currency wallet with ID [%d] not found".formatted(sourceCwId),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.TRANSFER_TEST)
    @Test
    @DisplayName("Should not transfer and return error if destination currency wallet not exists")
    void shouldNotTransferAndReturnErrorIfDestinationCurrencyWalletNotExistsTest() {
        // Data
        var destinationCwId = DataGenerator.generateLong();
        var amount = DataGenerator.randomBigDecimal();

        // Steps
        var sourceBandAccountId = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest())
                .id();
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBandAccountId,
                                DataGenerator.randomCurrency()
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, amount));

        var responseResult = paymentKafkaService.sendTransferRequestWithError(
                new CreateTransferRequestDTO(
                        sourceCwId,
                        destinationCwId,
                        amount,
                        randomText())
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "to",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidTransfer] Destination currency wallet with ID [%d] not found".formatted(destinationCwId),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.TRANSFER_TEST)
    @Test
    @DisplayName("Should not transfer and return error if source currency wallet not have enough money")
    void shouldNotTransferAndReturnErrorIfSourceCurrencyWalletNotHaveEnoughMoneyExistsTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var transferAmount = depositAmount.add(new BigDecimal("0.01"));
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency(sourceCurrency);

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var responseResult = paymentKafkaService.sendTransferRequestWithError(
                new CreateTransferRequestDTO(
                        sourceCwId,
                        destinationCwId,
                        transferAmount,
                        randomText())
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "amount",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidTransfer] Insufficient funds in source wallet: current balance [%s], required [%s]"
                                .formatted(depositAmount.toPlainString(), transferAmount.toPlainString()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.REFUND_TEST)
    @Test
    @DisplayName("Should refund and return SUCCESS response")
    void shouldRefundAndReturnSuccessResponseTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var refundAmount = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency();

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var transferId = paymentKafkaService.sendTransferRequest(
                        new CreateTransferRequestDTO(
                                sourceCwId,
                                destinationCwId,
                                depositAmount,
                                randomText()
                        ))
                .id();

        var responseResult = paymentKafkaService.sendRefundRequest(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount,
                        randomText())
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.REFUND_TEST)
    @Test
    @DisplayName("Should refund multiple times and return SUCCESS response")
    void shouldRefundMultipleTimesAndReturnSuccessResponseTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var refundAmount1 = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);
        var refundAmount2 = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount.subtract(refundAmount1));
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency();

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var transferId = paymentKafkaService.sendTransferRequest(
                        new CreateTransferRequestDTO(
                                sourceCwId,
                                destinationCwId,
                                depositAmount,
                                randomText()
                        ))
                .id();

        paymentKafkaService.sendRefundRequest(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount1,
                        randomText())
        );

        var responseResult = paymentKafkaService.sendRefundRequest(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount2,
                        randomText())
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );
    }

    @Tag(AllureTag.REFUND_TEST)
    @Test
    @DisplayName("Should not refund and return error if not enough money for refund")
    void shouldNotRefundAndReturnErrorIfNotEnoughMoneyForRefundTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var refundAmount = depositAmount.add(BigDecimal.ONE);
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency();

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var transferId = paymentKafkaService.sendTransferRequest(
                        new CreateTransferRequestDTO(
                                sourceCwId,
                                destinationCwId,
                                depositAmount,
                                randomText()
                        ))
                .id();

        var responseResult = paymentKafkaService.sendRefundRequestWithError(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount,
                        randomText())
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "amount",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidRefund] Requested refund amount [%s] exceeds available refund limit [%s]"
                                .formatted(refundAmount.toPlainString(), depositAmount.toPlainString()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.REFUND_TEST)
    @Test
    @DisplayName("Should not refund multiple times and return error if not enough money for refund")
    void shouldNotRefundMultipleTimesAndReturnErrorIfNotEnoughMoneyForRefundTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var refundAmount1 = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);
        var refundAmount2 = DataGenerator.randomBigDecimal(depositAmount.subtract(refundAmount1), depositAmount);
        var availableAmount = depositAmount.subtract(refundAmount1);
        var sourceCurrency = DataGenerator.randomCurrency();
        var destinationCurrency = DataGenerator.randomCurrency();

        // Steps
        var sourceBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var sourceCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                sourceBankAccount.id(),
                                sourceCurrency
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(sourceCwId, depositAmount));

        var destinationBankAccount = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest());
        var destinationCwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                destinationBankAccount.id(),
                                destinationCurrency
                        ))
                .id();

        var transferId = paymentKafkaService.sendTransferRequest(
                        new CreateTransferRequestDTO(
                                sourceCwId,
                                destinationCwId,
                                depositAmount,
                                randomText()
                        ))
                .id();

        paymentKafkaService.sendRefundRequest(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount1,
                        randomText())
        );

        var responseResult = paymentKafkaService.sendRefundRequestWithError(
                new CreateRefundRequestDTO(
                        transferId,
                        refundAmount2,
                        randomText())
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "amount",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidRefund] Requested refund amount [%s] exceeds available refund limit [%s]"
                                .formatted(refundAmount2.toPlainString(), availableAmount.toPlainString()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.WITHDRAWAL_TEST)
    @Test
    @DisplayName("Should withdrawal and return SUCCESS response if currencies the same")
    void shouldWithdrawalAndReturnSuccessResponseIfCurrenciesTheSameTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var withdrawalAmount = DataGenerator.randomBigDecimal(BigDecimal.ZERO, depositAmount);

        // Steps
        var bankAccountId = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest())
                .id();
        var cwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                bankAccountId,
                                DataGenerator.randomCurrency()
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(cwId, depositAmount));

        var responseResult = paymentKafkaService.sendWithdrawalRequest(
                new CreateWithdrawalRequestDTO(
                        cwId,
                        withdrawalAmount
                )
        );

        assertAll("Check payment service produce success operation response message if send create deposit request",
                () -> assertNotNull(responseResult.id(), "Check response id not null"),
                () -> assertEquals(SUCCESS, responseResult.status(), "Check response has SUCCESS status"),
                () -> assertNull(responseResult.errorMessage(), "Check error message is null"),
                () -> assertNotNull(responseResult.createdAt(), "Check created date not null")
        );

    }

    @Tag(AllureTag.WITHDRAWAL_TEST)
    @Test
    @DisplayName("Should not withdrawal and return error if not enough money")
    void shouldNotWithdrawalAndReturnErrorIfNotEnoughMoneyTest() {
        // Data
        var depositAmount = DataGenerator.randomBigDecimal();
        var withdrawalAmount = depositAmount.add(BigDecimal.ONE);

        // Steps
        var bankAccountId = paymentKafkaService.sendCreateBankAccountRequest(generateCreateBankAccountRequest())
                .id();
        var cwId = paymentKafkaService.sendCreateCurrencyWalletRequest(
                        new CreateCurrencyWalletRequestDTO(
                                bankAccountId,
                                DataGenerator.randomCurrency()
                        ))
                .id();
        paymentKafkaService.sendDepositRequest(new CreateDepositRequestDTO(cwId, depositAmount));

        var responseResult = paymentKafkaService.sendWithdrawalRequestWithError(
                new CreateWithdrawalRequestDTO(
                        cwId,
                        withdrawalAmount
                )
        );

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "amount",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidWithdrawal] Insufficient funds in wallet: current balance [%s], requested [%s]"
                                .formatted(depositAmount.toPlainString(), withdrawalAmount.toPlainString()),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

    @Tag(AllureTag.WITHDRAWAL_TEST)
    @Test
    @DisplayName("Should not withdrawal and return error if currency wallet not found")
    void shouldNotWithdrawalAndReturnErrorIfCurrencyWalletNotExistsTest() {
        // Data
        var cwId = DataGenerator.generateLong();
        var amount = DataGenerator.randomBigDecimal();

        // Steps
        var responseResult = paymentKafkaService.sendWithdrawalRequestWithError(new CreateWithdrawalRequestDTO(cwId, amount));

        assertAll("Check payment service produce error operation response message",
                () -> assertEquals(
                        "Illegal operation",
                        responseResult.error().message(),
                        "Check response has expected errors message"
                ),
                () -> assertEquals(
                        "from",
                        responseResult.error().errors().getFirst().reason(),
                        "Check response has expected reason"
                ),
                () -> assertEquals(
                        "[ValidWithdrawal] Currency wallet with ID [%d] not found".formatted(cwId),
                        responseResult.error().errors().getFirst().itemMessage(),
                        "Check response has expected error itemMessage"
                )
        );
    }

}
