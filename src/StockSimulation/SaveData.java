package StockSimulation;

import java.io.Serializable;
import java.util.Date;

public class SaveData implements Serializable {

	private static final long serialVersionUID = -7356613436090488064L;
	protected Date currentDate;
	protected int dateId;
	protected String eventStr;
	
	public SaveData(Date currentDate, int dateId, String eventStr) {
		this.currentDate = currentDate;
		this.dateId = dateId;
		this.eventStr = eventStr;
	} //SaveData()
	
} //SaveData class
