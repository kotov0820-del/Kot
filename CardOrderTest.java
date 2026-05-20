package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardOrderTest {

    @BeforeEach
    void setup() {
        // Открываем локальную страницу перед каждым тестом
        open("http://localhost:9999");
    }

    // ЗАДАЧА №1: Успешный заказ карты (Happy Path)
    @Test
    void shouldSubmitRequestSuccessfully() {
        // Находим поле имени и вводим текст
        $("[data-test-id=name] input").setValue("Иванов Иван");
        // Находим поле телефона и вводим номер
        $("[data-test-id=phone] input").setValue("+79998887766");
        // Кликаем по чекбоксу согласия
        $("[data-test-id=agreement]").click();
        // Кликаем по кнопке "Продолжить"
        $("button.button").click();

        // Ждем, пока появится сообщение об успехе и проверяем его текст
        $("[data-test-id=order-success]").shouldHave(
                exactText("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.")
        );
    }

    // ЗАДАЧА №2: Проверка валидации (Имя на английском - должно выдать ошибку)
    @Test
    void shouldShowErrorIfNameIsInvalid() {
        $("[data-test-id=name] input").setValue("John Doe");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $("[data-test-id=agreement]").click();
        $("button.button").click();

        // Проверяем, что у поля имени появился текст ошибки
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(
                exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")
        );
    }

    // ЗАДАЧА №2: Проверка валидации (Не поставлена галочка)
    @Test
    void shouldShowErrorIfAgreementNotChecked() {
        $("[data-test-id=name] input").setValue("Иванов Иван");
        $("[data-test-id=phone] input").setValue("+79998887766");
        // Намеренно НЕ кликаем чекбокс
        $("button.button").click();

        // Проверяем, что чекбокс подсветился красным (появился класс input_invalid)
        $("[data-test-id=agreement].input_invalid").shouldBe(Condition.visible);
    }
}