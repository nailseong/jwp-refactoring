package kitchenpos.application;

import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.ui.dto.request.MenuGroupCreateRequest;
import kitchenpos.ui.dto.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        final MenuGroup menuGroup = new MenuGroup(request.getName());
        final MenuGroup saved = menuGroupRepository.save(menuGroup);
        return new MenuGroupResponse(saved.getId(), saved.getName());
    }

    public List<MenuGroupResponse> list() {
        return stream(menuGroupRepository.findAll().spliterator(), false)
                .map(it -> new MenuGroupResponse(it.getId(), it.getName()))
                .collect(Collectors.toList());
    }
}
