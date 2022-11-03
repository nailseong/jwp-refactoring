package kitchenpos.table.ui.dto;

public class ChangeNumberOfGuestsRequest {

    private int numberOfGuests;

    private ChangeNumberOfGuestsRequest() {
    }

    public ChangeNumberOfGuestsRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
