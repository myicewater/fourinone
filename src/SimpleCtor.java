

import java.util.List;

import com.fourinone.Contractor;
import com.fourinone.WareHouse;
import com.fourinone.Worker;
import com.fourinone.WorkerLocal;

public class SimpleCtor extends Contractor {

	@Override
	public WareHouse giveTask(WareHouse arg0) {
		
		
		WorkerLocal[] works = getWaitingWorkers("helloWorker");
		
		if(works.length > 0){
			WareHouse r =works[0].doTask(arg0);
			while(true){
				if(r.getStatus() == WareHouse.READY){
					System.out.println(r.getString("obj"));
					break;
				}
			}
			System.out.println("game over!");
		}
		return null;
	}
	
	public static void main(String[] args) {
		SimpleCtor c = new SimpleCtor();
		WareHouse w = new WareHouse();
		w.put("action", "hello");
		c.giveTask(w);
		System.out.println("main end.");
	}

}
