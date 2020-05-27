package dev.drugowick.algaworks.algafoodapi.domain.model;

import dev.drugowick.algaworks.algafoodapi.domain.validation.IfFreeDeliverySubtotalEqualsTotal;
import dev.drugowick.algaworks.algafoodapi.domain.validation.ValidationGroups;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

// A custom annotation that validates if subtotal equals total when deliveryFee equals 0.
@IfFreeDeliverySubtotalEqualsTotal(fieldDeliveryFee = "deliveryFee", fieldSubtotal = "subtotal", fieldTotal = "total")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity(name = "order_")
public class Order {

    @NotNull(groups = ValidationGroups.OrderId.class)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column is included to exemplify its use, but it's not
     * recommended here since the database will be generated
     * via script and this is not a validation for the object,
     * only a constraint on the database.
     * <p>
     * It's used, though, if you want to re-generate the ddl from JPA entities.
     */

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal subtotal;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal total;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime", updatable = false)
    private OffsetDateTime createdDate;

    @Column(nullable = true, columnDefinition = "datetime")
    private OffsetDateTime confirmationDate;

    @Column(nullable = true, columnDefinition = "datetime")
    private OffsetDateTime cancellationDate;

    @Column(nullable = true, columnDefinition = "datetime")
    private OffsetDateTime deliveryDate;

    @NotNull
    @Valid
    @ConvertGroup(from = Default.class, to = ValidationGroups.PaymentMethodId.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull
    @Valid
    @ConvertGroup(from = Default.class, to = ValidationGroups.RestaurantId.class)
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @NotNull
    @Valid
    @ConvertGroup(from = Default.class, to = ValidationGroups.UserId.class)
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private Address deliveryAddress;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;
}
