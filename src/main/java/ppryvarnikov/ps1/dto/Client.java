package ppryvarnikov.ps1.dto;

import java.util.Objects;

public class Client {
    private Long id;
    private String surname;
    private String firstName;
    private String patronymic;
    private String email;
    private Long phoneNumber;

    public Client() {
    }

    public Client(String surname, String firstName, String patronymic, String email, Long phoneNumber) {
        this.surname = surname;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Client(Long id, String surname, String firstName, String patronymic, String email, Long phoneNumber) {
        this(surname, firstName, patronymic, email, phoneNumber);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", surname='" + surname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(surname, client.surname) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(patronymic, client.patronymic) &&
                Objects.equals(email, client.email) &&
                Objects.equals(phoneNumber, client.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
