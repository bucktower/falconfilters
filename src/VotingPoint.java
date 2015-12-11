
public class VotingPoint {

	private int x, y;
	private int whichBin = -1;
	
	public VotingPoint(int myX, int myY) {
		x = myX;
		y = myY;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public int getWhichBin() {
		return whichBin;
	}

	public void setWhichBin(int whichBin) {
		this.whichBin = whichBin;
	}
	
	public int distanceSquared(int x1, int y1) {
		return (int) ((Math.pow(x-x1,2))+(Math.pow(y-y1,2)));
	}

	@Override
	public String toString() {
		return "VotingPoint [x=" + x + ", y=" + y + ", whichBin=" + whichBin + "]";
	}
}
