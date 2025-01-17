package kitchenpos.application;

import static kitchenpos.table.domain.TableStatus.EMPTY;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.relay.application.OrderStatusChangedEventHandler;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/tear_down.sql")
@SpringBootTest
abstract class ServiceTest {

    protected SoftAssertions softly;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderStatusChangedEventHandler orderStatusChangedEventHandler;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        softly = new SoftAssertions();
    }

    protected Product saveProduct(final String name) {
        return saveProduct(name, BigDecimal.valueOf(10_000));
    }

    protected Product saveProduct(final String name, final BigDecimal price) {
        final Product product = Product.of(name, price);
        return productRepository.save(product);
    }

    protected MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu saveMenu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                            final MenuProduct... menuProducts) {
        final List<Product> products = Arrays.stream(menuProducts)
                .map(MenuProduct::getProductId)
                .map(it -> productRepository.getProduct(it))
                .collect(Collectors.toList());
        final MenuProductService menuProductService = MenuProductService.of(products, List.of(menuProducts));
        final Menu menu = Menu.of(name, price, menuGroup.getId(), List.of(menuProducts), menuProductService);
        return menuRepository.save(menu);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests, final boolean empty) {
        final OrderTable orderTable = OrderTable.of(numberOfGuests, empty);
        return orderTableRepository.save(orderTable);
    }

    protected OrderTable saveOrderTable(final int numberOfGuests, final boolean empty, final TableStatus tableStatus) {
        final OrderTable orderTable = new OrderTable(null, null, numberOfGuests, empty, tableStatus);
        return orderTableRepository.save(orderTable);
    }

    protected Order saveOrder(final OrderTable orderTable, final OrderStatus orderStatus,
                              final OrderLineItem... orderLineItems) {
        final Order order = new Order(
                orderTable.getId(),
                orderStatus,
                LocalDateTime.now(),
                toNewOrderLineItems(orderLineItems)
        );
        return orderRepository.save(order);
    }

    private List<OrderLineItem> toNewOrderLineItems(final OrderLineItem[] oldOrderLineItems) {
        final List<OrderLineItem> newOrderLineItems = new ArrayList<>();
        for (final OrderLineItem old : oldOrderLineItems) {
            final Menu menu = menuRepository.findById(old.getMenuId())
                    .orElseThrow();
            final OrderMenu orderMenu = OrderMenu.from(menu);
            final OrderLineItem orderLineItem = new OrderLineItem(old.getQuantity(), orderMenu);
            newOrderLineItems.add(orderLineItem);
        }
        return newOrderLineItems;
    }

    protected TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = TableGroup.from(LocalDateTime.now());
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : orderTables) {
            TableStatus tableStatus = orderTable.getTableStatus();
            if (tableStatus == null) {
                tableStatus = EMPTY;
            }
            final OrderTable saved = new OrderTable(orderTable.getId(), savedTableGroup.getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty(),
                    tableStatus);
            orderTableRepository.save(saved);
        }

        return savedTableGroup;
    }

    protected OrderTable getOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow();
    }
}
