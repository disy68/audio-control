package hu.diskay.audiocontrol.service.db;

import static java.util.Objects.isNull;

import hu.diskay.audiocontrol.controller.response.VolumeInformation;
import hu.diskay.audiocontrol.service.VolumeStore;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DbVolumeStore implements VolumeStore {

    private static final String UPSERT_INFO_QUERY =
        "MERGE INTO volume v USING "
            + " (VALUES '%s', %d, %d) temp (device_name, volume_value, muted) "
            + " ON temp.device_name = v.device_name "
            + " WHEN MATCHED THEN "
            + " UPDATE "
            + "  set device_name=temp.device_name, "
            + "  volume_value=temp.volume_value,"
            + "  muted=temp.muted "
            + " WHEN NOT MATCHED THEN "
            + " INSERT "
            + "  (device_name, volume_value, muted) "
            + " VALUES "
            + "  (temp.device_name, temp.volume_value, temp.muted) ";

    private static final String INFO_QUERY =
        "select "
            + " device_name, "
            + " volume_value, "
            + " muted"
            + "  from volume "
            + " where device_name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final VolumeStoryEntryExtractor extractor;

    public DbVolumeStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = new VolumeStoryEntryExtractor();
    }

    @Override
    public void put(String deviceName, VolumeInformation volumeInformation) {
        int muted = volumeInformation.isMuted() ? 1 : 0;
        jdbcTemplate.update(String.format(UPSERT_INFO_QUERY,
            deviceName,
            volumeInformation.getVolume(),
            muted));
    }

    @Override
    public VolumeInformation get(String deviceName) {

        VolumeStoreEntry entry = jdbcTemplate.query(
            INFO_QUERY,
            new Object[]{deviceName},
            extractor);

        if (isNull(entry)) {
            return getDefaultInformation();
        }

        return new VolumeInformation(entry.getVolumeValue(), entry.isMuted());
    }

    private VolumeInformation getDefaultInformation() {
        return new VolumeInformation(DEFAULT_VALUE, DEFAULT_MUTED_STATUS);
    }

    private class VolumeStoryEntryExtractor implements ResultSetExtractor<VolumeStoreEntry> {

        @Override
        public VolumeStoreEntry extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return new VolumeStoreEntry(
                    rs.getString("device_name"),
                    rs.getInt("volume_value"),
                    rs.getBoolean("muted"));
            }

            return null;
        }
    }
}
