package test.application.demo.dto;

public enum RequestStatus {
    DRAFT("DRAFT"), SENT("SENT"), ACCEPTED("ACCEPTED"), REJECTED("REJECTED");
    private String value;
    RequestStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
