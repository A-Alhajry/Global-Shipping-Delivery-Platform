package qu.master.adbs.gsdp.adapters;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SqlDateConverter implements AttributeConverter<LocalDate, Date> {
	
	public Date convertToDatabaseColumn(LocalDate localDate) {
		return localDate == null ? null : Date.valueOf(localDate);
	}
	
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		return sqlDate == null ? null : sqlDate.toLocalDate();
	}
}
