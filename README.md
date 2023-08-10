[![](https://jitpack.io/v/Payselection/Payselection-PayApp-SDK-Android.svg)](https://jitpack.io/#Payselection/Payselection-PayApp-SDK-Android)

## PaySelection SDK for Android

PaySelection SDK позволяет интегрировать прием платежей в мобильные приложение для платформы Android.

### Требования
Для работы PaySelection SDK необходим Android версии 5.0 или выше (API level 21)

### Подключение
В build.gradle уровня проекта добавить репозиторий Jitpack

```
repositories {
	maven { url 'https://jitpack.io' }
}
```
В build.gradle уровня приложения добавить зависимость:
```
implementation 'com.github.Payselection:Payselection-PayApp-SDK-Android:$version'
```


### Полезные ссылки

[Личный кабинет](https://merchant.payselection.com/login/)

[Разработчикам](https://api.payselection.com/#section/Request-signature)

### Структура проекта:

* **app** - Пример вызова методов с использованием SDK
* **sdk** - Исходный код SDK


### Возможности PaySelection SDK:

Вы можете с помощью SDK:

* создать заказ и совершить платеж картой
* получить детализацию по конкретной транзации

### Инициализация PaySelection SDK:

1.	Создайте конфигурацию с данными из личного кабинета

```
val configuration = SdkConfiguration(
	"04a36ce5163f6120972a6bf46a76600953ce252e8d513e4eea1f097711747e84a2b7bf967a72cf064fedc171f5effda2b899e8c143f45303c9ee68f7f562951c88", // Публичный ключ
	"20337" // Site ID
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
            // в результате ответа приходит transactionId и transactionSecretKey, redirectUrl
            // "transactionSecretKey" служит параметром запроса получения статуса по transactionId
            // "redirectUrl" - ссылка на веб-интерфейс платежной системы		
        },
		error = {
			// Обработка ошибки оплаты
		}
	)
}
```
3. Отобразите WebView с полученной ссылкой на веб-интерфейс платежной системы (параметр "redirectUrl" из ответа сервера на метод "pay") с помощью
   ThreeDsDialogFragment, который находится в пакете ui. Используйте интерфейс ThreeDSDialogListener для прослушивания статуса транзакции.

### Другие методы PaySelection SDK:

1. Получение статуса одной транзакции

```
sdk.getTransaction(transactionSecretKey, transactionId)
```

### Поддержка

По возникающим вопросам техничечкого характера обращайтесь на support@payselection.com