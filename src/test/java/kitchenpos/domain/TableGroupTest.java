package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단체 지정 도메인의")
class TableGroupTest {

    @Nested
    @DisplayName("addOrderTables 메서드는")
    class AddOrderTables {

        @Test
        @DisplayName("주문 테이블이 null일 수 없다.")
        void addOrderTables_orderTablesAreNull_exception() {
            // given
            final TableGroup tableGroup = getTableGroup();

            // when & then
            assertThatThrownBy(() -> tableGroup.addOrderTables(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블이 2개 미만 일 수 없다.")
        void addOrderTables_orderTableSizeLessThenTwo_exception() {
            // given
            final TableGroup tableGroup = getTableGroup();
            final List<OrderTable> orderTables = List.of(
                    getOrderTable(1)
            );

            // when & then
            assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("중복된 주문 테이블이 존재할 수 없다.")
        void addOrderTables_orderTableIsDuplicate_exception() {
            // given
            final TableGroup tableGroup = getTableGroup();
            final List<OrderTable> orderTables = List.of(
                    getOrderTable(1),
                    getOrderTable(1)
            );

            // when & then
            assertThatThrownBy(() -> tableGroup.addOrderTables(orderTables))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private TableGroup getTableGroup() {
            return TableGroup.from(LocalDateTime.now());
        }

        private OrderTable getOrderTable(final int id) {
            return new OrderTable((long) id, null, 1, true);
        }
    }
}
