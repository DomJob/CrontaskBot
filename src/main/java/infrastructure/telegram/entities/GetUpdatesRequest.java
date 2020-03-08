package infrastructure.telegram.entities;

public class GetUpdatesRequest {
    public long offset;

    public GetUpdatesRequest(long offset) {
        this.offset = offset;
    }
}
