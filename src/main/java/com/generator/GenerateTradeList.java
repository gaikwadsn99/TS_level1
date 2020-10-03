package com.generator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays; 
import java.util.Random;

import com.pojo.Market;
import com.pojo.MarketList;
import com.pojo.Trade;


public class GenerateTradeList {
	

	public List[] Generate()
	{
		List<MarketList> ML = new ArrayList<>();
		double apple[] = {115,20,20,120};
		double fb[]= {250,15,15,270};
		double walmart[] = {137,18,20,145};
		ML.add(new MarketList(1,apple));
		ML.add(new MarketList(2,fb));
		ML.add(new MarketList(3,walmart));


		int random;
		int [] cid={1,2,3,200};
		String [] bname = {"b1","b2","b3"};
		int index=0;
		int [] sec={1,2,3,4};
		String Sname[][]= {{"Apple ES","Apple Put Option","Apple Call Option","Apple Futures"},
							{"Facebook ES","Facebook Put Option","Facebook Call Option","Facebook Futures"},
							{"Walmart ES","Walmart Put Option","Walmart Call Option","Walmart Futures"}};
		boolean [] type={true,false};
		int fbr=0; 
		int []fr={1000,1000,1000};

		
		int arr[] = {100,150,200,250,300,350,400,450,500,550,600,650,700,750,800,850,900,950,1000};  
		int freq[] = {1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,1,2,3,4};  
		int n = arr.length;

		// securityId,security,securityName,currentMarketPrice
		Random rand = new Random();
		random = rand.nextInt(5)+1;
		int a=random;
		//random = 6;
		int [] frindex=new int[random];
		int frpointer=0; 
		for(int i = 0;i<random;i++)
		{
			frindex[i]=rand.nextInt(292);

		}

		Arrays.sort(frindex);

		for(int i = 0;i<random;i++)
		{
			if(i!=0&&frindex[i]-frindex[i-1]<15)
				frindex[i]+=15;


		}

		List<Trade>li = new ArrayList<>();
		List<Market>m = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		
		for(int i = 0;i<300;i++)
		{
			li.add(new Trade());
			li.get(i).getTradeExecutionTime().setTime(c.getTime());
			li.get(i).setTradeId(i+1);
			m.add(new Market());
			m.get(i).setMarketId(i+1);
			
			m.get(i).getTradeTime().setTime(c.getTime());
			
			c.add(Calendar.SECOND, 2);
		}
		
		int count = 0;
		for(int i=0;i<300;i++)
		{	

			List<Trade>frl=new ArrayList<>();
			frl.add(new Trade());
			frl.add(new Trade());
			frl.add(new Trade());
			if(frpointer<a&&fr[0]+fr[1]+fr[2]==3000&&i==frindex[frpointer])
			{

				frl = getScenario();

				fr[0]=i;
				fr[1]=rand.nextInt(5)+i+1; 
				fr[2]=rand.nextInt(5)+fr[1]+1;
				// The split number for the final firm trade in the fr scenario
				fbr=rand.nextInt(3)+1; 
				if((fbr+fr[2])-fr[0]-1>10)
					fbr = 1;
				//code for front running scenario  
				
				frpointer++;

				//firm trade
				li.get(fr[0]).setCustomerId(frl.get(0).getCustomerId());
				li.get(fr[0]).setSecurity(frl.get(0).getSecurity());
				li.get(fr[0]).setTradeType(frl.get(0).isTradeType());
				li.get(fr[0]).setQuantity(frl.get(0).getQuantity());
				li.get(fr[0]).setSecurityId(frl.get(0).getSecurityId());
				li.get(fr[0]).setBrokerName(frl.get(0).getBrokerName());
				index = frl.get(0).getSecurityId()-1; 
				
				//customer trade
				li.get(fr[1]).setCustomerId(frl.get(1).getCustomerId());
				li.get(fr[1]).setSecurity(frl.get(1).getSecurity());
				li.get(fr[1]).setTradeType(frl.get(1).isTradeType());
				li.get(fr[1]).setQuantity(frl.get(1).getQuantity());
				li.get(fr[1]).setSecurityId(frl.get(1).getSecurityId());
				li.get(fr[1]).setBrokerName(frl.get(0).getBrokerName());
				
				//firm trade
				for(int k=0;k<fbr;k++)
				{
					index = frl.get(2).getSecurityId()-1; 
					li.get(fr[2]+k).setCustomerId(frl.get(2).getCustomerId());
					li.get(fr[2]+k).setSecurity(frl.get(2).getSecurity());
					li.get(fr[2]+k).setTradeType(frl.get(2).isTradeType());
					li.get(fr[2]+k).setSecurityId(frl.get(2).getSecurityId());
					li.get(fr[2]+k).setBrokerName(frl.get(0).getBrokerName());
					
					if(k==fbr-1)
					{	
						int q = frl.get(2).getQuantity()-((k)*(frl.get(2).getQuantity()/fbr));
						li.get(fr[2]+k).setQuantity(q);
					}
					else
					{	
						li.get(fr[2]+k).setQuantity(frl.get(2).getQuantity()/fbr);						
					}
		
				}


			}

			if(i==fr[0])
			{

				li.get(fr[0]).setMarketPrice(ML.get(index).getPrice()[li.get(fr[0]).getSecurity()-1]);
				li.get(fr[0]).setPrice(ML.get(index).getPrice()[li.get(fr[0]).getSecurity()-1]+rand.nextDouble());
				li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
				PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
				m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
						fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
				fr[0]=1000;
				continue;

			}
			if(i==fr[1])
			{   
				li.get(i).setMarketPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]);
				li.get(i).setPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]+rand.nextDouble());
				li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
				
				PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
				m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
						fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
				fr[1]=1000;
				continue;
			}
			if(i==fr[2])
			{
				
				if(fbr == 1)
				{
					li.get(i).setMarketPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]);
					li.get(i).setPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]+rand.nextDouble());
					li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
					PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
					m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
							fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
					continue;
				}
				else {
					int k = 1;
					for( k=1;k<fbr;k++)
					{
						li.get(i).setMarketPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]);
						li.get(i).setPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]+rand.nextDouble());
						li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
						
						PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
						m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
								fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
						i++;
					}
					li.get(i).setMarketPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]);
					li.get(i).setPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]+rand.nextDouble());
					li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
					PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
					m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
							fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
				}
				fr[2]=1000;
				continue;

			}
			//random customer id
			random = rand.nextInt(4);
			li.get(i).setCustomerId(cid[random]); //0 - 3
			random = rand.nextInt(2);
			li.get(i).setBrokerName(bname[random]);
			//random security
			int security = rand.nextInt(3)+1;

			li.get(i).setSecurityId(security);
			index = li.get(i).getSecurityId()-1;
			//random type
			random = rand.nextInt(2);
			li.get(i).setTradeType(type[random]);
			// isChecked taken care of by constructor
			//random quantity distribution ?
			li.get(i).setQuantity( myRand(arr, freq, n));
			random = rand.nextInt(4); //0-3
			li.get(i).setSecurity(random+1);
			//price based on quantity
			li.get(i).setMarketPrice(ML.get(index).getPrice()[random]);
			li.get(i).setPrice(ML.get(index).getPrice()[li.get(i).getSecurity()-1]+rand.nextDouble());
			li.get(i).setSecurityName(Sname[li.get(i).getSecurityId()-1][li.get(i).getSecurity()-1]);
			
			PriceFluctuation(ML, li.get(i).isTradeType(), li.get(i).getQuantity(), li.get(i).getSecurity(),index);
			m.get(i).update( apple[0], apple[1], apple[2], apple[3], fb[0], 
					fb[1], fb[2], fb[2], walmart[0],  walmart[1],  walmart[2],  walmart[3]);
		}
		System.out.println(m.size());
		return new List[] {li,m};
	
		
	}


	static int findCeil(int arr[], int r, int l, int h)  
	{  
		int mid;  
		while (l < h)  
		{  
			mid = l + ((h - l) >> 1); // Same as mid = (l+h)/2  
			if(r > arr[mid])  
				l = mid + 1; 
			else
				h = mid;  
		}  
		return (arr[l] >= r) ? l : -1;  
	}  

	static int myRand(int arr[], int freq[], int n)  
	{  
		// Create and fill prefix array  
		int prefix[] = new int[n], i;  
		prefix[0] = freq[0];  
		for (i = 1; i < n; ++i)  
			prefix[i] = prefix[i - 1] + freq[i];  

		// prefix[n-1] is sum of all frequencies. 
		// Generate a random number with  
		// value from 1 to this sum  
		int r = ((int)(Math.random()*(323567)) % prefix[n - 1]) + 1;  

		// Find index of ceiling of r in prefix arrat  
		int indexc = findCeil(prefix, r, 0, n - 1);  
		return arr[indexc];  
	}  
	public List<Trade> getScenario()
	{
		
		Random rand = new Random();
		List<Trade>sc = new ArrayList<>();
		sc.add(new Trade());
		sc.add(new Trade());
		sc.add(new Trade());
		String [] bname = {"b1","b2","b3"};
		String b=bname[rand.nextInt(3)];
		sc.get(0).setBrokerName(b);
		sc.get(1).setBrokerName(b);
		sc.get(2).setBrokerName(b);
		int num=rand.nextInt(3)+1;
		sc.get(0).setCustomerId(200);
		sc.get(1).setCustomerId(num);
		sc.get(2).setCustomerId(200);
		int quant=rand.nextInt(300)+400;
		sc.get(0).setQuantity(quant); 
		quant=rand.nextInt(100)+900;
		sc.get(1).setQuantity(quant); 
		quant=rand.nextInt(300)+400;
		sc.get(2).setQuantity(quant);  
		
		num=rand.nextInt(2)+1;
		int security = rand.nextInt(3)+1;

		sc.get(0).setSecurityId(security);
		sc.get(1).setSecurityId(security);
		sc.get(2).setSecurityId(security);
		switch(num)
		{
		case 1:
			int [] sec=new int[2];
			sec[0]=1;
			sec[1]=4;
			int num1=rand.nextInt(2);
			int num2=rand.nextInt(4)+1;
			sc.get(0).setSecurity(num2);
			sc.get(1).setSecurity(sec[num1]);
			sc.get(2).setSecurity(num2);
			boolean f = rand.nextBoolean();

			if(f)
			{
				sc.get(1).setTradeType(true);
			} 
			else
			{
				sc.get(1).setTradeType(false);
			}

			if(num2==2)
				sc.get(0).setTradeType(!sc.get(1).isTradeType());
			else
				sc.get(0).setTradeType(sc.get(1).isTradeType());

			sc.get(2).setTradeType(!sc.get(0).isTradeType());   
			
			return sc;

		case 2:
			int [] sec1=new int[2];
			sec1[0]=2;
			sec1[1]=3;
			num1=rand.nextInt(2);
			sc.get(0).setSecurity(sec1[num1]);
			sc.get(1).setSecurity(sec1[num1]);
			sc.get(2).setSecurity(sec1[num1]);
			sc.get(0).setQuantity(sc.get(0).getQuantity()*10);
			sc.get(1).setQuantity(sc.get(1).getQuantity()*10);
			sc.get(2).setQuantity(sc.get(2).getQuantity()*10);
			
			f =rand.nextBoolean();
			if(f)
			{
				sc.get(1).setTradeType(true);
				sc.get(0).setTradeType(true);
				sc.get(2).setTradeType(false);
			} 
			else
			{
				sc.get(1).setTradeType(false);
				sc.get(0).setTradeType(false);
				sc.get(2).setTradeType(true);
			}
			
			return sc;      
		}
		return sc;
	}
	public void PriceFluctuation(List<MarketList> ml,boolean tradeType, int quantity, int security,int index) {

		int type = (tradeType)?1:-1;

		if(security == 1||security ==4)
		{
			ml.get(index).getPrice()[0] += 0.001 * quantity*type;
			ml.get(index).getPrice()[1] -= 0.0005 * quantity*type;
			ml.get(index).getPrice()[2] += 0.0005 * quantity* type;
			ml.get(index).getPrice()[3] += 0.001 * quantity* type;
		}
		else if(security == 2)
			{
			if(ml.get(index).getPrice()[1]-0.0001*quantity*type>0)
				ml.get(index).getPrice()[1]-=0.0001*quantity*type;
			else
				ml.get(index).getPrice()[1]+=0.0001*quantity*type;
			}	
		else
			if(ml.get(index).getPrice()[1]+0.0001*quantity*type>0)
				ml.get(index).getPrice()[1]+=0.0001*quantity*type;
			else
				ml.get(index).getPrice()[1]-=0.0001*quantity*type;
		

	}
	
	

}