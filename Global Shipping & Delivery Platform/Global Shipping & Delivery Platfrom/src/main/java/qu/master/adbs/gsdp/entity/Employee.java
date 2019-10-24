package qu.master.adbs.gsdp.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("E")
public class Employee extends Account {
	
	
}