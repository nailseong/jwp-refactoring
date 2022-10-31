package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 도메인의")
class MenuTest {

    @Nested
    @DisplayName("정잭 팩토리 메서드 of는")
    class Of {

        @Test
        @DisplayName("가격이 null일 수 없다.")
        void of_priceIsNull_exception() {
            // when & then
            assertThatThrownBy(() -> Menu.of("치킨", null, 1L, Collections.emptyList()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 음수일 수 없다.")
        void of_priceIsNegative_exception() {
            // when & then
            assertThatThrownBy(() -> Menu.of("치킨", BigDecimal.valueOf(-1), 1L, Collections.emptyList()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 상품 금액의 총 합보다 클 수 없다.")
        void of_totalAmountGraterThanPrice_exception() {
            // given
            final Product chicken = getProduct(1_000);
            final Product sushi = getProduct(2_000);

            final List<MenuProduct> menuProducts = List.of(
                    new MenuProduct(1L, 1L, chicken.getPrice()),
                    new MenuProduct(2L, 2L, sushi.getPrice())
            );

            // when & then
            assertThatThrownBy(() -> Menu.of("치킨", BigDecimal.valueOf(5_001L), 1L, menuProducts))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private Product getProduct(final int price) {
            return Product.of("치킨", BigDecimal.valueOf(price));
        }
    }
}