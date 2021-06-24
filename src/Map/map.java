package Map;
import java.awt.Graphics;
import java.util.Random;
import Timer.timer;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class map extends JPanel implements Runnable{
	private final int rows;//��
	private final int columns;//��
	JLabel  record;
	public boolean diy=false;//�ж��Ƿ���diy״̬
	public boolean clean=false;//�ж��Ƿ����
	private int speed;//�ٶ�
	private int lnum;//���ϸ����
	private static int shape[][]=new int [40][50];//��ͼ�����Ĳ���
	private static int zero[][]=new int [40][50];//��յ�ͼ�Ĳ���
	public static  int pauseshape[][]=new int [40][50];//������ͣ����
	private final CellStatus[][] generation1;
	private final CellStatus[][] generation2;
	CellStatus[][] currentGeneration=new CellStatus [40][50];//���׶�
	CellStatus[][] nextGeneration=new CellStatus [40][50];//�½׶�
	private volatile boolean isChanging = false;//�ж��Ƿ��ڱ仯
	private timer time=new timer();
	private int daishucount=1;//��¼����
	private int daishulimit=0x7fffffff;
	public map(int rows, int columns)
	{
		
		this.rows=rows;
		this.columns=columns;
		record = new JLabel();
		this.add(record);
		generation1=new CellStatus[rows][columns];
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				generation1[i][j]=CellStatus.Dead;
			}
		}
		generation2=new CellStatus[rows][columns];
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				generation2[i][j]=CellStatus.Dead;
			}
		}
		currentGeneration=generation1;
		nextGeneration=generation2;
	
		
	}
	
	public void transfrom(CellStatus[][] generation, int pauseshape[][])//��pause���鸳ֵ
	{
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				if(generation[i][j]==CellStatus.Active)
				{
					pauseshape[i][j]=1;
				}
				else if(generation[i][j]==CellStatus.Dead)
				{
					pauseshape[i][j]=0;
				}
			}
		}
	}
	public void run()
	{
		while(daishucount<=daishulimit)
		{
			synchronized(this)
			{
				while(isChanging)
				{
					
					try
					{
						this.wait();
					}catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			
			
				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<columns;j++)
					{
						evolve(i,j);
					}
				}
				//������current����ָ��next
				CellStatus[][]temp=null;
				temp=currentGeneration;
				currentGeneration=nextGeneration;
				nextGeneration=temp;
				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<columns;j++)
					{
						nextGeneration[i][j]=CellStatus.Dead;	
					}
				}
				transfrom(currentGeneration,pauseshape);
				daishucount++;
				repaint();//����current�������»���
				updateNumber();
				speed=time.GetSpeed();
     			sleep(speed);
			}
		}
	}
	public void updateNumber()
	{
		String s = " ��������� " + lnum ;
		record.setText(s);
	}
	public void changeSpeedSlow()
	{
		time.changeSpeedSlow();
		speed=time.GetSpeed();
	}
	public void changeSpeedFast()
	{time.changeSpeedFast();
		speed=time.GetSpeed();
	}
	public void changeSpeedHyper()
	{time.changeSpeedHyper();
		speed=time.GetSpeed();
	}
	
	
	public void paintComponent(Graphics g)//������άͼ,���ο����ϸ��
	{
		lnum=0;
		super.paintComponent(g);
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				if(currentGeneration[i][j]==CellStatus.Active)
				{
					g.fillRect(j*20, i*20, 20, 20);//������
					lnum++;
				}
				else
				{
					g.drawRect(j*20, i*20, 20, 20);//�����α߿�
				}
			}
		}
	}

	public void setShape()
	{
		setShape(shape);
	}
	public void setRandom()
	{
		Random a=new Random();
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				shape[i][j]=Math.abs(a.nextInt(2));
				pauseshape[i][j]=shape[i][j];
			}
		}
		setShapetemp(shape);
	}
	public void setZero()
	{
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				zero[i][j]=0;
			}
		}
	}
	public void setStop()//���
	{
		setZero();		
		shape=zero;
		setShape(shape);
		pauseshape=shape;
	}
	
	public void setPause()//��ͣ
	{
		shape=pauseshape;
		setShapetemp(pauseshape);
	}
	
	public void setDiy()
	{
		shape=pauseshape;
		setShapetemp(shape);
	}
	private void setShapetemp(int [][]shape)//���õ�ͼ�����ı�
	{
		isChanging=true;
		int arrowsRows=shape.length;
		int arrowsColumns=shape[0].length;
		int minimumRows=(arrowsRows<rows)?arrowsRows:rows;
		int minimunColumns=(arrowsColumns<columns)?arrowsColumns:columns;
		synchronized(this)
		{
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<columns;j++)
				{
					currentGeneration[i][j]=CellStatus.Dead;
				}
			}
			for(int i=0;i<minimumRows;i++)
			{
				for(int j=0;j<minimunColumns;j++)
				{
					if(shape[i][j]==1)
					{
						currentGeneration[i][j]=CellStatus.Active;
					}
				}
			}
			repaint();
			updateNumber();

		}
	}
	private void setShape(int [][]shape)//���õ�ͼ���ɸı�
	{
		isChanging=true;
		int arrowsRows=shape.length;
		int arrowsColumns=shape[0].length;
		//ѡȡ����Խ��
		int minimumRows=(arrowsRows<rows)?arrowsRows:rows;
		int minimunColumns=(arrowsColumns<columns)?arrowsColumns:columns;
		synchronized(this)
		{
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<columns;j++)
				{
					currentGeneration[i][j]=CellStatus.Dead;
				}
			}
			for(int i=0;i<minimumRows;i++)
			{
				for(int j=0;j<minimunColumns;j++)
				{
					if(shape[i][j]==1)
					{
						currentGeneration[i][j]=CellStatus.Active;
					}
					
				}
			}
			isChanging=false;
			this.notifyAll();//����
		}
		
	}
	
	public void evolve(int x,int y)//�жϺ���,����next����
	{
		int activeSurroundingCell=0;
		
		if(isVaildCell(x-1,y-1)&&(currentGeneration[x-1][y-1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x,y-1)&&(currentGeneration[x][y-1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y-1)&&(currentGeneration[x+1][y-1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y)&&(currentGeneration[x+1][y]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y+1)&&(currentGeneration[x+1][y+1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x,y+1)&&(currentGeneration[x][y+1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x-1,y+1)&&(currentGeneration[x-1][y+1]==CellStatus.Active))
			activeSurroundingCell++;
		if(isVaildCell(x-1,y)&&(currentGeneration[x-1][y]==CellStatus.Active))
			activeSurroundingCell++;
		
		if(activeSurroundingCell==3)
		{
			nextGeneration[x][y]=CellStatus.Active;
		}
		else if(activeSurroundingCell==2)
		{
			nextGeneration[x][y]=currentGeneration[x][y];
		}
		else
		{
			nextGeneration[x][y]=CellStatus.Dead;
		}
		
		/*
		for(int i=x-1;i<=x+1;i++)
		{
			for(int j=y-1;j<=y+1;j++) {
				
				if(i!=x&&j!=y)
				  if(isVaildCell(i,j)&&(currentGeneration[i][j]==CellStatus.Active))
					activeSurroundingCell++;
			}
			}

			
		if(activeSurroundingCell==3)
		{
			nextGeneration[x][y]=CellStatus.Active;
		}
		else if(activeSurroundingCell==2)
		{
			nextGeneration[x][y]=currentGeneration[x][y];
		}
		else
		{
			nextGeneration[x][y]=CellStatus.Dead;
		}
		*/
	}
	private boolean isVaildCell(int x,int y)
	{
		if((x>=0)&&(x<rows)&&(y>=0)&&(y<columns))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private void sleep(int x)
	{
		try {
			Thread.sleep(80*x);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
	static enum CellStatus
	{
		Active,
		Dead;
	}
	
	public void setspeed(int s) {
		time.SetSpeed(s);
	}
	public void setlimit(int l) {
		daishulimit=l;
	}

}