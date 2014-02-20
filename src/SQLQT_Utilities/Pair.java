package SQLQT_Utilities;

public class Pair<L,R> 
{
	protected L l;
	protected R r;
	
	public Pair(){};
	
	public Pair(L l, R r)
	{
		this.l = l;
		this.r = r;
	}
	
	public L getL()
	{
		return this.l;
	}
	
	public R getR()
	{
		return this.r;
	}
	
	public void setL(L l)
	{
		this.l = l;
	}
	
	public void setR(R r)
	{
		this.r = r;
	}
}
