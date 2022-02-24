package ppryvarnikov.ps1.dto;

import java.util.Objects;

public class ParcelStatus {
    private Short id;
    private String status;

    public ParcelStatus() {
    }

    public ParcelStatus(String status) {
        this.status = status;
    }

    public ParcelStatus(Short id, String status) {
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
        return "ParcelStatus{" +
                "id=" + id +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParcelStatus that = (ParcelStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
