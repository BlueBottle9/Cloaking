package bluescreen9.minecraft.bukkit.cloaking;

import java.util.HashMap;

public class Timer extends Thread{
	protected long time;
	protected long totalTime;
	private Runnable task;
	private HashMap<Long, Runnable> TimeTask = new HashMap<Long, Runnable>();
	private boolean cancelled = false;
	private Runnable tickTask;
			@Override
			public void run() {
					while(!cancelled) {
						time -= 1;
						tickTask.run();
						if (time <= 0) {
							task.run();
							return;
						}
						for (long t:TimeTask.keySet()) {
							if (t == time) {
								TimeTask.get(t).run();
							} 
						}
						try {
							sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			}
			
			public Timer(long seconds,Runnable task) {
				time = seconds;
				this.task = task;
				this.totalTime = seconds;
			}
			
			public int getTime() {
				return (int) (time /1000);
			}
			
			public void addTask(long time,Runnable task) {
					TimeTask.put(time, task);
			}
			
			public void cancel() {
				cancelled = true;
			}
			
			public void setTickTask(Runnable task) {
					this.tickTask = task;
			}
}
