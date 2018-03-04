package main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.PaymentController;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ac.at.tuwien.sepm.QSE15.entity.creditCard.CreditCard;
import main.java.ac.at.tuwien.sepm.QSE15.entity.payment.Payment;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.Reservation;
import main.java.ac.at.tuwien.sepm.QSE15.entity.reservation.RoomReservation;
import main.java.ac.at.tuwien.sepm.QSE15.gui.controllers.InvoiceController;
import main.java.ac.at.tuwien.sepm.QSE15.service.ServiceException;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardPayment.CreditCardPaymentService;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService.CreditCardService;
import main.java.ac.at.tuwien.sepm.QSE15.service.creditCardService.CreditCardServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.paymentService.PaymentServiceIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.service.roomReservationService.RoomReservationIMPL;
import main.java.ac.at.tuwien.sepm.QSE15.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
/**
 * Created by Luka Veljkovic on 6/2/17.
 */
@ComponentScan("main.java.ac.at.tuwien.sepm.QSE15")
public class PaymentController {
    public TextField amountTextField;
    public SplitMenuButton methodSplitMenuButton;
    public Button payButton;
    public Button cancelButton;
    public TextField reservationIdTextField;
    public TextField totalPriceTextField;
    public TextField balanceTextField;
    private AnnotationConfigApplicationContext context;
    private Reservation reservation;
    private PaymentServiceIMPL paymentServiceIMPL;
    private CreditCardPaymentService creditCardPaymentService;
    private CreditCardServiceIMPL creditCardServiceIMPL;
    private Stage currentStage;
    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);
    public void initialize() {
        context = new AnnotationConfigApplicationContext(this.getClass());
        paymentServiceIMPL = context.getBean(PaymentServiceIMPL.class);
        creditCardPaymentService = context.getBean(CreditCardPaymentService.class);
        creditCardServiceIMPL = context.getBean(CreditCardServiceIMPL.class);
    }
    public void payButtonClicked() {
        Long amount = 0L;
        Payment payment = new Payment();
        if (amountTextField.getText().matches("[0-9]+") && amountTextField.getText() != null) {
            amount = Long.valueOf(amountTextField.getText()) * 100;
        } else {
            Utility.showAlert("Entered value is not an integer number.", Alert.AlertType.ERROR);
            LOGGER.error("Entered value is not an integer number. Error in payment controller.");
        }
        payment.setPaidAmount(amount);
        payment.setReservation(reservation.getRid());
        payment.setDate(java.sql.Date.valueOf(LocalDate.now()));
        if (!reservation.getPaid()) {
            if (methodSplitMenuButton.getText().equals("Cash")) {
                payment.setPaymentMethod("Cash payment");
                try {
                    paymentServiceIMPL.create(payment);
                    paymentServiceIMPL.getPaidDifference(reservation);
                    if (reservation.getNeedsTObePaid() <= 0){
                        reservation.setPaid(true);
                        paymentServiceIMPL.setPaid(reservation);
                        long modulo = reservation.getNeedsTObePaid() % 100;
                        modulo = -modulo;
                        Utility.showAlert("The change is: " + -(Long.parseLong(balanceTextField.getText()) - (amount / 100)) + "," + modulo + " €.", Alert.AlertType.INFORMATION);
                        reservation.setNeedsTObePaid(0L);
                        this.currentStage.close();
                    }else {
                        paymentServiceIMPL.getPaidDifference(reservation);
                        balanceTextField.setText(reservation.getNeedsTObePaid()/100 + "");
                        amountTextField.setText("");
                        Utility.showAlert("Payment successful! New Balance: " + (reservation.getNeedsTObePaid()/100)+ " €.", Alert.AlertType.INFORMATION);
                    }
                } catch (ServiceException e) {
                    LOGGER.error("Error in PaymentController.");
                }
            } else {
                payment.setPaymentMethod("Credit Card");
                try {
                    CreditCard creditCard = new CreditCard();
                    creditCard.setCnr("4242424242424242");
                    creditCard.setExpMonth(6);
                    creditCard.setExpYear(2018);
                    creditCard.setCvv("314");

                    if (creditCardPaymentService.payAmountWithGivenCreditCard(creditCard,payment.getPaidAmount(), "Paid with " + creditCard.getCardType())) {
                        paymentServiceIMPL.create(payment);
                        paymentServiceIMPL.getPaidDifference(reservation);
                        if (reservation.getNeedsTObePaid() <= 0){
                            reservation.setPaid(true);
                            paymentServiceIMPL.setPaid(reservation);
                            long modulo = reservation.getNeedsTObePaid() % 100;
                            modulo = -modulo;
                            Utility.showAlert("The change is: " + -(Long.parseLong(balanceTextField.getText()) - (amount / 100)) + "," + modulo + " €.", Alert.AlertType.INFORMATION);
                            this.currentStage.close();

                            reservation.setNeedsTObePaid(0L);

                        }else {
                            paymentServiceIMPL.getPaidDifference(reservation);
                            balanceTextField.setText(reservation.getNeedsTObePaid()/100 + "");
                            amountTextField.setText("");
                            Utility.showAlert("Payment successful! New Balance: " + (reservation.getNeedsTObePaid()/100)+ " €.", Alert.AlertType.INFORMATION);
                        }
                    }else {
                        Utility.showAlert("CreditCard Error! \n Payment unsuccessful!", Alert.AlertType.ERROR);
                    }
                } catch (ServiceException e) {
                    LOGGER.error("Error at SQL PaymentController!");
                }
            }
        }
    }
    public void setReservation(Reservation reservation){
        try {
            paymentServiceIMPL.getPaidDifference(reservation);
        } catch (ServiceException e) {
            LOGGER.error("Error in setReservation!");
        }
        this.reservation = reservation;
        reservationIdTextField.setText(reservation.getRid() + "");
        totalPriceTextField.setText(reservation.getTotal()/100 + "");
        balanceTextField.setText(reservation.getNeedsTObePaid()/100 + "");
    }
    public void cancelButtonClicked() {
        this.currentStage.close();
    }
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        currentStage.setResizable(false);
    }
    public void cashOnAction(ActionEvent actionEvent) {
        methodSplitMenuButton.setText("Cash");
    }
    public void cardOnAction(ActionEvent actionEvent) {
        methodSplitMenuButton.setText("Card");
    }
}