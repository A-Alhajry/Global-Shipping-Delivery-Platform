package qu.master.adbs.gsdp.adapters;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SqlDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
	
	public Timestamp convertToDatabaseColumn(LocalDateTime localTime) {
		return localTime == null ? null : Timestamp.valueOf(localTime);
	}
	
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return timestamp == null ? null : timestamp.toLocalDateTime();
	}
}
