package infrastructure.eventhandler;

import java.util.List;

public interface UpdateFetcher {
    List<Update> getUpdates(long offset);
}
