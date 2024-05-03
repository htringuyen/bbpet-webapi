package com.bbpet.domain.order;

import com.bbpet.domain.customer.Customer;
import com.bbpet.domain.employee.Employee;
import com.bbpet.domain.orderitem.OrderItem;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@SqlResultSetMapping(
        name = "orderViewResult",
        classes = {
                @ConstructorResult(
                        targetClass = OrderView.class,
                        columns = {
                                @ColumnResult(name = "orderId", type = Long.class),
                                @ColumnResult(name = "status", type = String.class),
                                @ColumnResult(name = "totalPrice", type = Double.class),
                                @ColumnResult(name = "createdTime", type = LocalDateTime.class),
                                @ColumnResult(name = "customerName", type = String.class),
                                @ColumnResult(name = "deliveryAddress", type = String.class)
                        }
                )
        }
)

@Data
@ToString
@Entity
@Table(name = "[Order]")
public class Order {

    @Id
    @Column(name = "id")
    protected Long id;

    @Column(name = "createdTime")
    protected LocalDateTime createdTime;

    @Column(name = "deliveryAddress")
    protected String deliveryAddress;

    @Column(name = "status")
    protected String status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    protected Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmedByEmployeeId")
    protected Employee employee;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    protected List<OrderItem> orderItems;
}


























