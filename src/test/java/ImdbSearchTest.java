import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ImdbSearchTest {
    @BeforeEach
    void browserSetUp() {
        open("https://www.imdb.com/");
        Configuration.pageLoadStrategy = "eager";
    }

    @ParameterizedTest(name = "Для поискового запроса {0} должен отдавать не пустой список фильмов")
    @DisplayName("TC_001: Проверка не пустого вывода страницы при поиске фильмов")
    @ValueSource(strings = {
            "Интерстеллар",
            "Начало",
    })
    void searchWithValueSourceShouldReturnResults(String movieTitle) {
        $("#suggestion-search").setValue(movieTitle).pressEnter();

        $("section[data-testid='find-results-section-title']")
                .shouldBe(visible)
                .$$("li").shouldHave(sizeGreaterThan(0));

        $("section[data-testid='find-results-section-title'] li:first-child")
                .shouldHave(text(movieTitle));
    }

    @ParameterizedTest(name = "Для поискового запроса {0} должны быть видны и название, и год фильма")
    @DisplayName("TC_002: Проверка поиска фильма по названию и году")
    @CsvSource({
            "Интерстеллар, 2014",
            "Начало, 2010",
    })
    void searchWithYearShouldReturnCorrectMovie(String movieTitle, int year) {
        $("#suggestion-search").setValue(movieTitle + " " + year).pressEnter();

        $("section[data-testid='find-results-section-title'] li:first-child")
                .shouldHave(text(movieTitle))
                .shouldHave(text(String.valueOf(year)));
    }
    @CsvFileSource(resources = "/movie_data")
    @ParameterizedTest(name = "Для поискового запроса {0} должен быть указан год {1}")
    @DisplayName("TC_003: Проверка поиска фильма по названию и году")
    void searchResultsShouldContainExpectedFilmAndYear (String movieTitle, int year) {
        $("#suggestion-search").setValue(movieTitle + " " + year).pressEnter();

        $("section[data-testid='find-results-section-title'] li:first-child")
                .shouldHave(text(movieTitle))
                .shouldHave(text(String.valueOf(year)));
    }
}


