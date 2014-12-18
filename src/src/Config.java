public class Config
{
	public long simTime;
	public int numNodes;
	public int enterRate;
	public int exitRate;
	public int chaffGenRate;
	public int txGenRate;
	public int gridWidth;
	public int gridHeight;
	public int circuitWidth; 
	public int circuitDepth;
	public int buffSize;
	public int mixDelay;
	public int pktSize;
	public long seed;
	public int retryLimit;
	public String outfileprefix;
	public String path;
	public boolean genMatrices;
	public int initialAddressSize;
	public int validNodeTransmitReq;
	public int addressBookSize;
	
	// Default to true
	public boolean keepInMemory = true;
}
