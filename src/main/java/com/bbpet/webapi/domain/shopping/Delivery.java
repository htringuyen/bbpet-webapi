package com.bbpet.webapi.domain.shopping;

import com.bbpet.webapi.domain.Staging;
import com.bbpet.webapi.services.shopping.IntCount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@NamedNativeQueries({
        @NamedNativeQuery(name = "getDeliverablesToProcess", query = """
                EXEC getDeliverablesToProcess :fromTime, :toTime, :fromPrice, :toPrice, :searchColumn,
                                            :searchValue, :sortColumn, :sortOrder, :rowsOffset, :rowsFetch
                """, resultSetMapping = "deliverableMapping"),
})

@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getDeliverablesToProcess",
                procedureName = "getDeliverablesToProcess",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fromTime", type = LocalDateTime.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "toTime", type = LocalDateTime.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fromPrice", type = Double.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "toPrice", type = Double.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "searchColumn", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "searchValue", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "sortColumn", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "sortOrder", type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "rowsOffset", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN, name = "rowsFetch", type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "totalRows", type = Integer.class)
                }
                //, resultClasses = {IntCount.class, Deliverable.class}
                , resultSetMappings = {"deliverableMapping"}
        )
})


@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "intCountMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = IntCount.class,
                                columns = {
                                        @ColumnResult(name = "totalElements", type = Integer.class),
                                }
                        )
                }
        ),


        @SqlResultSetMapping(
                name = "deliverableMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = Deliverable.class,
                                columns = {
                                        @ColumnResult(name = "orderId", type = Long.class),
                                        @ColumnResult(name = "itemType", type = String.class),
                                        @ColumnResult(name = "orderItemIds", type = String.class),
                                        @ColumnResult(name = "totalPrice", type = Double.class),
                                        @ColumnResult(name = "createdTime", type = LocalDateTime.class),
                                        @ColumnResult(name = "customerName", type = String.class),
                                        @ColumnResult(name = "address", type = String.class),
                                        @ColumnResult(name = "phoneNumber", type = String.class),
                                        @ColumnResult(name = "deliveryStatus", type = String.class),
                                        @ColumnResult(name = "sourceLocation", type = String.class),
                                }
                        )
                }
        )
})


@Data
@Entity
@Table(name = "Delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @NotNull
    @Column(name = "startTime")
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "endTime")
    private LocalDateTime endTime;

    @Column(name = "reason")
    private String reason;


    /**
     * Status of Delivery and the flow of status
     */
    public enum Status implements Staging<Status> {

        PENDING(1, false, true),

        IN_PROGRESS(2, false, true),

        SUCCESS(3, true, true),

        FAILED(3, true, false)
        ;

        private final int order;

        private final boolean terminated;

        private final boolean prioritized;

        Status(int order, boolean terminated, boolean prioritized) {
            this.order = order;
            this.terminated = terminated;
            this.prioritized = prioritized;
        }


        @Override
        public int getOrder() {
            return order;
        }

        @Override
        public boolean isTerminated() {
            return terminated;
        }

        @Override
        public boolean isPrioritized() {
            return prioritized;
        }

        @Override
        public List<Status> getStages() {
            return Arrays.asList(values());
        }
    }

}


































