## PaySelection SDK for Android

PaySelection SDK позволяет интегрировать прием платежей в мобильные приложение для платформы Android.

### Требования
Для работы PaySelection SDK необходим Android версии 5.0 или выше (API level 21)

### Полезные ссылки

[Личный кабинет](https://merchant.payselection.com/login/)

[Разработчикам](https://api.payselection.com/#section/Request-signature)

### Структура проекта:

* **app** - Пример вызова методов с использованием SDK
* **sdk** - Исходный код SDK


### Возможности PaySelection SDK:

Вы можете с помощью SDK:

* создать заказ и совершить платеж картой
* получить все транзакции по заказу
* получить детализацию по конкретной транзации
* запросить возврат по заказу

### Инициализация PaySelection SDK:

1.	Создайте конфигурацию с данными из личного кабинета

```
val configuration = SdkConfiguration(
	"046fdb81fc698b90dd12f005bc399208fd01bb3225e962d58e115c86d905c5f2144cb5dfe2a30868fdf165a5010de46235a248c645b657c046038466537b01f1d6", // Публичный ключ
	"20160", // Site ID
	"5ve4wkzTycthTKut" // Секретный ключ
)
```

2.	Создайте экземпляр PaySelectionPaymentsSdk для работы с API

```
val sdk = PayselectionPaymentsSdk.getInstance(configuration)
```

### Оплата с использованием PaySelection SDK:

1. Создайте объект PaymentData с информацией о транзакции и данными карты

```
val paymentData = PaymentData.create(
	transactionDetails = TransactionDetails(
		amount = "100",
		currency = "RUB"
	),
	cardDetails = CardDetails(
		cardholderName = "TEST CARD",
		cardNumber = "4111111111111111",
		cvc = "123",
		expMonth = "12",
		expYear = "24"
	)
)
```

2. Асинхронно (например, с использыванием coroutines) вызовите метод pay

```
viewModelScope.launch(handler) {
	sdk.pay(
		orderId = orderId, // ID заказа в вашей системе
		description = "", // Комментарий к оплате
		paymentData = paymentData // Данные из предыдущего шага
	).proceedResult(
		success = {
			// Получение данных о созданной транзакции
		},
		error = {
			// Обработка ошибки оплаты
		}
	)
}
```

### Другие методы PaySelection SDK:

1. Получение всех транзакций по заказку

```
sdk.getOrderStatus(orderId)
```

2. Получение статуса одной транзакции

```
sdk.getTransaction(transactionId)
```

3. Оформление возврата

```
sdk.refund(
	refundData = RefundData(
		transactionId = transactionId,
		amount = "100",
		currency = "RUB"
	)
)
```

4. Завершения одностадийной/двухстадийной операции оплаты

```
sdk.confirm(
	confirmData = ConfirmData(
		transactionId = transactionId,
		orderId = orderId,
		paRes = "string",
		MD = "string"
	)
)
```

### Поддержка

По возникающим вопросам техничечкого характера обращайтесь на support@payselection.com