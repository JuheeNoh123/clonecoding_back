package CGVcloneCoding.cloneCoding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Payment {
    @Id
    private String id;

    @Column(nullable = false)
    private int paymentAmount;    //결제 금액

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime paymentTime;

    private String paymentType;
    private String easyPaymentType;


    public Payment(String id, int paymentAmount, User user, String paymentType, String easyPaymentType) {
        this.id = id;
        this.paymentAmount = paymentAmount;
        this.user = user;
        this.paymentTime = LocalDateTime.now();
        this.paymentType = paymentType;
        this.easyPaymentType = easyPaymentType;
    }
}
