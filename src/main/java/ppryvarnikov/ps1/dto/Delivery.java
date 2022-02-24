package ppryvarnikov.ps1.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class Delivery {
    private Long id;
    private Client sender;
    private Department departmentSender;
    private Department departmentRecipient;
    private Long phoneRecipient;
    private String surnameRecipient;
    private String firstNameRecipient;
    private String patronymicRecipient;
    private ParcelStatus parcelStatus;
    private Timestamp dateTimeCreation;
    private Timestamp dateTimeStatusChange;

    public Delivery() {
    }

    public Delivery(Client sender,
                    Department departmentSender,
                    Department departmentRecipient,
                    Long phoneRecipient,
                    String surnameRecipient,
                    String firstNameRecipient,
                    String patronymicRecipient,
                    ParcelStatus parcelStatus,
                    Timestamp dateTimeCreation,
                    Timestamp dateTimeStatusChange) {
        this.sender = sender;
        this.departmentSender = departmentSender;
        this.departmentRecipient = departmentRecipient;
        this.phoneRecipient = phoneRecipient;
        this.surnameRecipient = surnameRecipient;
        this.firstNameRecipient = firstNameRecipient;
        this.patronymicRecipient = patronymicRecipient;
        this.parcelStatus = parcelStatus;
        this.dateTimeCreation = dateTimeCreation;
        this.dateTimeStatusChange = dateTimeStatusChange;
    }

    public Delivery(Long id,
                    Client sender,
                    Department departmentSender,
                    Department departmentRecipient,
                    Long phoneRecipient,
                    String surnameRecipient,
                    String firstNameRecipient,
                    String patronymicRecipient,
                    ParcelStatus parcelStatus,
                    Timestamp dateTimeCreation,
                    Timestamp dateTimeStatusChange) {
        this(sender,
                departmentSender,
                departmentRecipient,
                phoneRecipient,
                surnameRecipient,
                firstNameRecipient,
                patronymicRecipient,
                parcelStatus,
                dateTimeCreation,
                dateTimeStatusChange);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getSender() {
        return sender;
    }

    public void setSender(Client sender) {
        this.sender = sender;
    }

    public Department getDepartmentSender() {
        return departmentSender;
    }

    public void setDepartmentSender(Department departmentSender) {
        this.departmentSender = departmentSender;
    }

    public Department getDepartmentRecipient() {
        return departmentRecipient;
    }

    public void setDepartmentRecipient(Department departmentRecipient) {
        this.departmentRecipient = departmentRecipient;
    }

    public Long getPhoneRecipient() {
        return phoneRecipient;
    }

    public void setPhoneRecipient(Long phoneRecipient) {
        this.phoneRecipient = phoneRecipient;
    }

    public String getSurnameRecipient() {
        return surnameRecipient;
    }

    public void setSurnameRecipient(String surnameRecipient) {
        this.surnameRecipient = surnameRecipient;
    }

    public String getFirstNameRecipient() {
        return firstNameRecipient;
    }

    public void setFirstNameRecipient(String firstNameRecipient) {
        this.firstNameRecipient = firstNameRecipient;
    }

    public String getPatronymicRecipient() {
        return patronymicRecipient;
    }

    public void setPatronymicRecipient(String patronymicRecipient) {
        this.patronymicRecipient = patronymicRecipient;
    }

    public ParcelStatus getParcelStatus() {
        return parcelStatus;
    }

    public void setParcelStatus(ParcelStatus parcelStatus) {
        this.parcelStatus = parcelStatus;
    }

    public Timestamp getDateTimeCreation() {
        return dateTimeCreation;
    }

    public void setDateTimeCreation(Timestamp dateTimeCreation) {
        this.dateTimeCreation = dateTimeCreation;
    }

    public Timestamp getDateTimeStatusChange() {
        return dateTimeStatusChange;
    }

    public void setDateTimeStatusChange(Timestamp dateTimeStatusChange) {
        this.dateTimeStatusChange = dateTimeStatusChange;
    }

    @Override
    public String toString() {
        return "Delivery{" +
                "id=" + id +
                ", sender=" + sender +
                ", departmentSender=" + departmentSender +
                ", departmentRecipient=" + departmentRecipient +
                ", phoneRecipient=" + phoneRecipient +
                ", surnameRecipient='" + surnameRecipient + '\'' +
                ", firstNameRecipient='" + firstNameRecipient + '\'' +
                ", patronymicRecipient='" + patronymicRecipient + '\'' +
                ", parcelStatus=" + parcelStatus +
                ", dateTimeCreation=" + dateTimeCreation +
                ", dateTimeStatusChange=" + dateTimeStatusChange +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(id, delivery.id) &&
                Objects.equals(sender, delivery.sender) &&
                Objects.equals(departmentSender, delivery.departmentSender) &&
                Objects.equals(departmentRecipient, delivery.departmentRecipient) &&
                Objects.equals(phoneRecipient, delivery.phoneRecipient) &&
                Objects.equals(surnameRecipient, delivery.surnameRecipient) &&
                Objects.equals(firstNameRecipient, delivery.firstNameRecipient) &&
                Objects.equals(patronymicRecipient, delivery.patronymicRecipient) &&
                Objects.equals(parcelStatus, delivery.parcelStatus) &&
                Objects.equals(dateTimeCreation, delivery.dateTimeCreation) &&
                Objects.equals(dateTimeStatusChange, delivery.dateTimeStatusChange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
