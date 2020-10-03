package com.detection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.pojo.Trade;

//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.PdfWriter;
public class DetectionAlgo {

	double threshold = 100000;
	int seconds = 2;


	public ArrayList<ArrayList<Trade>> DetectionAl(List<Trade> li)
	{
		
		ArrayList<ArrayList<Trade>> frlist = new ArrayList<ArrayList<Trade>>();

		for(int i = 0;i<li.size();i++)
		{
			if(li.get(i).getPrice() * li.get(i).getQuantity()>=threshold 
					&& li.get(i).isChecked() == false && li.get(i).getCustomerId()!=200)
			{
				int inter[]=Interval(i,li); 
				if(inter[0]==inter[1]&&inter[1]==inter[2])
					continue;
				ArrayList<Trade> Temp = new ArrayList<>();
				if(li.get(i).getSecurity()==1 || li.get(i).getSecurity()==4)
					Temp= DetectESF(inter,li);
				else if(li.get(i).getSecurity() == 2)
					Temp= DetectPut(inter, li);
				else
					Temp= DetectCall(inter, li);
				
				if(Temp.size()>=3)
					frlist.add(Temp);
			}
		}
		
		
		return frlist;
	}
	public int[] Interval(int index,List<Trade>li){
		int interval[]= {0,0,0};
		if(index == 0 || index == li.size()-1)
			return interval; 
		Calendar tempc = (Calendar)li.get(index).getTradeExecutionTime();
		Calendar befc = (Calendar)tempc.clone();
		befc.add(Calendar.SECOND, -10);
		Calendar afterc = (Calendar)tempc.clone();
		afterc.add(Calendar.SECOND,10);

		interval[1]=index;
		int i = index;

		while(befc.before(tempc))
		{
			i--;
			if(i ==0)
				break;
			befc.add(Calendar.SECOND, seconds);
		}
		interval[0]=i;
		i = index;
		while(afterc.after(tempc))
		{
			i++;
			if(i == li.size()-1)
				break;
			afterc.add(Calendar.SECOND, -seconds);
		}
		interval[2]=i;
		return interval;

	}


	public ArrayList<Trade> DetectESF(int interval[],List<Trade>li) {
		int custIndex=interval[1];
		int startIndex=interval[0];
		int endIndex=interval[2];


		//If customer buys equities or futures
		ArrayList<Trade>tempList = new ArrayList<>();

		if(li.get(custIndex).isTradeType())
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((((li.get(j).isTradeType()==true)&&(li.get(j).getSecurity()==1||li.get(j).getSecurity()==4
						||li.get(j).getSecurity()==3))||(li.get(j).isTradeType()==false&&li.get(j).getSecurity()==2))
						&&(li.get(j).getCustomerId()==200)&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId())
						)
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&(li.get(i).getCustomerId()==200)&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId())
								)
						{
							// Print into a file or enter into db
							if(!li.get(custIndex).isChecked())
							{
								////////////////////////
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								li.get(i).setChecked(true); 
								tempList.add(li.get(i));
							}

						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			} 
		}
		//If customer sells equities or futures
		else
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((((li.get(j).isTradeType()==false)&&(li.get(j).getSecurity()==1||li.get(j).getSecurity()==4||li.get(j).getSecurity()==3))||(li.get(j).isTradeType()==true&&li.get(j).getSecurity()==2))&&(li.get(j).getCustomerId()==200)&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId())
						)
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&li.get(i).getCustomerId()==200&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId())
								)
						{

							if(!li.get(custIndex).isChecked())
							{
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								tempList.add(li.get(i));
								li.get(i).setChecked(true); 
							}

						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			} 
		} 
		return tempList;
	}

	public ArrayList<Trade> DetectCall(int interval[],List<Trade>li)
	{
		int custIndex=interval[1];
		int startIndex=interval[0];
		int endIndex=interval[2];

		//If customer buys call options
		ArrayList<Trade>tempList = new ArrayList<>();

		if(li.get(custIndex).isTradeType())
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((li.get(j).isTradeType()==true)&&(li.get(j).getSecurity()==3)&&li.get(j).getCustomerId()==200&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId()))
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&(li.get(i).getCustomerId()==200)&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId())
								)
						{
							if(!li.get(custIndex).isChecked())
							{
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								tempList.add(li.get(i));
								li.get(i).setChecked(true); 
							}
						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			}
		}

		//If firm sells call options
		else
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((li.get(j).isTradeType()==false)&&(li.get(j).getSecurity()==3)&&(li.get(j).getCustomerId()==200)&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId()))
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&(li.get(i).getCustomerId()==200)&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId()))
						{

							if(!li.get(custIndex).isChecked())
							{
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								tempList.add(li.get(i));
								li.get(i).setChecked(true); 
							} 

						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			}
		}
		return tempList;
	}


	public ArrayList<Trade> DetectPut(int interval[],List<Trade>li)
	{
		int custIndex=interval[1];
		int startIndex=interval[0];
		int endIndex=interval[2];
		//int p1=custIndex,p2=custIndex;

		ArrayList<Trade>tempList = new ArrayList<>();

		if(li.get(custIndex).isTradeType())
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((li.get(j).isTradeType()==true)&&(li.get(j).getSecurity()==2)&&(li.get(j).getCustomerId()==200)&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId()))
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&(li.get(i).getCustomerId()==200)&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId()))
						{

							if(!li.get(custIndex).isChecked())
							{
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								tempList.add(li.get(i));
								li.get(i).setChecked(true); 
							}

						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			}
		}
		else
		{
			int j=custIndex-1;

			while(startIndex<=j)
			{
				if((li.get(j).isTradeType()==false)&&(li.get(j).getSecurity()==2)&&(li.get(j).getCustomerId()==200)&&(li.get(j).getSecurityId()==li.get(custIndex).getSecurityId()))
				{
					int i=custIndex+1;
					while(i<=endIndex)
					{
						if((!li.get(i).isTradeType()==li.get(j).isTradeType())&&(li.get(j).getSecurity()==li.get(i).getSecurity())&&(li.get(i).getCustomerId()==200)&&(li.get(i).getSecurityId()==li.get(custIndex).getSecurityId()))
						{

							if(!li.get(custIndex).isChecked())
							{
								tempList.add(li.get(j));
								li.get(j).setChecked(true);

								tempList.add(li.get(custIndex));
								li.get(custIndex).setChecked(true);

								tempList.add(li.get(i));
								li.get(i).setChecked(true);
							}
							else
							{
								tempList.add(li.get(i));
								li.get(i).setChecked(true); 
							}

						}
						i=i+1;
					}
				}
				if(tempList.size()!=0)
				{
					return tempList;
				}
				j=j-1;
			}
		}
		return tempList;
	}
}
