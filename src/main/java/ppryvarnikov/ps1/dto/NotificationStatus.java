package ppryvarnikov.ps1.dto;

import java.util.Objects;

public class NotificationStatus {
    private Short id;
    private String status;

    public NotificationStatus() {
    }

    public NotificationStatus(String status) {
        this.status = status;
    }

    public NotificationStatus(Short id, String status) {
        this(status);
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "NotificationStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationStatus that = (NotificationStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
