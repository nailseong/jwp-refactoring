package kitchenpos.menugroup.domain;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.repository.CrudRepository;

public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {

    @Override
    List<MenuGroup> findAll();

    @Override
    List<MenuGroup> findAllById(Iterable<Long> menuGroupIds);
}
