package com.chunkmapper;

public class TestTasker extends Tasker {

	public TestTasker() {
		super(1);
	}

	@Override
	protected void doTask(Point p) throws Exception {
		System.out.println(p);
	}
	protected Point getTask() {
		Point playerPosition = new Point(0, 0); 
		synchronized(lock) {
			Point chosenPoint = null;
			double bestDistance = Double.MAX_VALUE;
			for (Point candidatePoint : taskQueue) {
				Point candidatePointPosition = new Point(candidatePoint.x * 512, candidatePoint.z * 512);
				double currentDistance = candidatePointPosition.distance(playerPosition);
				if (currentDistance < bestDistance) {
					chosenPoint = candidatePoint;
					bestDistance = currentDistance;
				}
			}
			if (chosenPoint != null) {
				taskQueue.remove(chosenPoint);
			}
			return chosenPoint;
		}
	}
	public static void main(String[] args) {
		TestTasker tasker = new TestTasker();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				tasker.addTask(i, j);
			}
		}
		tasker.shutdown();
	}

}
