package tripway.server.repository;

import java.sql.Timestamp;

public interface PointDtoProjection {
    Long getId();

    String getName();

    String getDescription();

    Timestamp getUpdated();

    String getUsername();

    String getUserId();

    Long getTripId();
}
