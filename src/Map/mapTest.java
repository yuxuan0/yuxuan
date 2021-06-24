package Map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Map.map.CellStatus;

public class mapTest {
private static map mmap=new map(3,3); 

	@Before
	public void setUp() throws Exception {
		for(int i=0;i<3;i++)
			for(int j=0;j<3;j++)
				mmap.currentGeneration[i][j]=CellStatus.Active;
	}

	@Test
	public void testMap() {
	
	}

	@Test
	public void testEvolve() {
		mmap.evolve(1, 1);
		mmap.evolve(0,0);
		assertEquals(CellStatus.Dead,mmap.nextGeneration[1][1]);
		assertEquals(CellStatus.Active,mmap.nextGeneration[0][0]);
	
	}

}
