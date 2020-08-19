import java.io.*;
class Date implements Comparable<Date>{
    private int year;
    private int month;
    private int date;
    private int hour;
    private int min;
    private int sec;
    /* Assuming mini second can be very long So better to be store in a 64-bit Integer*/
    private long minisec;
    Date(String s)
    {
        String[] components=s.split("[-:T.,Z\\s+]");
        year=Integer.parseInt(components[0]);
        month=Integer.parseInt(components[1]);
        date=Integer.parseInt(components[2]);
        hour=Integer.parseInt(components[3]);
        min=Integer.parseInt(components[4]);
        sec=Integer.parseInt(components[5]);
        minisec=Long.parseLong(components[6]);
    }
    public int compareTo(Date d)
    {
        /* How to dates will be compare */
        if(this.year!=d.year)
        return this.year-d.year;
        if(this.month!=d.month)
        return this.month-d.month;
        if(this.date!=d.date)
        return this.date-d.date;
        if(this.hour!=d.hour)
        return this.hour-d.hour;
        if(this.min!=d.min)
        return this.min-d.min;
        if(this.sec!=d.sec)
        return this.sec-d.sec;
        else if(this.minisec<d.minisec)
        return -1;
        else if(this.minisec>d.minisec)
        return 1;
        return 0;
    }
    public void print()
    {
        System.out.println(year+" "+month+" "+date+" "+hour+" "+min+" "+sec+" "+minisec+" ");
    }

}

class Solver{ 
    public static void solve(File[] files,Date from, Date end) throws Exception
    {
        
        int k=findIndex(files,from);
        if(k==-1)
        return ;
        RandomAccessFile f=new RandomAccessFile(files[k], "r");    
        long l=1;
        long r=f.length();
        long found=-1;
        /* Binary search inside the desired file to find the first required  */
        while(l<=r)
        {
            long mid=(l+r)/2L;
            /* offset of the middle line in l-r range inside a file */
            long  off=give(f,mid,r);
            if(off==-1)
                r=mid-1L;
            else
            {
                f.seek(off);
                Date dt=new Date(f.readLine());
                if(dt.compareTo(from)>=0)
                {
                    r=mid-1L;
                    found=off;
                }
                else
                l=mid+1L;
            }
        }
        if(found!=-1){
            boolean res=true;
            PrintWriter pw=new PrintWriter(System.out);
            /* Start print the require logs consecutively until we reach the 'end' log of query  */
            for(int i=k;i<files.length;i++)
            {
                f=new RandomAccessFile(files[i], "r");
                f.seek(found);
                String hold;
                while((hold=f.readLine())!=null)
                {
                    if(new Date(hold).compareTo(end)>0)
                    {
                        res=false;
                        break;
                    }
                    pw.println(hold);
                }
                f.close();
                if(!res)
                break;
                found=0;
            }
            pw.flush();
            pw.close();
        }
        f.close();
    }
    
    /* This method return the last file which have first require entry */
    public static int findIndex(File[] files,Date from) throws Exception
    {
        int l=0;
        int r=files.length-1;
        int found=-1;
        BufferedReader br;

        /* Doing Binary-Search over the list of files using their first entry only*/
        while(l<=r)
        {
            int mid=(l+r)/2;
            br=new BufferedReader(new FileReader(files[mid]));
            Date en=new Date(br.readLine());
            if(en.compareTo(from)<=0)
            {
                found=mid;
                l=mid+1;
            }
            else
            r=mid-1;
            br.close();
        }

        return found;
    }
    
    /* return the offset of the middle line asked by binary search inside a file*/
    public static long give(RandomAccessFile f,long pos,long end) throws Exception
    {
        f.seek(pos);
        f.readLine();
        long temp=f.getFilePointer();
        if(temp>=end)
        return -1;
        return temp;
    }
    
    public static long time()
    {
        return System.currentTimeMillis();
    }
    
    /* Use for Debugging :- look girst p entry from each file */
    public static void see(File[] files,int p) throws Exception
    {
        for(int i=0;i<files.length;i++)
        {
            BufferedReader br=new BufferedReader(new FileReader(files[i]));
            for(int j=0;j<p;j++)
            {
                String s=br.readLine();
                System.out.println(s);
            }
            br.close();
            System.out.println(i+"   -----END-------"+" "+files[i].length());
        }
    }
}
public class Main {
    public static void main(String args[]) throws Exception
    {
        if(args.length==0)
        return;
        File f=new File(args[0]);

        if(!f.isDirectory())
        {
            System.out.println("Specified path is not a Directory ");
            return;
        }
        /* Storing all list of files */
        String[] list=new File(args[0]).list();
        File[] files=new File[list.length];
        for(int i=0;i<list.length;i++)
        files[i]=new File(args[0]+"\\"+list[i]);

        /*Converting time from ISO 8601 to our Date object */
        Date start=new Date(args[1]);
        Date end=new Date(args[2]);

        /*  Start solve the proble, */
       Solver.solve(files,start,end);
       
    }
}
