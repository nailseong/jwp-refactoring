package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
import kitchenpos.ui.dto.response.OrderResponse;
import kitchenpos.ui.dto.response.OrderResponse.OrderLineItemResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderCreateRequest request) {

        if (CollectionUtils.isEmpty(request.getOrderLineItems())) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = request.getOrderLineItems()
                .stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        final List<OrderLineItem> orderLineItems = request.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItem(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());
        final Order order = new Order(orderTable.getId(),
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems);
        final Order savedOrder = orderRepository.save(order);
        final Long orderId = savedOrder.getId();
        final List<OrderLineItemResponse> orderLineItemResponses = savedOrder.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), orderId, it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new OrderResponse(savedOrder.getId(), savedOrder.getOrderTableId(),
                savedOrder.getOrderStatus(),
                savedOrder.getOrderedTime(), orderLineItemResponses);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        final List<OrderResponse> responses = new ArrayList<>();
        for (final Order order : orders) {
            final List<OrderLineItemResponse> orderLineItemResponses = order.getOrderLineItems()
                    .stream()
                    .map(it -> new OrderLineItemResponse(it.getSeq(), order.getId(), it.getMenuId(), it.getQuantity()))
                    .collect(Collectors.toList());
            final OrderResponse orderResponse = new OrderResponse(order.getId(), order.getOrderTableId(),
                    order.getOrderStatus(), order.getOrderedTime(), orderLineItemResponses);
            responses.add(orderResponse);
        }
        return responses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final ChangeOrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        final Order updatedOrder = savedOrder.changeOrderStatus(orderStatus.name());

        orderRepository.save(updatedOrder);

        final List<OrderLineItemResponse> orderLineItemResponses = updatedOrder.getOrderLineItems()
                .stream()
                .map(it -> new OrderLineItemResponse(it.getSeq(), updatedOrder.getId(), it.getMenuId(),
                        it.getQuantity()))
                .collect(Collectors.toList());
        return new OrderResponse(updatedOrder.getId(), updatedOrder.getOrderTableId(), updatedOrder.getOrderStatus(),
                updatedOrder.getOrderedTime(), orderLineItemResponses);
    }
}
