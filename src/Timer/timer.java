package Timer;

public class timer {
	private int speed=8;//�ٶ�
	public void changeSpeedSlow()
	{
		speed=8;
	}
	public void changeSpeedFast()
	{
		speed=3;
	}
	public void changeSpeedHyper()
	{
		speed=1;
	}
	
	public int GetSpeed() {
		return speed;
	}
	
	public void SetSpeed(int s) {
		speed=s;
	}
}
