package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MenuGroupService의")
class MenuGroupServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("메뉴 그룹을 생성한다.")
        void create_validMenuGroup_success() {
            // given
            final String name = "반반치킨";
            final MenuGroupCreateRequest request = new MenuGroupCreateRequest(name);

            // when
            final MenuGroupResponse actual = menuGroupService.create(request);

            // then
            softly.assertThat(actual.getName()).isEqualTo(name);
            softly.assertThat(actual.getId()).isNotNull()
                    .isPositive();
            softly.assertAll();
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("메뉴 그룹 목록을 조회한다.")
        void list_savedMenuGroup_success() {
            // given
            final String name1 = "진돌이 한 마리 치킨";
            final String name2 = "진돌이 두 마리 치킨";
            final String name3 = "진돌이 세 마리 치킨";

            saveMenuGroup(name1);
            saveMenuGroup(name2);
            saveMenuGroup(name3);

            // when
            final List<MenuGroupResponse> actual = menuGroupService.list();

            // then
            assertThat(actual).extracting(MenuGroupResponse::getName)
                    .containsExactly(name1, name2, name3);
        }
    }
}
