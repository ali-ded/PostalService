package ppryvarnikov.ps1.dto;

import java.util.Objects;

public class Notification {
    private Long id;
    private String message;
    private NotificationStatus notificationStatus;

    public Notification() {
    }

    public Notification(String message, NotificationStatus notificationStatus) {
        this.message = message;
        this.notificationStatus = notificationStatus;
    }

    public Notification(Long id, String message, NotificationStatus notificationStatus) {
        this(message, notificationStatus);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", notificationStatus=" + notificationStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(message, that.message) &&
                Objects.equals(notificationStatus, that.notificationStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
