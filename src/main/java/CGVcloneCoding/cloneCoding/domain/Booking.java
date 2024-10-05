package CGVcloneCoding.cloneCoding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ticketType;
    private int ticketPrice;

    @ManyToOne
    @JoinColumn(name = "screening_id")
    private Screening screening;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="payment_id")
    private Payment payment;

    public Booking(Screening screening, Seat seat, User user, String ticketType, int ticketPrice, Payment payment) {
        this.screening = screening;
        this.seat = seat;
        this.user = user;
        this.ticketType = ticketType;
        this.ticketPrice = ticketPrice;
        this.payment = payment;
    }




}
