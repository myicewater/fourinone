

import com.fourinone.MigrantWorker;
import com.fourinone.WareHouse;

public class SimpleWorker extends MigrantWorker {

	
	public WareHouse doTask(WareHouse w){
		WareHouse r = new WareHouse();
		String value = w.getString("action");
		String obj = "World";
		System.out.println(value+ obj);
		r.put("obj", obj);
		return r;
	}
	
	public static void main(String[] args) {
		SimpleWorker  s = new SimpleWorker();
		
		s.waitWorking("helloWorker");
	}
}
