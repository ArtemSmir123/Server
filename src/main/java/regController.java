import objects.Regist;

import java.util.*;

public class regController extends Thread{
    protected static HashMap<String,Regist> arrayList = new HashMap<>();
    public void run() {
        while (true){
            Date curTime = new Date();
            if (!arrayList.isEmpty()) {
                for (String key : arrayList.keySet()) {
                    if (curTime.getTime() - arrayList.get(key).getRegTime().getTime() > 120000) {
                        arrayList.remove(key);
                    }
                }
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {

            }
            System.out.println(arrayList);
        }
    }
}
